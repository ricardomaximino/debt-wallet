package es.brasatech.debit_wallet.application.in.web;

import es.brasatech.debit_wallet.application.out.jpa.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class WalletController {

    private final PortfolioRepository portfolioRepository;


    @GetMapping("/portfolios")
    public String portifolios(Model model) {
        Map<UUID, BigDecimal> balances = Map.of(UUID.randomUUID(), new BigDecimal(100));
        model.addAttribute("balances", balances);
        return "portfolios";
    }
}
