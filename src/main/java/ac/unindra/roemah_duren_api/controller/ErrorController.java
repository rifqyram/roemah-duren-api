package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler({ResponseStatusException.class})
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        log.error("Error: {}", e.getMessage());
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(e.getStatusCode().value())
                .message(e.getReason())
                .build();
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("Error DB: {}", e.getMessage());
        String message = "";
        HttpStatus status = HttpStatus.CONFLICT;

        if (e.getCause() != null) {
            String causeMessage = e.getCause().getMessage();
            if (causeMessage.contains("constraint [uk_")) {
                message = "Data sudah ada. Silahkan gunakan data lain.";
            } else if (causeMessage.contains("cannot be null")) {
                message = "Data tidak boleh kosong.";
                status = HttpStatus.BAD_REQUEST;
            } else if (causeMessage.contains("foreign key constraint")) {
                message = "Data tidak dapat dihapus karena digunakan oleh data lain.";
                status = HttpStatus.BAD_REQUEST;
            } else if (causeMessage.contains("Duplicate entry")) {
                message = "Data duplikat ditemukan. Silakan gunakan data yang berbeda.";
            } else {
                message = "Terjadi kesalahan pada server.";
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }

        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(status.value())
                .message(message)
                .build();
        return ResponseEntity.status(status).body(response);
    }

}
