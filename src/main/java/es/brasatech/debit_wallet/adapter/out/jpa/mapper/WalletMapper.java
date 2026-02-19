package es.brasatech.debit_wallet.adapter.out.jpa.mapper;

import es.brasatech.debit_wallet.adapter.out.jpa.entity.*;
import es.brasatech.debit_wallet.domain.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WalletMapper {

    DebtEntity mapToDebtEntity(Debt debt);

    Debt mapToDebt(DebtEntity debtEntity);

    DebtorEntity mapToDebtorEntity(Debtor debtor);

    Debtor mapToDebtor(DebtorEntity debtorEntity);

    ItemEntity mapToItemEntity(Item item);

    Item mapToItem(ItemEntity itemEntity);

    PaymentEntity mapToPaymentEntity(Payment payment);

    Payment mapToPayment(PaymentEntity paymentEntity);

    WalletEntity mapToWalletEntity(Wallet wallet);

    Wallet mapToWallet(WalletEntity walletEntity);

    UserEntity mapToUserEntity(User user);

    User mapToUser(UserEntity userEntity);
}
