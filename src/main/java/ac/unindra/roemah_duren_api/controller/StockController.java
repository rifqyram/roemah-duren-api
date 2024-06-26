package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.request.StockRequest;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.PagingResponse;
import ac.unindra.roemah_duren_api.dto.response.StockResponse;
import ac.unindra.roemah_duren_api.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(APIUrl.STOCK_API)
public class StockController {
    private final StockService stockService;

    @PostMapping
    public ResponseEntity<?> createStock(@RequestBody StockRequest stockRequest) {
        StockResponse stockResponse = stockService.create(stockRequest);

        CommonResponse<StockResponse> response = CommonResponse.<StockResponse>builder()
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .statusCode(HttpStatus.CREATED.value())
                .data(stockResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "q", defaultValue = "", required = false) String query,
            @RequestParam(name = "all", defaultValue = "false", required = false) boolean all
    ) {
        if (all) {
            CommonResponse<List<StockResponse>> response = CommonResponse.<List<StockResponse>>builder()
                    .message(ResponseMessage.SUCCESS_GET_DATA)
                    .statusCode(HttpStatus.OK.value())
                    .data(stockService.getAll())
                    .build();
            return ResponseEntity.ok(response);
        }

        Page<StockResponse> responsePage = stockService.getAll(PagingRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .build());

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalElements(responsePage.getTotalElements())
                .totalPages(responsePage.getTotalPages())
                .hasNext(responsePage.hasNext())
                .hasPrevious(responsePage.hasPrevious())
                .build();

        CommonResponse<List<StockResponse>> response = CommonResponse.<List<StockResponse>>builder()
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .statusCode(HttpStatus.OK.value())
                .data(stockService.getAll())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateStock(@RequestBody StockRequest stockRequest) {
        StockResponse stockResponse = stockService.update(stockRequest);

        CommonResponse<StockResponse> response = CommonResponse.<StockResponse>builder()
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .statusCode(HttpStatus.OK.value())
                .data(stockResponse)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStock(@PathVariable String id) {
        stockService.delete(id);

        CommonResponse<Void> response = CommonResponse.<Void>builder()
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok(response);
    }

}
