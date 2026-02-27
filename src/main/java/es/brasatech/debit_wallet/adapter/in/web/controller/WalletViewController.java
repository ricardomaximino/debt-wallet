package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import es.brasatech.debit_wallet.application.port_in.WorkspaceUseCase;
import es.brasatech.debit_wallet.domain.model.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class WalletViewController {

    private final DebtWalletService debtWalletService;
    private final WorkspaceUseCase workspaceUseCase;

    @GetMapping("/")
    public String index(org.springframework.ui.Model model) {
        try {
            java.util.UUID userId = debtWalletService.getLoggedUseId();
            if (userId != null) {
                model.addAttribute("workspaces", workspaceUseCase.getWorkspaces(userId));
            }
        } catch (Exception e) {
            // User not logged in or other error
        }
        return "landing";
    }

    @GetMapping("/wallet")
    public String wallet(Model model) {
        try {
            java.util.UUID userId = debtWalletService.getLoggedUseId();
            var workspaces = workspaceUseCase.getWorkspaces(userId);
            if (!workspaces.isEmpty()) {
                return "redirect:/w/" + workspaces.get(0).slug();
            }
        } catch (Exception e) {
        }

        model.addAttribute("paymentTypeList", Arrays.asList(PaymentType.values()));
        return "wallet";
    }

    @GetMapping("/w/{slug}")
    public String workspace(@PathVariable String slug, Model model) {
        var workspace = workspaceUseCase.getWorkspaceBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Workspace not found: " + slug));

        model.addAttribute("workspaceSlug", workspace.slug());
        model.addAttribute("workspaceId", workspace.id());
        model.addAttribute("workspaceName", workspace.name());
        model.addAttribute("paymentTypeList", Arrays.asList(PaymentType.values()));

        return "wallet";
    }
}
