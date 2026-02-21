package es.brasatech.debit_wallet.adapter.in.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscription")
@Slf4j
public class UserSubscriptionRequestController {

    @PostMapping("/request")
    public ResponseEntity<Void> subscribe(@RequestParam String email) {
        log.info("Subscription request received for email: {}", email);
        return ResponseEntity.ok().build();
    }
}
