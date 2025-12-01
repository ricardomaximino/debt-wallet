// Data Models
class Debtor {
  constructor(id, name, surname, email, phone, address) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  getFullName() {
    return `${this.name} ${this.surname}`.trim();
  }
}

class Wallet {
  constructor(id, name, debts, createdAt) {
    this.id = id;
    this.name = name;
    this.debts = debts;
    this.createdAt = createdAt;
  }

  getTotalOwed() {
    return this.debts.reduce((sum, debt) => sum + debt.getOutstanding(), 0);
  }
}

class Debt {
  constructor(id, walletId, debtorId, name, description, email, value, paymentType, payments, createdAt) {
    this.id = id;
    this.walletId = walletId;
    this.debtorId = debtorId;
    this.name = name;
    this.description = description;
    this.email = email;
    this.value = parseFloat(value);
    this.paymentType = paymentType;
    this.payments = payments;
    this.createdAt = createdAt;
  }

  getTotalPaid() {
    return this.payments.reduce((sum, payment) => sum + payment.amount, 0);
  }

  getOutstanding() {
    return Math.max(0, this.value - this.getTotalPaid());
  }

  isFullyPaid() {
    return this.getOutstanding() === 0;
  }
}

class Payment {
  constructor(id, debtId, amount, date, type, createdAt) {
    this.id = id;
    this.debtId = debtId;
    this.amount = parseFloat(amount);
    this.date = date;
    this.type = type;
    this.createdAt = createdAt;
  }

}

// Application State Manager
class DebtWalletApp {
  constructor() {
    this.wallets = [];
    this.currentView = 'dashboard';
    this.currentWallet = null;
    this.currentDebt = null;
    this.searchTimeout = null;
    this.selectedDebtor = null;
  }

  async init() {
    await this.loadData();
    this.render();
    this.initializeEventListeners();
  }

  initializeEventListeners() {
    // Debtor search with debounce
    document.addEventListener('input', (e) => {
      if (e.target.id === 'debtorSearch') {
        clearTimeout(this.searchTimeout);
        const query = e.target.value.trim();

        if (query.length < 2) {
          document.getElementById('debtorResults').innerHTML = '';
          return;
        }

        this.searchTimeout = setTimeout(() => {
          this.searchDebtors(query);
        }, 300);
      }
    });

    // Close search results when clicking outside
    document.addEventListener('click', (e) => {
      if (!e.target.closest('#debtorSearch') && !e.target.closest('#debtorResults')) {
        document.getElementById('debtorResults').innerHTML = '';
      }
    });

    // Reset debtor selection when modal closes
    const debtModal = document.getElementById('debtModal');
    debtModal.addEventListener('hidden.bs.modal', () => {
      this.resetDebtorSelection();
    });
  }

