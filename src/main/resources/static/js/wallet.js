// Application State Manager
class DebtWalletApp {
  constructor() {
    this.currentView = 'dashboard';
    this.currentWalletId = null;
    this.currentDebtId = null;
    this.searchTimeout = null;
    this.selectedClient = null;
    this.csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
  }

  async init() {
    this.render();
    this.initializeEventListeners();
  }

  initializeEventListeners() {
    // Client search with debounce
    document.addEventListener('input', (e) => {
      if (e.target.id === 'clientSearch') {
        clearTimeout(this.searchTimeout);
        const query = e.target.value.trim();

        if (query.length < 2) {
          const results = document.getElementById('clientResults');
          if (results) results.innerHTML = '';
          return;
        }

        this.searchTimeout = setTimeout(() => {
          this.searchClients(query);
        }, 300);
      }
    });

    // Close search results when clicking outside
    document.addEventListener('click', (e) => {
      const results = document.getElementById('clientResults');
      if (results && !e.target.closest('#clientSearch') && !e.target.closest('#clientResults')) {
        results.innerHTML = '';
      }
    });

    // Reset client selection when modal closes
    const debtModal = document.getElementById('debtModal');
    if (debtModal) {
      debtModal.addEventListener('hidden.bs.modal', () => {
        this.resetClientSelection();
      });
    }
  }

  async searchClients(query) {
    const resultsContainer = document.getElementById('clientResults');
    if (!resultsContainer) return;

    // Show loading fragment
    try {
      const loadingResponse = await fetch('/fragments/client-results?loading=true');
      resultsContainer.innerHTML = await loadingResponse.text();

      const response = await fetch(`/fragments/client-results?query=${encodeURIComponent(query)}`);
      const html = await response.text();
      resultsContainer.innerHTML = html;
    } catch (error) {
      console.error('Error searching clients:', error);
      try {
        const errorResponse = await fetch('/fragments/client-results?error=true');
        resultsContainer.innerHTML = await errorResponse.text();
      } catch (e) {
        resultsContainer.innerHTML = '<div class="alert alert-danger">Error</div>';
      }
    }
  }

  selectClientFragment(element) {
    const client = {
      id: element.getAttribute('data-client-id'),
      name: element.getAttribute('data-client-name'),
      email: element.getAttribute('data-client-email')
    };
    this.selectClient(client);
  }

  selectClient(client) {
    this.selectedClient = client;
    document.getElementById('debtName').value = client.name;
    document.getElementById('debtEmail').value = client.email;
    document.getElementById('selectedClientId').value = client.id;
    document.getElementById('clientSearch').value = client.name;
    document.getElementById('clientResults').innerHTML = '';
  }

  resetClientSelection() {
    this.selectedClient = null;
    document.getElementById('clientSearch').value = '';
    document.getElementById('debtName').value = '';
    document.getElementById('debtEmail').value = '';
    document.getElementById('selectedClientId').value = '';
    document.getElementById('clientResults').innerHTML = '';
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
        const errorResponse = await fetch('/fragments/error-alert');
        appContainer.innerHTML = await errorResponse.text();
      } catch (e) {
        appContainer.innerHTML = '<div class="alert alert-danger">Error</div>';
      }
    }
  }

  // CRUD Operations
  async createWallet() {
    const name = document.getElementById('walletName').value;
    if (!name) return;

    try {
      await fetch('/api/wallet', {
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
    let clientId = document.getElementById('selectedClientId').value;

    if (!name || !value) return;

    try {
      if (!clientId) {
        const response = await fetch('/api/client', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': this.csrfToken
          },
          body: JSON.stringify({ name: name, email: email })
        });
        const data = await response.json();
        clientId = data.id;
      }

      await fetch('/api/debt', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': this.csrfToken
        },
        body: JSON.stringify({
          walletId: this.currentWalletId,
          clientId: clientId,
          name: name,
          description: description,
          email: email,
          amount: parseFloat(value),
          paymentType: paymentType
        })
      });

      bootstrap.Modal.getInstance(document.getElementById('debtModal')).hide();
      document.getElementById('debtForm').reset();
      this.resetClientSelection();
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
      await fetch('/api/payment', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': this.csrfToken
        },
        body: JSON.stringify({
          debtId: this.currentDebtId,
          amount: parseFloat(amount),
          date: date + 'T00:00:00',
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