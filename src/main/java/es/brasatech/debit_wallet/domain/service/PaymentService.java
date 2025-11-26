package es.brasatech.debit_wallet.domain.service;

import es.brasatech.debit_wallet.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface PaymentService {

    void registerPayment(UUID debtId, BigDecimal amount, String method, String note);

    Portfolio savePortfolio(Portfolio portfolio);

    List<Portfolio> getAll(UUID userId);

    List<Item> getItems(UUID portfolioId);

    List<Payment> getPayments(UUID debtId);

    Debtor getDebtorById(UUID debtorId);

    List<Debt> getDebtByPortfolioId(UUID portfolioId);

    Debt getDebtById(UUID debtId);

    Portfolio getPortfolioById(UUID portfolioId);
}
