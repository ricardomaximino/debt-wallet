package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import es.brasatech.debit_wallet.application.port_in.ProfileUseCase;
import es.brasatech.debit_wallet.application.port_in.WorkspaceUseCase;
import es.brasatech.debit_wallet.domain.model.PaymentType;
import es.brasatech.debit_wallet.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/settings")
public class ProfilePageController {

    private final ProfileUseCase profileUseCase;
    private final DebtWalletService debtWalletService;
    private final WorkspaceUseCase workspaceUseCase;

    @GetMapping("/profile")
    public String profilePage(Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        User user = profileUseCase.getProfile(userId);
        var workspaces = workspaceUseCase.getWorkspaces(userId);

        model.addAttribute("user", user);
        model.addAttribute("workspaces", workspaces);
        model.addAttribute("paymentTypeList", Arrays.asList(PaymentType.values()));

        return "settings/profile-page";
    }
}
