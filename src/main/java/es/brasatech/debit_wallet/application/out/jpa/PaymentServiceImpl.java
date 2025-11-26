package es.brasatech.debit_wallet.application.out.jpa;

import es.brasatech.debit_wallet.application.out.jpa.mapper.WalletMapper;
import es.brasatech.debit_wallet.application.out.jpa.repository.*;
import es.brasatech.debit_wallet.domain.*;
import es.brasatech.debit_wallet.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final DebtRepository debtRepo;
    private final DebtorRepository debtorRepository;
    private final ItemRepository itemRepository;
    private final PortfolioRepository portfolioRepository;
    private final WalletMapper mapper;


    @Override
    public void registerPayment(UUID debtId, BigDecimal amount, String method, String note) {

    }

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        var portfolioEntity = portfolioRepository.save(mapper.mapToPortfolioEntity(portfolio));
        return mapper.mapToPortfolio(portfolioEntity);
    }

    @Override
    public List<Portfolio> getAll(UUID userId) {
        var portfolioEntityList = portfolioRepository.findAll();
        return portfolioEntityList.stream().map(mapper::mapToPortfolio).toList();
    }

    @Override
    public List<Item> getItems(UUID portfolioId) {
        return itemRepository.findAll().stream().map(mapper::mapToItem).toList();
    }

    @Override
    public List<Payment> getPayments(UUID debtId) {
        return paymentRepository.findAll().stream().map(mapper::mapToPayment).toList();
    }

    @Override
    public Debtor getDebtorById(UUID debtorId) {
        return debtorRepository.findById(debtorId).map(mapper::mapToDebtor).orElseThrow(() -> new RuntimeException("Debtor not found"));
    }

    @Override
    public List<Debt> getDebtByPortfolioId(UUID portfolioId) {
        return debtRepo.findByPortfolioId(portfolioId).stream().map(mapper::mapToDebt).toList();
    }

    @Override
    public Debt getDebtById(UUID debtId) {
        return debtRepo.findById(debtId).map(mapper::mapToDebt).orElseThrow(() -> new RuntimeException("Debt not found"));
    }

    @Override
    public Portfolio getPortfolioById(UUID portfolioId) {
        return portfolioRepository.findById(portfolioId).map(mapper::mapToPortfolio).orElseThrow(() -> new RuntimeException("Portfolio not found"));
    }
}
