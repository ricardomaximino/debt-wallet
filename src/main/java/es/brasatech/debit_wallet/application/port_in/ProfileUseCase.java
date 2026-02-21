package es.brasatech.debit_wallet.application.port_in;

import es.brasatech.debit_wallet.domain.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

public interface ProfileUseCase {
    User getProfile(UUID userId);

    User updateProfile(UUID userId, String firstName, String middleName, String surname, LocalDate birthday,
            String email);

    void changePassword(UUID userId, String currentPassword, String newPassword);

    String uploadPicture(UUID userId, MultipartFile file);

    void removePicture(UUID userId);
}
