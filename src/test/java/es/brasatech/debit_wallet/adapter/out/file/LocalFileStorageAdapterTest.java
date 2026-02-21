package es.brasatech.debit_wallet.adapter.out.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileStorageAdapterTest {

    private final LocalFileStorageAdapter adapter = new LocalFileStorageAdapter();

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Given a file and folder, when store is called, then save file and return normalized path")
    void store_SavesFileAndReturnsNormalizedPath() throws IOException {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
        String folder = tempDir.resolve("profiles").toString();

        // When
        String resultPath = adapter.store(folder, "test.txt", file);

        // Then
        assertNotNull(resultPath);
        assertFalse(resultPath.contains("\\"), "Path should use forward slashes for cross-platform compatibility");

        Path storedFile = Path.of(resultPath);
        assertTrue(Files.exists(storedFile));
        assertEquals("Hello World", Files.readString(storedFile));
    }

    @Test
    @DisplayName("Given an existing file, when delete is called, then remove file from disk")
    void delete_RemovesFileIfExists() throws IOException {
        // Given
        Path fileToDelete = tempDir.resolve("delete_me.txt");
        Files.writeString(fileToDelete, "content");
        assertTrue(Files.exists(fileToDelete));

        // When
        adapter.delete(fileToDelete.toString());

        // Then
        assertFalse(Files.exists(fileToDelete));
    }

    @Test
    @DisplayName("Given a non-existent file, when delete is called, then do nothing without exception")
    void delete_WhenFileDoesNotExist_DoesNotThrowException() {
        // Given
        Path nonExistent = tempDir.resolve("nope.txt");

        // When & Then
        assertDoesNotThrow(() -> adapter.delete(nonExistent.toString()));
    }
}
