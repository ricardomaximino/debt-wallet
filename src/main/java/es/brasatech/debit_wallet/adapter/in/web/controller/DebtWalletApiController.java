package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.adapter.in.web.resource.WalletNameResource;
import es.brasatech.debit_wallet.adapter.in.web.resource.DebtView;
import es.brasatech.debit_wallet.adapter.in.web.resource.ClientView;
import es.brasatech.debit_wallet.adapter.in.web.resource.PaymentView;
import es.brasatech.debit_wallet.adapter.in.web.resource.WalletView;
import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DebtWalletApiController {

    private final DebtWalletService debtWalletService;

    @GetMapping("/wallet")
    public List<WalletView> getWallet(@RequestHeader("X-Workspace-Id") UUID workspaceId) {
        return debtWalletService.getWalletViews(debtWalletService.getLoggedUseId(), workspaceId);
    }

    @PostMapping("/wallet")
    public WalletView createWallet(@RequestHeader("X-Workspace-Id") UUID workspaceId,
            @RequestBody WalletNameResource walletNameResource) {
        return debtWalletService.createWalletView(debtWalletService.getLoggedUseId(), workspaceId,
                walletNameResource.name());
    }

    @PostMapping("/client")
    public ClientView createClient(@RequestHeader("X-Workspace-Id") UUID workspaceId,
            @RequestBody ClientView clientView) {
        return debtWalletService.createClientView(workspaceId, clientView.name(), clientView.email());
    }

    @PostMapping("/debt")
    public DebtView createDebt(@RequestHeader("X-Workspace-Id") UUID workspaceId, @RequestBody DebtView debtView) {
        return debtWalletService.crateDebtView(debtWalletService.getLoggedUseId(), workspaceId, debtView);
    }

    @PostMapping("/payment")
    public PaymentView registerPayment(@RequestHeader("X-Workspace-Id") UUID workspaceId,
            @RequestBody PaymentView paymentView) {
        return debtWalletService.registerPayment(debtWalletService.getLoggedUseId(), workspaceId, paymentView);
    }

    @GetMapping("/client/search")
    public List<ClientView> searchClient(@RequestHeader("X-Workspace-Id") UUID workspaceId,
            @RequestParam String query) {
        return debtWalletService.searchClientViews(workspaceId, query);
    }

}
