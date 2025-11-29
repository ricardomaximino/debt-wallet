package es.brasatech.debit_wallet.application.in.web;

import es.brasatech.debit_wallet.application.in.web.dto.WalletView;
import es.brasatech.debit_wallet.domain.service.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DebtWalletViewController {

    private final DebtWalletService debtWalletService;

    private final List<WalletView> walletViewList = new ArrayList<>();


    @GetMapping("/wallet")
    public String walletView(Model model) {
        return "wallet";
    }

}
