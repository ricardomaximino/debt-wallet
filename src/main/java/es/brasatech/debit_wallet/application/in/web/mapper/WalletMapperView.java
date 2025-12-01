package es.brasatech.debit_wallet.application.in.web.mapper;

import es.brasatech.debit_wallet.application.in.web.dto.DebtView;
import es.brasatech.debit_wallet.application.in.web.dto.DebtorView;
import es.brasatech.debit_wallet.application.in.web.dto.PaymentView;
import es.brasatech.debit_wallet.application.in.web.dto.WalletView;
import es.brasatech.debit_wallet.domain.Debt;
import es.brasatech.debit_wallet.domain.Debtor;
import es.brasatech.debit_wallet.domain.Payment;
import es.brasatech.debit_wallet.domain.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapperView {

    @Mapping(target = "value", source = "amount")
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
