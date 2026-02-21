package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import es.brasatech.debit_wallet.application.port_in.ProfileUseCase;
import es.brasatech.debit_wallet.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/fragments/settings")
public class ProfileViewController {

    private final ProfileUseCase profileUseCase;
    private final DebtWalletService debtWalletService;

    @GetMapping("/profile")
    public String getProfile(Model model) {
        UUID userId = debtWalletService.getLoggedUseId();
        User user = profileUseCase.getProfile(userId);
        model.addAttribute("user", user);
        return "fragments/settings/profile :: profile";
    }
}
