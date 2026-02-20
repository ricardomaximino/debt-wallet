package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.adapter.in.web.resource.ClientView;
import es.brasatech.debit_wallet.adapter.in.web.resource.DebtView;
import es.brasatech.debit_wallet.adapter.in.web.resource.WalletView;
import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/fragments")
@RequiredArgsConstructor
public class FragmentViewController {

    private final DebtWalletService debtWalletService;

    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        List<WalletView> wallets = debtWalletService.getWalletViews(userId);

        java.math.BigDecimal totalOwed = wallets.stream()
                .map(WalletView::totalOwed)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("wallets", wallets);
        model.addAttribute("totalOwed", totalOwed);

        return "fragments/dashboard :: dashboard";
    }

    @GetMapping("/wallet/{walletId}")
    public String getWallet(@PathVariable UUID walletId, Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        // For simplicity in this first task, we use the already enriched view from
        // service
        WalletView wallet = debtWalletService.getWalletViews(userId).stream()
                .filter(w -> w.id().equals(walletId))
                .findFirst()
                .orElseThrow();

        model.addAttribute("wallet", wallet);
        return "fragments/wallet :: wallet-details";
    }

    @GetMapping("/debt/{walletId}/{debtId}")
    public String getDebt(@PathVariable UUID walletId, @PathVariable UUID debtId, Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        WalletView wallet = debtWalletService.getWalletViews(userId).stream()
                .filter(w -> w.id().equals(walletId))
                .findFirst()
                .orElseThrow();

        DebtView debt = wallet.debts().stream()
                .filter(d -> d.id().equals(debtId))
                .findFirst()
                .orElseThrow();

        model.addAttribute("wallet", wallet);
        model.addAttribute("debt", debt);
        return "fragments/debt :: debt-details";
    }

    @GetMapping("/client-results")
    public String getClientResults(@RequestParam(required = false) String query,
            @RequestParam(defaultValue = "false") boolean loading,
            @RequestParam(required = false) String error,
            Model model) {
        if (query != null && !query.trim().isEmpty() && !loading && error == null) {
            model.addAttribute("clients", debtWalletService.searchClientViews(query));
        } else {
            model.addAttribute("clients", List.of());
        }
        model.addAttribute("loading", loading);
        model.addAttribute("error", error);
        return "fragments/client-results :: results";
    }

    @GetMapping("/error-alert")
    public String getErrorAlert(@RequestParam(defaultValue = "An error occurred. Please try again.") String message,
            Model model) {
        model.addAttribute("message", message);
        return "fragments/error :: alert";
    }
}
