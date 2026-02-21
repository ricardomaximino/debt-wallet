package es.brasatech.debit_wallet.application.port_out;

import org.springframework.web.multipart.MultipartFile;

public interface FileStoragePort {
    String store(String folder, String filename, MultipartFile file);

    void delete(String path);
}
