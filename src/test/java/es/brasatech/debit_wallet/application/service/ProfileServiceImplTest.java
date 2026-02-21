package es.brasatech.debit_wallet.application.service;

import es.brasatech.debit_wallet.application.port_out.FileStoragePort;
import es.brasatech.debit_wallet.application.port_out.WalletPersistencePort;
import es.brasatech.debit_wallet.domain.model.PlanRole;
import es.brasatech.debit_wallet.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private WalletPersistencePort persistencePort;

    @Mock
    private FileStoragePort fileStoragePort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User(
                userId,
                "Old Name",
                "Old",
                null,
                "Name",
                null,
                "old@email.com",
                "username",
                "encodedPassword",
                null,
                true,
                PlanRole.FREE,
                Set.of(),
                Set.of(),
                LocalDateTime.now());
    }

    @Test
    @DisplayName("Given an existing user, when getProfile is called, then return the user")
    void getProfile_WhenUserExists_ReturnsUser() {
        // Given
        when(persistencePort.findUserById(userId)).thenReturn(Optional.of(user));

        // When
        User result = profileService.getProfile(userId);

        // Then
        assertEquals(user, result);
    }

    @Test
    @DisplayName("Given a non-existing user, when getProfile is called, then throw RuntimeException")
    void getProfile_WhenUserDoesNotExist_ThrowsException() {
        // Given
        when(persistencePort.findUserById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> profileService.getProfile(userId));
    }

    @Test
    @DisplayName("Given a user, when updateProfile is called with valid data, then update and save user")
    void updateProfile_UpdatesAndSavesUser() {
        // Given
        when(persistencePort.findUserById(userId)).thenReturn(Optional.of(user));
        when(persistencePort.saveUser(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        String newFirst = "New";
        String newSurname = "Surname";
        String newEmail = "new@email.com";
        LocalDate birthday = LocalDate.now();

        // When
        User result = profileService.updateProfile(userId, newFirst, null, newSurname, birthday, newEmail);

        // Then
        assertNotNull(result);
        assertEquals(newFirst, result.firstName());
        assertEquals(newSurname, result.surname());
        assertEquals(newEmail, result.email());
        assertEquals(birthday, result.birthday());
        verify(persistencePort).saveUser(any(User.class));
    }

    @Test
    @DisplayName("Given a user and correct password, when changePassword is called, then update encoded password")
    void changePassword_WhenCorrectCurrentPassword_UpdatesPassword() {
        // Given
        when(persistencePort.findUserById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("current", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("newEncoded");

        // When
        profileService.changePassword(userId, "current", "new");

        // Then
        verify(persistencePort).saveUser(argThat(u -> u.password().equals("newEncoded")));
    }

    @Test
    @DisplayName("Given a user and wrong password, when changePassword is called, then throw RuntimeException")
    void changePassword_WhenWrongCurrentPassword_ThrowsException() {
        // Given
        when(persistencePort.findUserById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPassword")).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> profileService.changePassword(userId, "wrong", "new"));
    }

    @Test
    @DisplayName("Given a user with existing picture, when uploadPicture is called, then delete old and store new")
    void uploadPicture_DeletesOldAndStoresNew() {
        // Given
        User userWithPic = new User(
                userId, "Name", "First", null, "Surname", null, "email", "user", "pass", "old/path",
                true, PlanRole.FREE, Set.of(), Set.of(), LocalDateTime.now());
        when(persistencePort.findUserById(userId)).thenReturn(Optional.of(userWithPic));

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.jpg");
        when(fileStoragePort.store(anyString(), anyString(), eq(file))).thenReturn("new/path");

        // When
        profileService.uploadPicture(userId, file);

        // Then
        verify(fileStoragePort).delete("old/path");
        verify(fileStoragePort).store(contains(userId.toString()), contains("profile"), eq(file));
        verify(persistencePort).saveUser(argThat(u -> u.profilePicturePath().equals("new/path")));
    }

    @Test
    @DisplayName("Given a user with picture, when removePicture is called, then delete file and clear path")
    void removePicture_DeletesAndClearsPath() {
        // Given
        User userWithPic = new User(
                userId, "Name", "First", null, "Surname", null, "email", "user", "pass", "path/to/delete",
                true, PlanRole.FREE, Set.of(), Set.of(), LocalDateTime.now());
        when(persistencePort.findUserById(userId)).thenReturn(Optional.of(userWithPic));

        // When
        profileService.removePicture(userId);

        // Then
        verify(fileStoragePort).delete("path/to/delete");
        verify(persistencePort).saveUser(argThat(u -> u.profilePicturePath() == null));
    }
}
