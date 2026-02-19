package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.adapter.in.web.resource.WalletNameResource;
import es.brasatech.debit_wallet.adapter.in.web.resource.DebtView;
import es.brasatech.debit_wallet.adapter.in.web.resource.DebtorView;
import es.brasatech.debit_wallet.adapter.in.web.resource.PaymentView;
import es.brasatech.debit_wallet.adapter.in.web.resource.WalletView;
import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DebtWalletApiController {

    private final DebtWalletService debtWalletService;

    @GetMapping("/wallet")
    public List<WalletView> getWallet() {
        return debtWalletService.getWalletViews(debtWalletService.getLoggedUseId());
    }

    @PostMapping("/wallet")
    public WalletView createWallet(@RequestBody WalletNameResource walletNameResource) {
        return debtWalletService.createWalletView(debtWalletService.getLoggedUseId(), walletNameResource.name());
    }

    @PostMapping("/debtor")
    public DebtorView createDebtor(@RequestBody DebtorView debtorView) {
        return debtWalletService.createDebtorView(debtorView.name(), debtorView.email());
    }

    @PostMapping("/debt")
    public DebtView createDebt(@RequestBody DebtView debtView) {
        return debtWalletService.crateDebtView(debtWalletService.getLoggedUseId(), debtView);
    }

    @PostMapping("/payment")
    public PaymentView registerPayment(@RequestBody PaymentView paymentView) {
        return debtWalletService.registerPayment(debtWalletService.getLoggedUseId(), paymentView);
    }

    @GetMapping("/debtor/search")
    public List<DebtorView> searchDebtor(@RequestParam String query) {
        return debtWalletService.searchDebtorViews(query);
    }

}
