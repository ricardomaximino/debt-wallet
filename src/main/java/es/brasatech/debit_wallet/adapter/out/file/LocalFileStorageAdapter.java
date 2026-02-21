package es.brasatech.debit_wallet.adapter.out.file;

import es.brasatech.debit_wallet.application.port_out.FileStoragePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Component
@Slf4j
public class LocalFileStorageAdapter implements FileStoragePort {

    @Override
    public String store(String folder, String filename, MultipartFile file) {
        try {
            Path uploadPath = Paths.get(folder);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("Stored file at: {}", filePath);
            // Normalize path to use forward slashes for URL compatibility
            return filePath.toString().replace("\\", "/");
        } catch (IOException e) {
            log.error("Could not store file", e);
            throw new RuntimeException("Could not store file", e);
        }
    }

    @Override
    public void delete(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
            log.info("Deleted file at: {}", path);
        } catch (IOException e) {
            log.warn("Could not delete file at: {}", path, e);
        }
    }
}
