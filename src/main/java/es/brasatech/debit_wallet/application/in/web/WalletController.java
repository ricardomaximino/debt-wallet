package es.brasatech.debit_wallet.application.in.web;

import es.brasatech.debit_wallet.application.in.model.WalletNameResource;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WalletController {

    private final PortfolioRepository portfolioRepository;

    private final List<WalletView> walletViewList = new ArrayList<>();


    @GetMapping("/wallet")
    public String walletView(Model model) {
        model.addAttribute("wallet", debtWalletView());
        return "wallet";
    }

    @ResponseBody
    @GetMapping("/api/wallet")
    public List<WalletView> getWallet() {
        return walletViewList;
    }

    @ResponseBody
    @PostMapping("/api/wallet")
    public WalletView createWallet(@RequestBody WalletNameResource walletNameResource) {
        var walletView = new WalletView(walletNameResource.value());
        walletViewList.add(walletView);
        return walletView;
    }

    private DebtWalletView debtWalletView() {
        return null;
    }

    private WalletView createWalletView() {
        var walletId = UUID.randomUUID();
        return new WalletView(walletId, "My Wallet", List.of(createDebtView(walletId)));
    }

    private DebtView createDebtView(UUID walletId) {
        var debitor = new DebtorView(UUID.randomUUID(),"Ricardo", "Gonçalves", "ricardo@mail.com", "900900900", "Calle Novelda, 13");
        return new DebtView(UUID.randomUUID(), "Description of the debt", walletId, new BigDecimal(10), PaymentType.FLEXIBLE, List.of(), debitor);
    }

}
