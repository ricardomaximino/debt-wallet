// Application State Manager
class DebtWalletApp {
  constructor() {
    this.currentView = 'dashboard';
    this.currentWalletId = null;
    this.currentDebtId = null;
    this.searchTimeout = null;
    this.selectedDebtor = null;
    this.csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  }

  async init() {
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
          const results = document.getElementById('debtorResults');
          if (results) results.innerHTML = '';
          return;
        }

        this.searchTimeout = setTimeout(() => {
          this.searchDebtors(query);
        }, 300);
      }
    });

    // Close search results when clicking outside
    document.addEventListener('click', (e) => {
      const results = document.getElementById('debtorResults');
      if (results && !e.target.closest('#debtorSearch') && !e.target.closest('#debtorResults')) {
        results.innerHTML = '';
      }
    });

    // Reset debtor selection when modal closes
    const debtModal = document.getElementById('debtModal');
    if (debtModal) {
      debtModal.addEventListener('hidden.bs.modal', () => {
        this.resetDebtorSelection();
      });
    }
  }

  async searchDebtors(query) {
    const resultsContainer = document.getElementById('debtorResults');
    if (!resultsContainer) return;

    // Show loading fragment
    try {
      const loadingResponse = await fetch('/fragments/debtor-results?loading=true');
      resultsContainer.innerHTML = await loadingResponse.text();

      const response = await fetch(`/fragments/debtor-results?query=${encodeURIComponent(query)}`);
      const html = await response.text();
      resultsContainer.innerHTML = html;
    } catch (error) {
      console.error('Error searching debtors:', error);
      try {
        const errorResponse = await fetch(`/fragments/debtor-results?error=${encodeURIComponent('Error searching debtors')}`);
        resultsContainer.innerHTML = await errorResponse.text();
      } catch (e) {
        resultsContainer.innerHTML = '<div class="alert alert-danger">Error searching debtors</div>';
      }
    }
  }

  selectDebtorFragment(element) {
    const debtor = {
      id: element.getAttribute('data-debtor-id'),
      name: element.getAttribute('data-debtor-name'),
      email: element.getAttribute('data-debtor-email')
    };
    this.selectDebtor(debtor);
  }

  selectDebtor(debtor) {
    this.selectedDebtor = debtor;
    document.getElementById('debtName').value = `${debtor.name} ${debtor.surname || ''}`.trim();
    document.getElementById('debtEmail').value = debtor.email;
    document.getElementById('selectedDebtorId').value = debtor.id;
    document.getElementById('debtorSearch').value = `${debtor.name} ${debtor.surname || ''}`.trim();
    document.getElementById('debtorResults').innerHTML = '';
  }

  resetDebtorSelection() {
    this.selectedDebtor = null;
    document.getElementById('debtorSearch').value = '';
    document.getElementById('debtName').value = '';
    document.getElementById('debtEmail').value = '';
    document.getElementById('selectedDebtorId').value = '';
    document.getElementById('debtorResults').innerHTML = '';
  }

  // Navigation
  showDashboard() {
    this.currentView = 'dashboard';
    this.currentWalletId = null;
    this.currentDebtId = null;
    this.render();
  }

  showWallet(walletId) {
    this.currentView = 'wallet';
    this.currentWalletId = walletId;
    this.currentDebtId = null;
    this.render();
  }

  showDebt(debtId) {
    this.currentView = 'debt';
    this.currentDebtId = debtId;
    this.render();
  }

  // Rendering via AJAX Fragments
  async render() {
    const appContainer = document.getElementById('app');
    let url = '/fragments/dashboard';

    if (this.currentView === 'wallet') {
      url = `/fragments/wallet/${this.currentWalletId}`;
    } else if (this.currentView === 'debt') {
      url = `/fragments/debt/${this.currentWalletId}/${this.currentDebtId}`;
    }

    try {
      const response = await fetch(url);
      const html = await response.text();
      appContainer.innerHTML = html;
    } catch (error) {
      console.error('Failed to load fragment:', error);
      try {
        const errorResponse = await fetch(`/fragments/error-alert?message=${encodeURIComponent('Error loading content. Please try again.')}`);
        appContainer.innerHTML = await errorResponse.text();
      } catch (e) {
        appContainer.innerHTML = '<div class="alert alert-danger">Critical Error</div>';
      }
    }
  }

  // CRUD Operations
  async createWallet() {
    const name = document.getElementById('walletName').value;
    if (!name) return;

    try {
      await fetch('http://localhost:8082/api/wallet', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': this.csrfToken
        },
        body: JSON.stringify({ name: name })
      });

      bootstrap.Modal.getInstance(document.getElementById('walletModal')).hide();
      document.getElementById('walletForm').reset();
      this.render();
    } catch (error) {
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

    try {
      if (!debtorId) {
        const response = await fetch('http://localhost:8082/api/debtor', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': this.csrfToken
          },
          body: JSON.stringify({ name: name, email: email })
        });
        const data = await response.json();
        debtorId = data.id;
      }

      await fetch('http://localhost:8082/api/debt', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': this.csrfToken
        },
        body: JSON.stringify({
          walletId: this.currentWalletId,
          debtorId: debtorId,
          name: name,
          description: description,
          email: email,
          amount: parseFloat(value),
          paymentType: paymentType
        })
      });

      bootstrap.Modal.getInstance(document.getElementById('debtModal')).hide();
      document.getElementById('debtForm').reset();
      this.resetDebtorSelection();
      this.render();
    } catch (error) {
      console.error('Create debt failed:', error);
    }
  }

  async recordPayment() {
    const amount = document.getElementById('paymentAmount').value;
    const date = document.getElementById('paymentDate').value;
    const type = document.getElementById('paymentType').value;

    if (!amount || !date) return;

    try {
      await fetch('http://localhost:8082/api/payment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': this.csrfToken
        },
        body: JSON.stringify({
          debtId: this.currentDebtId,
          amount: parseFloat(amount),
          date: date,
          type: type
        })
      });

      bootstrap.Modal.getInstance(document.getElementById('paymentModal')).hide();
      document.getElementById('paymentForm').reset();
      this.render();
    } catch (error) {
      console.error('Create payment failed:', error);
    }
  }
}

// Initialize the application
const app = new DebtWalletApp();
app.init();