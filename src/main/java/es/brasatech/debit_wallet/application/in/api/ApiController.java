package es.brasatech.debit_wallet.application.in.api;

import es.brasatech.debit_wallet.application.in.mapper.WalletMapperResource;
import es.brasatech.debit_wallet.application.in.model.DebtResource;
import es.brasatech.debit_wallet.application.in.model.PortfolioResource;
import es.brasatech.debit_wallet.domain.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final PaymentService paymentService;
    private final WalletMapperResource mapper;


    @PostMapping("/portfolio")
    public PortfolioResource createPortfolios(@RequestBody PortfolioResource portfolioResource) {
        var portfolio = paymentService.savePortfolio(mapper.mapToPortfolio(portfolioResource));
        return mapper.mapToPortfolioResource(portfolio);
    }

    @GetMapping("/portfolio")
    public List<PortfolioResource> getPortfolios(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        return paymentService.getAll(userId).stream().map(mapper::mapToPortfolioResource).toList();
    }

    @GetMapping("/portfolio/{portfolioId}/debt")
    public List<DebtResource> getPortfolioDebts(@PathVariable UUID portfolioId) {
        return paymentService.getDebtByPortfolioId(portfolioId).stream().map(mapper::mapToDebtResource).toList();
    }

    @PostMapping("/debt/{debtId}/payment")
    public ResponseEntity<?> addPayment(@PathVariable UUID debtId, @RequestParam BigDecimal amount, @RequestParam String method) {
        paymentService.registerPayment(debtId, amount, method, "API payment");
        return ResponseEntity.ok().build();
    }

}
