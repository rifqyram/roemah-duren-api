package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.SupplierRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.SupplierResponse;
import ac.unindra.roemah_duren_api.dto.response.PagingResponse;
import ac.unindra.roemah_duren_api.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.SUPPLIER_API)
@Slf4j
@RequiredArgsConstructor
public class SupplierController {
    private final SupplierService supplierService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody SupplierRequest request) {
        SupplierResponse supplierResponse = supplierService.create(request);
        CommonResponse<SupplierResponse> response = CommonResponse.<SupplierResponse>builder()
                .data(supplierResponse)
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getOne(@PathVariable(name = "id") String id) {
        SupplierResponse supplierResponse = supplierService.getOne(id);
        CommonResponse<SupplierResponse> response = CommonResponse.<SupplierResponse>builder()
                .data(supplierResponse)
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getAllPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "all", required = false) boolean all
    ) {
        if (all) {
            List<SupplierResponse> suppliers = supplierService.getAll();
            CommonResponse<List<SupplierResponse>> response = CommonResponse.<List<SupplierResponse>>builder()
                    .data(suppliers)
                    .statusCode(HttpStatus.OK.value())
                    .message(ResponseMessage.SUCCESS_GET_DATA)
                    .build();
            return ResponseEntity.ok(response);
        }

        Page<SupplierResponse> pagination = supplierService.getPage(PagingRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .build());

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(pagination.getTotalPages())
                .totalElements(pagination.getTotalElements())
                .hasPrevious(pagination.hasPrevious())
                .hasNext(pagination.hasNext())
                .build();

        CommonResponse<List<SupplierResponse>> response = CommonResponse.<List<SupplierResponse>>builder()
                .data(pagination.getContent())
                .statusCode(HttpStatus.OK.value())
                .paging(pagingResponse)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@RequestBody SupplierRequest request) {
        SupplierResponse supplierResponse = supplierService.update(request);
        CommonResponse<SupplierResponse> response = CommonResponse.<SupplierResponse>builder()
                .data(supplierResponse)
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id) {
        supplierService.delete(id);
        CommonResponse<SupplierResponse> response = CommonResponse.<SupplierResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.ok(response);
    }

}
