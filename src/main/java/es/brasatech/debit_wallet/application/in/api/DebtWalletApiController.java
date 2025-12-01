package es.brasatech.debit_wallet.application.in.api;

import es.brasatech.debit_wallet.application.in.mapper.WalletMapperResource;
import es.brasatech.debit_wallet.application.in.model.WalletNameResource;
import es.brasatech.debit_wallet.application.in.web.dto.DebtView;
import es.brasatech.debit_wallet.application.in.web.dto.DebtorView;
import es.brasatech.debit_wallet.application.in.web.dto.PaymentView;
import es.brasatech.debit_wallet.application.in.web.dto.WalletView;
import es.brasatech.debit_wallet.domain.service.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DebtWalletApiController {

    private final DebtWalletService debtWalletService;
    private final WalletMapperResource mapper;


    @GetMapping("/wallet")
    public List<WalletView> getWallet() {
        return debtWalletService.getWalletViews(debtWalletService.getLoggedUseId());
    }

    @PostMapping("/wallet")
    public WalletView createWallet(@RequestBody WalletNameResource walletNameResource) {
        return debtWalletService.createWalletView(debtWalletService.getLoggedUseId(), walletNameResource.value());
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

}
