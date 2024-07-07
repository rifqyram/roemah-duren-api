package ac.unindra.roemah_duren_api.constant;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public enum TransactionType {
    INBOUND("Pembelian"),
    SALE("Penjualan"),
    TRANSFER("Transfer");

    private final String type;

    TransactionType(String type) {
        this.type = type;
    }

    public static TransactionType fromString(String type) {
        for (TransactionType transactionType : TransactionType.values()) {
            if (transactionType.type.equalsIgnoreCase(type)) {
                return transactionType;
            }
        }
        return null;
    }
}