  // Simulate API call to search debtors
  async searchDebtors(query) {
    const resultsContainer = document.getElementById('debtorResults');

    // Show loading state
    resultsContainer.innerHTML = `
      <div class="debtor-results-container">
        <div class="debtor-result-item text-center">
          <div class="spinner-border spinner-border-sm text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
          <small class="ms-2">Searching...</small>
        </div>
      </div>
    `;

    try {
      // Simulate API call with delay
      const debtors = await this.mockDebtorSearch(query);

      if (debtors.length === 0) {
        resultsContainer.innerHTML = `
          <div class="debtor-results-container">
            <div class="debtor-result-item text-center text-muted">
              <i class="fas fa-search me-2"></i>
              <small>No debtors found. Enter details manually.</small>
            </div>
          </div>
        `;
        return;
      }

      // Auto-fill if only one result
      if (debtors.length === 1) {
        this.selectDebtor(debtors[0]);
        resultsContainer.innerHTML = '';
        return;
      }

      // Display top 5 results
      resultsContainer.innerHTML = `
        <div class="debtor-results-container">
          ${debtors.slice(0, 5).map(debtor => `
            <div class="debtor-result-item" onclick="app.selectDebtor(${JSON.stringify(debtor).replace(/"/g, '&quot;')})">
              <div><strong>${debtor.name} ${debtor.surname}</strong></div>
              <small class="text-muted d-block">${debtor.email}</small>
              ${debtor.phone ? `<small class="text-muted"><i class="fas fa-phone me-1"></i>${debtor.phone}</small>` : ''}
            </div>
          `).join('')}
        </div>
      `;
    } catch (error) {
      resultsContainer.innerHTML = `
        <div class="debtor-results-container">
          <div class="debtor-result-item text-center text-danger">
            <i class="fas fa-exclamation-triangle me-2"></i>
            <small>Error searching debtors</small>
          </div>
        </div>
      `;
      console.error('Error searching debtors:', error);
    }
  }

  // Mock API call - Replace with actual backend call
  async mockDebtorSearch(query) {
    // Simulate network delay
    await new Promise(resolve => setTimeout(resolve, 300));

    // Mock debtor database
    const mockDebtors = [
      { id: crypto.randomUUID(), name: 'John', surname: 'Smith', email: 'john.smith@example.com', phone: '+1-555-0101', address: '123 Main St' },
      { id: crypto.randomUUID(), name: 'John', surname: 'Doe', email: 'john.doe@company.com', phone: '+1-555-0102', address: '456 Oak Ave' },
      { id: crypto.randomUUID(), name: 'Jane', surname: 'Johnson', email: 'jane.j@business.com', phone: '+1-555-0103', address: '789 Pine Rd' },
      { id: crypto.randomUUID(), name: 'Michael', surname: 'Williams', email: 'mwilliams@corp.com', phone: '+1-555-0104', address: '321 Elm St' },
      { id: crypto.randomUUID(), name: 'Sarah', surname: 'Brown', email: 'sbrown@startup.io', phone: '+1-555-0105', address: '654 Maple Dr' },
      { id: crypto.randomUUID(), name: 'David', surname: 'Jones', email: 'djones@tech.com', phone: '+1-555-0106', address: '987 Cedar Ln' },
      { id: crypto.randomUUID(), name: 'Emily', surname: 'Davis', email: 'edavis@design.co', phone: '+1-555-0107', address: '147 Birch Way' },
      { id: crypto.randomUUID(), name: 'James', surname: 'Miller', email: 'jmiller@agency.com', phone: '+1-555-0108', address: '258 Ash Ct' },
    ];

    // Filter based on query (search in name, surname, and email)
    const lowerQuery = query.toLowerCase();
    return mockDebtors.filter(debtor =>
      debtor.name.toLowerCase().includes(lowerQuery) ||
      debtor.surname.toLowerCase().includes(lowerQuery) ||
      debtor.email.toLowerCase().includes(lowerQuery)
    );
  }

  selectDebtor(debtor) {
    this.selectedDebtor = new Debtor(
      debtor.id,
      debtor.name,
      debtor.surname,
      debtor.email,
      debtor.phone,
      debtor.address
    );

    // Fill form fields
    document.getElementById('debtName').value = this.selectedDebtor.getFullName();
    document.getElementById('debtEmail').value = this.selectedDebtor.email;
    document.getElementById('selectedDebtorId').value = this.selectedDebtor.id;
    document.getElementById('debtorSearch').value = this.selectedDebtor.getFullName();

    // Clear results
    document.getElementById('debtorResults').innerHTML = '';

    // Visual feedback
    document.getElementById('debtName').classList.add('border-success');
    document.getElementById('debtEmail').classList.add('border-success');
    setTimeout(() => {
      document.getElementById('debtName').classList.remove('border-success');
      document.getElementById('debtEmail').classList.remove('border-success');
    }, 1500);
  }

  resetDebtorSelection() {
    this.selectedDebtor = null;
    document.getElementById('debtorSearch').value = '';
    document.getElementById('debtName').value = '';
    document.getElementById('debtName').value = '';
    document.getElementById('debtEmail').value = '';
    document.getElementById('selectedDebtorId').value = '';
    document.getElementById('debtorResults').innerHTML = '';
  }

  // Data Persistence
  saveData() {
    localStorage.setItem('debtWalletData', JSON.stringify(this.wallets));
  }

  async loadData() {
    try {
      const response = await fetch('http://localhost:8082/api/wallet', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
        }
      });

      const data = await response.json();

      if (data && Array.isArray(data)) {
        this.wallets = data.map(w => Object.assign(new Wallet(), w));
        this.wallets.forEach(wallet => {
          wallet.debts = wallet.debts.map(d => Object.assign(new Debt(), d));
          wallet.debts.forEach(debt => {
            debt.payments = debt.payments.map(p => Object.assign(new Payment(), p));
          });
        });
      } else {
        // Initialize with sample data
        this.initializeSampleData();
      }
    } catch (error) {
      console.error("Failed to load data:", error);
      this.initializeSampleData();
    }
  }


  initializeSampleData() {
    const wallet1 = new Wallet('Freelance Clients');
    const debt1 = new Debt(wallet1.id, 1, 'John Smith', 'Website redesign project', 'john.smith@example.com', 5000, 'one-time');
    const payment1 = new Payment(debt1.id, 2000, '2024-11-15', 'bank-transfer');
    debt1.payments.push(payment1);
    wallet1.debts.push(debt1);

    const debt2 = new Debt(wallet1.id, 3, 'Jane Johnson', 'Mobile app development', 'jane.j@business.com', 8000, 'installment');
    wallet1.debts.push(debt2);

    this.wallets.push(wallet1);

    const wallet2 = new Wallet('Consulting Projects');
    const debt3 = new Debt(wallet2.id, 5, 'Sarah Brown', 'Marketing consultation', 'sbrown@startup.io', 1500, 'recurring');
    wallet2.debts.push(debt3);

    this.wallets.push(wallet2);
    this.saveData();
  }

  // Navigation
  showDashboard() {
    this.currentView = 'dashboard';
    this.currentWallet = null;
    this.currentDebt = null;
    this.render();
  }

  showWallet(walletId) {
    this.currentWallet = this.wallets.find(w => w.id === walletId);
    this.currentView = 'wallet';
    this.currentDebt = null;
    this.render();
  }

  showDebt(debtId) {
    const debt = this.currentWallet.debts.find(d => d.id === debtId);
    this.currentDebt = debt;
    this.currentView = 'debt';
    this.render();
  }

  // CRUD Operations
  async createWallet() {
    const name = document.getElementById('walletName').value;
    if (!name) {
      return;
    }
    try {
      const response = await fetch('http://localhost:8082/api/wallet', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
      },
      body: JSON.stringify({ value: name })
      })
      const data = await response.json();
      const wallet = new Wallet();
      wallet.id = data.id;
      wallet.name = data.name;
      wallet.createdAt = data.createdAt;
      this.wallets.push(wallet);
      await this.loadData();
      bootstrap.Modal.getInstance(document.getElementById('walletModal')).hide();
      document.getElementById('walletForm').reset();
      this.render()
    }
    catch(error) {
      console.error('Create wallet failed:', error);
    }
  }

  async createDebt() {
    const name = document.getElementById('debtName').value;
    const description = document.getElementById('debtDescription').value;
    const email = document.getElementById('debtEmail').value;
    const value = document.getElementById('debtValue').value;
    const paymentType = document.getElementById('debtPaymentType').value;
    let debtorId = document.getElementById('selectedDebtorId').value;

    if (!name || !value) return;

    if (!debtorId) {
      try {
        const response = await fetch('http://localhost:8082/api/debtor', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
          },
          body: JSON.stringify({
            name: name,
            email: email,
            })
        });

        const data = await response.json();
        debtorId = data.id;
      } catch(error) {
        console.error('Create debtor failed:', error);
      }
    }

    let debt = new Debt(
      null,
      this.currentWallet.id,
      debtorId,
      name,
      description,
      email,
      value,
      paymentType,
      [],
      null
    );

    try {
      const response = await fetch('http://localhost:8082/api/debt', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
        },
        body: JSON.stringify(debt)
      });

      const data = await response.json();
      debt = new Debt(
        data.id,
        data.walletId,
        data.debtorId,
        data.name,
        data.description,
        data.email,
        parseFloat(data.value),
        data.paymentType,
        data.payments,
        data.createdAt
      );
    } catch(error) {
      console.error('Create debtor failed:', error);
    }

    this.currentWallet.debts.push(debt);
    this.saveData();

    bootstrap.Modal.getInstance(document.getElementById('debtModal')).hide();
    document.getElementById('debtForm').reset();
    this.resetDebtorSelection();
    this.render();
  }

  async recordPayment() {
    const amount = document.getElementById('paymentAmount').value;
    const date = document.getElementById('paymentDate').value;
    const type = document.getElementById('paymentType').value;

    if (!amount || !date) return;

    try {
      const response = await fetch('http://localhost:8082/api/payment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]')?.content
        },
        body: JSON.stringify(new Payment(this.currentDebt.id, amount, date, type))
      });

      const data = await response.json();

      const payment = new Payment(data.id, data.debtId, data.amount, data.date, data.type);
      payment.createdAt = data.createdAt;
      this.currentDebt.payments.push(payment);
      bootstrap.Modal.getInstance(document.getElementById('paymentModal')).hide();
      document.getElementById('paymentForm').reset();
      this.render();

    } catch(error) {
      console.error('Create payment failed:', error);
    }
  }

  // Rendering
  render() {
    const app = document.getElementById('app');

    switch(this.currentView) {
      case 'dashboard':
        app.innerHTML = this.renderDashboard();
        break;
      case 'wallet':
        app.innerHTML = this.renderWallet();
        break;
      case 'debt':
        app.innerHTML = this.renderDebt();
        break;
    }
  }

  renderDashboard() {
    const totalOwed = this.wallets.reduce((sum, w) => sum + w.getTotalOwed(), 0);

    return `
      <div class="row mb-4">
        <div class="col-md-6">
          <div class="stat-card">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <p class="text-muted mb-1">Total Amount Owed</p>
                <h2 class="mb-0">$${totalOwed.toFixed(2)}</h2>
              </div>
              <div class="text-primary" style="font-size: 3rem; opacity: 0.2;">
                <i class="fas fa-dollar-sign"></i>
              </div>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="stat-card">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <p class="text-muted mb-1">Active Wallets</p>
                <h2 class="mb-0">${this.wallets.length}</h2>
              </div>
              <div class="text-primary" style="font-size: 3rem; opacity: 0.2;">
                <i class="fas fa-wallet"></i>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="d-flex justify-content-between align-items-center mb-3">
        <h3>My Wallets</h3>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#walletModal">
          <i class="fas fa-plus me-2"></i>New Wallet
        </button>
      </div>

      ${this.wallets.length === 0 ? `
        <div class="empty-state">
          <i class="fas fa-wallet"></i>
          <h4>No wallets yet</h4>
          <p>Create your first wallet to start tracking debts</p>
        </div>
      ` : this.wallets.map(wallet => `
        <div class="wallet-card" onclick="app.showWallet('${wallet.id}')">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <h5 class="mb-1">${wallet.name}</h5>
              <small class="text-muted">${wallet.debts.length} debt(s)</small>
            </div>
            <div class="text-end">
              <h4 class="mb-0 text-primary">$${wallet.getTotalOwed().toFixed(2)}</h4>
              <small class="text-muted">owed</small>
            </div>
          </div>
        </div>
      `).join('')}
    `;
  }

  renderWallet() {
    const wallet = this.currentWallet;

    return `
      <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="#" onclick="app.showDashboard(); return false;">Dashboard</a></li>
          <li class="breadcrumb-item active">${wallet.name}</li>
        </ol>
      </nav>

      <div class="row mb-4">
        <div class="col-md-12">
          <div class="stat-card">
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h4 class="mb-1">${wallet.name}</h4>
                <p class="text-muted mb-0">${wallet.debts.length} debt(s)</p>
              </div>
              <div class="text-end">
                <h2 class="mb-0 text-primary">$${wallet.getTotalOwed().toFixed(2)}</h2>
                <small class="text-muted">total owed</small>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="d-flex justify-content-between align-items-center mb-3">
        <h3>Debts</h3>
        <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#debtModal">
          <i class="fas fa-plus me-2"></i>New Debt
        </button>
      </div>

      ${wallet.debts.length === 0 ? `
        <div class="empty-state">
          <i class="fas fa-file-invoice-dollar"></i>
          <h4>No debts in this wallet</h4>
          <p>Add your first debt to start tracking</p>
        </div>
      ` : wallet.debts.map(debt => `
        <div class="debt-card ${debt.isFullyPaid() ? 'fully-paid' : ''}" onclick="app.showDebt('${debt.id}')">
          <div class="d-flex justify-content-between align-items-start">
            <div class="flex-grow-1">
              <h5 class="mb-1">
                ${debt.name}
                ${debt.isFullyPaid() ? '<span class="badge bg-success ms-2">Paid</span>' : ''}
              </h5>
              <p class="text-muted mb-2">${debt.description || 'No description'}</p>
              <div class="d-flex gap-3">
                <small class="text-muted">
                  <i class="fas fa-envelope me-1"></i>${debt.email || 'No email'}
                </small>
                <small class="text-muted">
                  <i class="fas fa-credit-card me-1"></i>${debt.paymentType}
                </small>
              </div>
            </div>
            <div class="text-end">
              <h5 class="mb-1 ${debt.isFullyPaid() ? 'text-success' : 'text-danger'}">
                $${debt.getOutstanding().toFixed(2)}
              </h5>
              <small class="text-muted">of $${debt.value.toFixed(2)}</small>
              <div class="mt-2">
                <div class="progress" style="height: 6px; width: 100px;">
                  <div class="progress-bar bg-success" style="width: ${(debt.getTotalPaid() / debt.value * 100).toFixed(0)}%"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      `).join('')}
    `;
  }

  renderDebt() {
    const debt = this.currentDebt;

    return `
      <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="#" onclick="app.showDashboard(); return false;">Dashboard</a></li>
          <li class="breadcrumb-item"><a href="#" onclick="app.showWallet('${this.currentWallet.id}'); return false;">${this.currentWallet.name}</a></li>
          <li class="breadcrumb-item active">${debt.name}</li>
        </ol>
      </nav>

      <div class="row mb-4">
        <div class="col-md-12">
          <div class="stat-card">
            <div class="d-flex justify-content-between align-items-start mb-3">
              <div class="flex-grow-1">
                <h3 class="mb-2">
                  ${debt.name}
                  ${debt.isFullyPaid() ? '<span class="badge bg-success ms-2">Fully Paid</span>' : '<span class="badge bg-warning text-dark ms-2">Outstanding</span>'}
                </h3>
                <p class="text-muted mb-3">${debt.description || 'No description'}</p>
                <div class="row">
                  <div class="col-md-4 mb-2">
                    <small class="text-muted d-block">Email</small>
                    <strong>${debt.email || 'Not provided'}</strong>
                  </div>
                  <div class="col-md-4 mb-2">
                    <small class="text-muted d-block">Payment Type</small>
                    <strong>${debt.paymentType}</strong>
                  </div>
                  <div class="col-md-4 mb-2">
                    <small class="text-muted d-block">Created</small>
                    <strong>${new Date(debt.createdAt).toLocaleDateString()}</strong>
                  </div>
                </div>
              </div>
            </div>

            <div class="row text-center">
              <div class="col-md-4">
                <p class="text-muted mb-1">Total Amount</p>
                <h4 class="mb-0">$${debt.value.toFixed(2)}</h4>
              </div>
              <div class="col-md-4">
                <p class="text-muted mb-1">Paid</p>
                <h4 class="mb-0 text-success">$${debt.getTotalPaid().toFixed(2)}</h4>
              </div>
              <div class="col-md-4">
                <p class="text-muted mb-1">Outstanding</p>
                <h4 class="mb-0 text-danger">$${debt.getOutstanding().toFixed(2)}</h4>
              </div>
            </div>

            <div class="progress mt-3" style="height: 10px;">
              <div class="progress-bar bg-success" style="width: ${(debt.getTotalPaid() / debt.value * 100).toFixed(0)}%"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="d-flex justify-content-between align-items-center mb-3">
        <h4>Payment History</h4>
        ${!debt.isFullyPaid() ? `
          <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#paymentModal" onclick="document.getElementById('paymentDate').value = new Date().toISOString().split('T')[0]">
            <i class="fas fa-plus me-2"></i>Record Payment
          </button>
        ` : ''}
      </div>

      ${debt.payments.length === 0 ? `
        <div class="empty-state">
          <i class="fas fa-money-bill-wave"></i>
          <h4>No payments yet</h4>
          <p>Record the first payment to start tracking</p>
        </div>
      ` : debt.payments.sort((a, b) => new Date(b.date) - new Date(a.date)).map(payment => `
        <div class="payment-item">
          <div class="d-flex justify-content-between align-items-center">
            <div>
              <h6 class="mb-1">$${payment.amount.toFixed(2)}</h6>
              <small class="text-muted">
                <i class="fas fa-calendar me-1"></i>${new Date(payment.date).toLocaleDateString()}
                <i class="fas fa-credit-card ms-3 me-1"></i>${payment.type}
              </small>
            </div>
            <div>
              <i class="fas fa-check-circle text-success" style="font-size: 1.5rem;"></i>
            </div>
          </div>
        </div>
      `).join('')}
    `;
  }
}

// Initialize the application
const app = new DebtWalletApp();
app.init();