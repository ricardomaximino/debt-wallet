package es.brasatech.debit_wallet.domain;

public interface Operation {
    Debt apply(Debt debt);
}
