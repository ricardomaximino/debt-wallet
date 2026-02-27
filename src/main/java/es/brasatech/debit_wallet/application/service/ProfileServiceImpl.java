package es.brasatech.debit_wallet.application.service;

import es.brasatech.debit_wallet.application.port_in.ProfileUseCase;
import es.brasatech.debit_wallet.application.port_out.FileStoragePort;
import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileUseCase {

    private final WalletPersistencePort persistencePort;
    private final FileStoragePort fileStoragePort;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.profile.upload-dir:uploads/profiles}")
    private String uploadDir;

    @Override
    public User getProfile(UUID userId) {
        return persistencePort.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    }

    @Override
    public User updateProfile(UUID userId, String firstName, String middleName, String surname, LocalDate birthday,
            String email) {
        User user = getProfile(userId);
        User updatedUser = new User(
                user.id(),
                firstName + (middleName != null ? " " + middleName : "") + " " + surname,
                firstName,
                middleName,
                surname,
                birthday,
                email,
                user.username(),
                user.password(),
                user.profilePicturePath(),
                user.enabled(),
                user.planRole(),
                user.roles(),
                user.workspaceIds(),
                user.createdAt());
        return persistencePort.saveUser(updatedUser);
    }

    @Override
    public void changePassword(UUID userId, String currentPassword, String newPassword) {
        User user = getProfile(userId);

        boolean isDefaultPassword = passwordEncoder.matches("password", user.password());

        if (currentPassword != null && !currentPassword.isEmpty()) {
            if (!passwordEncoder.matches(currentPassword, user.password())) {
                throw new RuntimeException("Invalid current password");
            }
        } else if (!isDefaultPassword) {
            // If it's not the default password, current password is required
            throw new RuntimeException("Current password is required to change to a new one");
        }
        User updatedUser = new User(
                user.id(),
                user.name(),
                user.firstName(),
                user.middleName(),
                user.surname(),
                user.birthday(),
                user.email(),
                user.username(),
                passwordEncoder.encode(newPassword),
                user.profilePicturePath(),
                user.enabled(),
                user.planRole(),
                user.roles(),
                user.workspaceIds(),
                user.createdAt());
        persistencePort.saveUser(updatedUser);
    }

    @Override
    public String uploadPicture(UUID userId, MultipartFile file) {
        User user = getProfile(userId);
        if (user.profilePicturePath() != null) {
            fileStoragePort.delete(user.profilePicturePath());
        }

        String extension = getFileExtension(file.getOriginalFilename());
        String filename = "profile_" + userId + extension;
        String path = fileStoragePort.store(uploadDir + "/" + userId, filename, file);

        User updatedUser = new User(
                user.id(),
                user.name(),
                user.firstName(),
                user.middleName(),
                user.surname(),
                user.birthday(),
                user.email(),
                user.username(),
                user.password(),
                path,
                user.enabled(),
                user.planRole(),
                user.roles(),
                user.workspaceIds(),
                user.createdAt());
        persistencePort.saveUser(updatedUser);
        return path;
    }

    @Override
    public void removePicture(UUID userId) {
        User user = getProfile(userId);
        if (user.profilePicturePath() != null) {
            fileStoragePort.delete(user.profilePicturePath());
            User updatedUser = new User(
                    user.id(),
                    user.name(),
                    user.firstName(),
                    user.middleName(),
                    user.surname(),
                    user.birthday(),
                    user.email(),
                    user.username(),
                    user.password(),
                    null,
                    user.enabled(),
                    user.planRole(),
                    user.roles(),
                    user.workspaceIds(),
                    user.createdAt());
            persistencePort.saveUser(updatedUser);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null)
            return ".png";
        int lastDot = filename.lastIndexOf('.');
        return lastDot > 0 ? filename.substring(lastDot) : ".png";
    }
}
