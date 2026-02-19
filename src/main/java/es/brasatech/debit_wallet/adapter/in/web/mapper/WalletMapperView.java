package es.brasatech.debit_wallet.adapter.in.web.mapper;

import es.brasatech.debit_wallet.adapter.in.web.resource.DebtView;
import es.brasatech.debit_wallet.adapter.in.web.resource.DebtorView;
import es.brasatech.debit_wallet.adapter.in.web.resource.PaymentView;
import es.brasatech.debit_wallet.adapter.in.web.resource.WalletView;
import es.brasatech.debit_wallet.domain.model.Debt;
import es.brasatech.debit_wallet.domain.model.Debtor;
import es.brasatech.debit_wallet.domain.model.Payment;
import es.brasatech.debit_wallet.domain.model.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapperView {

    DebtView mapToDebtView(Debt debt);

    @Mapping(target = "debts", ignore = true)
    WalletView mapToWalletView(Wallet wallet);

    default WalletView mapToWalletView(Wallet wallet, List<Debt> debtList) {
        var debtViewList = debtList.stream().map(this::mapToDebtView).toList();
        var walletView = mapToWalletView(wallet);
        return new WalletView(walletView, debtViewList);
    }

    DebtorView mapToDebtorView(Debtor debtor);

    PaymentView mapToPaymentView(Payment payment);
}
