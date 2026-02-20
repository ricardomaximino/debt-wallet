package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.domain.model.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class WalletViewController {

    @GetMapping("/")
    public String index() {
        return "landing";
    }

    @GetMapping("/wallet")
    public String wallet(Model model) {
        model.addAttribute("paymentTypeList", Arrays.asList(PaymentType.values()));
        return "wallet";
    }
}
