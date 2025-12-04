package es.brasatech.debit_wallet.application.out.jpa;

import es.brasatech.debit_wallet.application.in.mapper.WalletMapperResource;
import es.brasatech.debit_wallet.application.in.web.dto.DebtView;
import es.brasatech.debit_wallet.application.in.web.dto.DebtorView;
import es.brasatech.debit_wallet.application.in.web.dto.PaymentView;
import es.brasatech.debit_wallet.application.in.web.dto.WalletView;
import es.brasatech.debit_wallet.application.in.web.mapper.WalletMapperView;
import es.brasatech.debit_wallet.application.out.jpa.mapper.WalletMapper;
import es.brasatech.debit_wallet.application.out.jpa.repository.*;
import es.brasatech.debit_wallet.domain.*;
import es.brasatech.debit_wallet.domain.service.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DebtWalletServiceImpl implements DebtWalletService {

    private final PaymentRepository paymentRepository;
    private final DebtRepository debtRepository;
    private final DebtorRepository debtorRepository;
    private final ItemRepository itemRepository;
    private final WalletRepository walletRepository;
    private final WalletMapper domainMapper;
    private final WalletMapperResource resourceMapper;
    private final WalletMapperView viewMapper;

    private final User user = new User(UUID.fromString("fd4437c0-bda6-489d-964f-7e43169cace0"), "Ricardo", "ricardo@mail.com");

    @Override
    public UUID getLoggedUseId() {
        return user.id();
    }

    @Override
    public User getUserById(UUID userId) {
        return user;
    }

    @Override
    public WalletView createWalletView(UUID userId, String name) {
        var wallet = new Wallet(UUID.randomUUID(), name, LocalDateTime.now(),userId);
        var walletEntity = walletRepository.save(domainMapper.mapToWalletEntity(wallet));
        return viewMapper.mapToWalletView(domainMapper.mapToWallet(walletEntity));
    }

    @Override
    public DebtorView createDebtorView(String name, String email) {
        var debtor = new Debtor(UUID.randomUUID(), name, null, null, email, null, null, LocalDateTime.now());
        var debtorEntity = debtorRepository.save(domainMapper.mapToDebtorEntity(debtor));
        return viewMapper.mapToDebtorView(domainMapper.mapToDebtor(debtorEntity));
    }

    @Override
    public DebtView crateDebtView(UUID userId, DebtView debtView) {
        var debt = new Debt(
            UUID.randomUUID(),
            debtView.walletId(),
            debtView.debtorId(),
            userId,
            debtView.name(),
            debtView.email(),
            debtView.description(),
            debtView.value(),
            LocalDateTime.now(),
            DebtStatus.OPEN,
            debtView.paymentType(),
            List.of());
        var debtEntity = debtRepository.save(domainMapper.mapToDebtEntity(debt));
        return viewMapper.mapToDebtView(domainMapper.mapToDebt(debtEntity));
    }

    @Override
    public PaymentView registerPayment(UUID userId, PaymentView paymentView) {
        var payment = new Payment(UUID.randomUUID(), paymentView.debtId(), paymentView.amount(), paymentView.date(), paymentView.type(), LocalDateTime.now());
        var paymentEntity = paymentRepository.save(domainMapper.mapToPaymentEntity(payment));
        return viewMapper.mapToPaymentView(domainMapper.mapToPayment(paymentEntity));
    }

    @Override
    public List<WalletView> getWalletViews(UUID userId) {
        var debtList = debtRepository.findByUserId(userId).stream().map(domainMapper::mapToDebt).toList();
        var walletList = walletRepository.findAllByUserId(userId).stream().map(domainMapper::mapToWallet).toList();
        var list = walletList.stream().map(wallet -> {
            var debts = debtList.stream().filter(debt -> debt.walletId().equals(wallet.id())).toList();
            return viewMapper.mapToWalletView(wallet, debts);
        }).toList();
        return list.stream().map(w -> {
            var ds = w.debts().stream().map(d -> {
                var paymentViewList = paymentRepository.findByDebtId(d.id()).stream()
                    .map(domainMapper::mapToPayment)
                    .map(viewMapper::mapToPaymentView).toList();
                return new DebtView(d, paymentViewList);
            }).toList();
            return new WalletView(w, ds);
        }).toList();
    }
}
