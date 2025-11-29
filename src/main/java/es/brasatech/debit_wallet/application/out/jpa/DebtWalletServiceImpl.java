package es.brasatech.debit_wallet.application.out.jpa;

import es.brasatech.debit_wallet.application.in.mapper.WalletMapperResource;
import es.brasatech.debit_wallet.application.in.web.dto.DebtorView;
import es.brasatech.debit_wallet.application.in.web.dto.WalletView;
import es.brasatech.debit_wallet.application.in.web.mapper.WalletMapperView;
import es.brasatech.debit_wallet.application.out.jpa.mapper.WalletMapper;
import es.brasatech.debit_wallet.application.out.jpa.repository.*;
import es.brasatech.debit_wallet.domain.Debtor;
import es.brasatech.debit_wallet.domain.User;
import es.brasatech.debit_wallet.domain.Wallet;
import es.brasatech.debit_wallet.domain.service.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final WalletMapper mapper;
    private final WalletMapperResource mapperResource;
    private final WalletMapperView mapperView;

    private final User user = new User(UUID.randomUUID(), "Ricardo", "ricardo@mail.com");

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
        var wallet = new Wallet(userId, name);
        var walletEntity = walletRepository.save(mapper.mapToWalletEntity(wallet));
        return mapperView.mapToWalletView(mapper.mapToWallet(walletEntity));
    }

    @Override
    public DebtorView createDebtorView(String name, String email) {
        var debtor = new Debtor(name, email);
        var debtorEntity = debtorRepository.save(mapper.mapToDebtorEntity(debtor));
        return mapperView.mapToDebtorView(mapper.mapToDebtor(debtorEntity));
    }

    @Override
    public List<WalletView> getWalletViews(UUID userId) {
        var debtList = debtRepository.findByUserId(userId).stream().map(mapper::mapToDebt).toList();
        var walletList = walletRepository.findAllByUserId(userId).stream().map(mapper::mapToWallet).toList();
        return walletList.stream().map(wallet -> {
            var debts = debtList.stream().filter(debt -> debt.walletId().equals(wallet.id())).toList();
            return mapperView.mapToWalletView(wallet, debts);
        }).toList();
    }
}
