package es.brasatech.debit_wallet.adapter.out.jpa.service;

import es.brasatech.debit_wallet.adapter.out.jpa.mapper.WalletMapper;
import es.brasatech.debit_wallet.adapter.out.jpa.repository.*;
import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletPersistenceAdapter implements WalletPersistencePort {

    private final WalletRepository walletRepository;
    private final DebtRepository debtRepository;
    private final DebtorRepository debtorRepository;
    private final PaymentRepository paymentRepository;
    private final WalletMapper mapper;

    @Override
    public Wallet saveWallet(Wallet wallet) {
        var entity = mapper.mapToWalletEntity(wallet);
        return mapper.mapToWallet(walletRepository.save(entity));
    }

    @Override
    public List<Wallet> findAllWalletsByUserId(UUID userId) {
        return walletRepository.findAllByUserId(userId).stream()
                .map(mapper::mapToWallet)
                .toList();
    }

    @Override
    public Debt saveDebt(Debt debt) {
        var entity = mapper.mapToDebtEntity(debt);
        return mapper.mapToDebt(debtRepository.save(entity));
    }

    @Override
    public List<Debt> findDebtsByUserId(UUID userId) {
        return debtRepository.findByUserId(userId).stream()
                .map(mapper::mapToDebt)
                .toList();
    }

    @Override
    public List<Debt> findDebtsByWalletId(UUID walletId) {
        return debtRepository.findByWalletId(walletId).stream()
                .map(mapper::mapToDebt)
                .toList();
    }

    @Override
    public Debtor saveDebtor(Debtor debtor) {
        var entity = mapper.mapToDebtorEntity(debtor);
        return mapper.mapToDebtor(debtorRepository.save(entity));
    }

    @Override
    public Payment savePayment(Payment payment) {
        var entity = mapper.mapToPaymentEntity(payment);
        return mapper.mapToPayment(paymentRepository.save(entity));
    }

    @Override
    public List<Payment> findPaymentsByDebtId(UUID debtId) {
        return paymentRepository.findByDebtId(debtId).stream()
                .map(mapper::mapToPayment)
                .toList();
    }

    @Override
    public List<Debtor> searchDebtors(String query) {
        return debtorRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query).stream()
                .map(mapper::mapToDebtor)
                .toList();
    }
}
