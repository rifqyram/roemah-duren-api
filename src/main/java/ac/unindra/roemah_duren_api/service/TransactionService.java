package ac.unindra.roemah_duren_api.service;

import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.TransactionRequest;
import ac.unindra.roemah_duren_api.dto.response.TransactionDetailResponse;
import ac.unindra.roemah_duren_api.dto.response.TransactionResponse;
import ac.unindra.roemah_duren_api.entity.Transaction;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    TransactionResponse create(TransactionRequest request);
    Page<TransactionResponse> getPage(PagingRequest request);
    List<TransactionResponse> getAll();
    List<TransactionResponse> getAll(LocalDateTime startDate, LocalDateTime endDate, String branchId, String transactionType);
    TransactionResponse get(String id);
    Transaction getById(String id);
    List<TransactionDetailResponse> getDetail(String id);

}
