package es.brasatech.debit_wallet.application.in.web;

import es.brasatech.debit_wallet.domain.PaymentType;
import es.brasatech.debit_wallet.domain.service.DebtWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class DebtWalletViewController {

    private final DebtWalletService debtWalletService;


    @GetMapping("/wallet")
    public String walletView(Model model) {
        model.addAttribute("paymentTypeList", Arrays.stream(PaymentType.values()).toList());
        return "wallet";
    }

}
