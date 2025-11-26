package es.brasatech.debit_wallet.application.in.mapper;

import es.brasatech.debit_wallet.application.in.model.*;
import es.brasatech.debit_wallet.domain.*;
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

    PortfolioResource mapToPortfolioResource(Portfolio portfolio);
    Portfolio mapToPortfolio(PortfolioResource portfolioResource);

    UserResource mapToUserResource(User user);
    User mapToUser(UserResource userResource);
}
