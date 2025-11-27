package es.brasatech.debit_wallet.application.in.web;

import es.brasatech.debit_wallet.application.in.web.dto.DebtView;
import es.brasatech.debit_wallet.application.in.web.dto.DebtWalletView;
import es.brasatech.debit_wallet.application.in.web.dto.DebtorView;
import es.brasatech.debit_wallet.application.in.web.dto.WalletView;
import es.brasatech.debit_wallet.application.out.jpa.repository.PortfolioRepository;
import es.brasatech.debit_wallet.domain.PaymentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WalletController {

    private final PortfolioRepository portfolioRepository;


    @GetMapping("/wallet")
    public String walletView(Model model) {
        model.addAttribute("wallet", debtWalletView());
        return "wallet";
    }

    private DebtWalletView debtWalletView() {
        var walletList = List.of(createWalletView());
        var total = walletList.stream().map(WalletView::total).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new DebtWalletView(total, walletList.size(), walletList);
    }

    private WalletView createWalletView() {
        return new WalletView(new BigDecimal(10), List.of(createDebtView()));
    }

    private DebtView createDebtView() {
        var debitor = new DebtorView(UUID.randomUUID(),"Ricardo", "Gonçalves", "ricardo@mail.com", "900900900", "Calle Novelda, 13");
        return new DebtView(UUID.randomUUID(), "Description of the debt", new BigDecimal(10), PaymentType.FLEXIBLE, List.of(), debitor);
    }

}
