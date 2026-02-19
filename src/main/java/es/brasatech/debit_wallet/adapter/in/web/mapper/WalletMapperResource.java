package es.brasatech.debit_wallet.adapter.in.web.mapper;

import es.brasatech.debit_wallet.adapter.in.web.resource.*;
import es.brasatech.debit_wallet.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapperResource {

    DebtResource mapToDebtResource(Debt debt);

    Debt mapToDebt(DebtResource debtResource);

    DebtorResource mapToDebtorResource(Debtor debtor);

    Debtor mapToDebtor(DebtorResource debtorResource);

    ItemResource mapToItemResource(Item item);

    Item mapToItem(ItemResource itemResource);

    PaymentResource mapToPaymentResource(Payment payment);

    Payment mapToPayment(PaymentResource paymentResource);

    PortfolioResource mapToPortfolioResource(Wallet wallet);

    Wallet mapToPortfolio(PortfolioResource portfolioResource);

    UserResource mapToUserResource(User user);

    User mapToUser(UserResource userResource);
}
