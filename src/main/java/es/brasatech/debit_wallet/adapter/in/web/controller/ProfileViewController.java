package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import es.brasatech.debit_wallet.application.port_in.ProfileUseCase;
import es.brasatech.debit_wallet.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fragments/settings")
public class ProfileViewController {

    private final ProfileUseCase profileUseCase;
    private final DebtWalletService debtWalletService;
    private final es.brasatech.debit_wallet.application.port_in.WorkspaceUseCase workspaceUseCase;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        User user = profileUseCase.getProfile(userId);
        var workspaces = workspaceUseCase.getWorkspaces(userId);
        model.addAttribute("user", user);
        model.addAttribute("workspaces", workspaces);
        return "fragments/settings/profile :: profile";
    }

    @PostMapping("/workspaces")
    @ResponseBody
    public void createWorkspace(@RequestParam String name) {
        UUID userId = debtWalletService.getLoggedUseId();
        workspaceUseCase.createWorkspace(userId, name);
    }
}
