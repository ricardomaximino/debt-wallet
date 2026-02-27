package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.adapter.in.web.resource.DebtView;
// import es.brasatech.debit_wallet.adapter.in.web.resource.ClientView; // Removed unused
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
    private final es.brasatech.debit_wallet.application.port_in.WorkspaceUseCase workspaceUseCase;

    private UUID getWorkspaceId(String slug) {
        return workspaceUseCase.getWorkspaceBySlug(slug)
                .map(es.brasatech.debit_wallet.domain.model.Workspace::id)
                .orElseThrow(() -> new RuntimeException("Workspace not found: " + slug));
    }

    @GetMapping("/{workspaceSlug}/dashboard")
    public String getDashboard(@PathVariable String workspaceSlug, Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        UUID workspaceId = getWorkspaceId(workspaceSlug);
        List<WalletView> wallets = debtWalletService.getWalletViews(userId, workspaceId);

        java.math.BigDecimal totalOwed = wallets.stream()
                .map(WalletView::totalOwed)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        model.addAttribute("wallets", wallets);
        model.addAttribute("totalOwed", totalOwed);
        model.addAttribute("workspaceSlug", workspaceSlug);

        return "fragments/dashboard :: dashboard";
    }

    @GetMapping("/{workspaceSlug}/wallet/{walletId}")
    public String getWallet(@PathVariable String workspaceSlug, @PathVariable UUID walletId, Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        UUID workspaceId = getWorkspaceId(workspaceSlug);

        WalletView wallet = debtWalletService.getWalletViews(userId, workspaceId).stream()
                .filter(w -> w.id().equals(walletId))
                .findFirst()
                .orElseThrow();

        model.addAttribute("wallet", wallet);
        model.addAttribute("workspaceSlug", workspaceSlug);
        return "fragments/wallet :: wallet-details";
    }

    @GetMapping("/{workspaceSlug}/debt/{walletId}/{debtId}")
    public String getDebt(@PathVariable String workspaceSlug, @PathVariable UUID walletId, @PathVariable UUID debtId,
            Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        UUID workspaceId = getWorkspaceId(workspaceSlug);

        WalletView wallet = debtWalletService.getWalletViews(userId, workspaceId).stream()
                .filter(w -> w.id().equals(walletId))
                .findFirst()
                .orElseThrow();

        DebtView debt = wallet.debts().stream()
                .filter(d -> d.id().equals(debtId))
                .findFirst()
                .orElseThrow();

        model.addAttribute("wallet", wallet);
        model.addAttribute("debt", debt);
        model.addAttribute("workspaceSlug", workspaceSlug);
        return "fragments/debt :: debt-details";
    }

    @GetMapping("/{workspaceSlug}/client-results")
    public String getClientResults(@PathVariable String workspaceSlug,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "false") boolean loading,
            @RequestParam(required = false) String error,
            Model model) {
        if (query != null && !query.trim().isEmpty() && !loading && error == null) {
            UUID workspaceId = getWorkspaceId(workspaceSlug);
            model.addAttribute("clients", debtWalletService.searchClientViews(workspaceId, query));
        } else {
            model.addAttribute("clients", List.of());
        }
        model.addAttribute("loading", loading);
        model.addAttribute("error", error);
        model.addAttribute("workspaceSlug", workspaceSlug);
        return "fragments/client-results :: results";
    }

    @GetMapping("/error-alert")
    public String getErrorAlert(@RequestParam(defaultValue = "An error occurred. Please try again.") String message,
            Model model) {
        model.addAttribute("message", message);
        return "fragments/error :: alert";
    }

    @GetMapping("/{workspaceSlug}/home")
    public String getWorkspaceHome(@PathVariable String workspaceSlug, Model model) {
        UUID workspaceId = getWorkspaceId(workspaceSlug);
        model.addAttribute("workspaceSlug", workspaceSlug);
        model.addAttribute("workspaceId", workspaceId);
        return "fragments/workspace-home :: workspace-home";
    }
}
