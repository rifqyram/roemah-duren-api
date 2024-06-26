package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.CustomerRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.CustomerResponse;
import ac.unindra.roemah_duren_api.dto.response.PagingResponse;
import ac.unindra.roemah_duren_api.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.CUSTOMER_API)
@Slf4j
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody CustomerRequest request) {
        log.info("Request to create customer: {}", request);
        CustomerResponse customerResponse = customerService.create(request);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .data(customerResponse)
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .build();
        log.info("Response create customer: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getOne(@PathVariable(name = "id") String id) {
        log.info("Request to get customer by id: {}", id);
        CustomerResponse customerResponse = customerService.getOne(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .data(customerResponse)
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .build();
        log.info("Response get customer by id: {}", response);
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
        log.info("Request to get all customer with page: {}, size: {}, query: {}", page, size, query);

        if (all) {
            List<CustomerResponse> customerResponses = customerService.getAll();
            CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                    .data(customerResponses)
                    .statusCode(HttpStatus.OK.value())
                    .message(ResponseMessage.SUCCESS_GET_DATA)
                    .build();
            log.info("Response get all customer with page: {}, size: {}, query: {}", page, size, query);
            return ResponseEntity.ok(response);
        }

        Page<CustomerResponse> pagination = customerService.getPagination(PagingRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .build());

        PagingResponse pagingResponse = PagingResponse.builder()
                .size(page)
                .size(size)
                .totalPages(pagination.getTotalPages())
                .totalElements(pagination.getTotalElements())
                .hasPrevious(pagination.hasPrevious())
                .hasNext(pagination.hasNext())
                .build();

        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .data(pagination.getContent())
                .statusCode(HttpStatus.OK.value())
                .paging(pagingResponse)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .build();

        log.info("Response get all customer with page: {}, size: {}, query: {}", page, size, query);
        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@RequestBody CustomerRequest request) {
        log.info("Request to update customer: {}", request);
        CustomerResponse customerResponse = customerService.update(request);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .data(customerResponse)
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();
        log.info("Response update customer: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id) {
        log.info("Request to delete customer by id: {}", id);
        customerService.deleteById(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        log.info("Response delete customer by id: {}", response);
        return ResponseEntity.ok(response);
    }

}
