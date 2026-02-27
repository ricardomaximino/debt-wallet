package es.brasatech.debit_wallet.adapter.in.web.controller;

import es.brasatech.debit_wallet.application.port_in.DebtWalletService;
import es.brasatech.debit_wallet.application.port_in.ProfileUseCase;
import es.brasatech.debit_wallet.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileApiController {

    private final ProfileUseCase profileUseCase;
    private final DebtWalletService debtWalletService;

    @PostMapping("/update")
    public ResponseEntity<User> updateProfile(
            @RequestParam String firstName,
            @RequestParam(required = false) String middleName,
            @RequestParam String surname,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthday,
            @RequestParam String email) {
        UUID userId = debtWalletService.getLoggedUseId();
        User updatedUser = profileUseCase.updateProfile(userId, firstName, middleName, surname, birthday, email);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/password")
    public ResponseEntity<Void> changePassword(
            @RequestParam(required = false) String currentPassword,
            @RequestParam String newPassword) {
        UUID userId = debtWalletService.getLoggedUseId();
        profileUseCase.changePassword(userId, currentPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/picture")
    public ResponseEntity<Map<String, String>> uploadPicture(@RequestParam("file") MultipartFile file) {
        UUID userId = debtWalletService.getLoggedUseId();
        String path = profileUseCase.uploadPicture(userId, file);
        return ResponseEntity.ok(Map.of("path", path));
    }

    @DeleteMapping("/picture")
    public ResponseEntity<Void> removePicture() {
        UUID userId = debtWalletService.getLoggedUseId();
        profileUseCase.removePicture(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/picture-view")
    public ResponseEntity<org.springframework.core.io.Resource> getPicture(@RequestParam String path) {
        try {
            java.nio.file.Path filePath = java.nio.file.Paths.get(path);
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(
                    filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.IMAGE_PNG) // Should detect type ideally
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (java.net.MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
