package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.TransactionRequest;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.PagingResponse;
import ac.unindra.roemah_duren_api.dto.response.TransactionDetailResponse;
import ac.unindra.roemah_duren_api.dto.response.TransactionResponse;
import ac.unindra.roemah_duren_api.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.TRANSACTION_API)
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse transactionResponse = transactionService.create(request);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(transactionResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTransaction(@PathVariable String id) {
        TransactionResponse transactionResponse = transactionService.get(id);
        CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            path = "/report",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTransactionsByBranchId(
            @RequestParam(name = "branchId", required = false) String branchId,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(name = "type") String transactionType
    ) {
        List<TransactionResponse> transactionResponses = transactionService.getAll(startDate, endDate, branchId, transactionType);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTransactions(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "all", required = false) boolean all
    ) {
        if (all) {
            List<TransactionResponse> transactionResponses = transactionService.getAll();
            CommonResponse<?> response = CommonResponse.builder()
                    .statusCode(HttpStatus.OK.value())
                    .message(ResponseMessage.SUCCESS_GET_DATA)
                    .data(transactionResponses)
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        Page<TransactionResponse> transactionResponses = transactionService.getPage(PagingRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .build());

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalElements(transactionResponses.getTotalElements())
                .totalPages(transactionResponses.getTotalPages())
                .hasNext(transactionResponses.hasNext())
                .hasPrevious(transactionResponses.hasPrevious())
                .build();

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionResponses.getContent())
                .paging(pagingResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            value = "/{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getTransactionDetail(@PathVariable String id) {
        List<TransactionDetailResponse> transactionDetailResponses = transactionService.getDetail(id);
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(transactionDetailResponses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
