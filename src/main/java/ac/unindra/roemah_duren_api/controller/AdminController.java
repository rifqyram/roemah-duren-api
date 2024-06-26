package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.AdminRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.AdminResponse;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.PagingResponse;
import ac.unindra.roemah_duren_api.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.ADMIN_API)
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> create(@RequestBody AdminRequest request) {
        log.info("Request to create admin: {}", request);
        AdminResponse adminResponse = adminService.create(request);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .data(adminResponse)
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .build();
        log.info("Response create admin: {}", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getOne(@PathVariable(name = "id") String id) {
        log.info("Request to get admin by id: {}", id);
        AdminResponse adminResponse = adminService.getOne(id);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .data(adminResponse)
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .build();
        log.info("Response get admin by id: {}", response);
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
        log.info("Request to get all admin with page: {}, size: {}, query: {}", page, size, query);

        if (all) {
            List<AdminResponse> adminResponses = adminService.getAll();
            CommonResponse<List<AdminResponse>> response = CommonResponse.<List<AdminResponse>>builder()
                    .data(adminResponses)
                    .statusCode(HttpStatus.OK.value())
                    .message(ResponseMessage.SUCCESS_GET_DATA)
                    .build();
            log.info("Response get all admin with page: {}, size: {}, query: {}", page, size, query);
            return ResponseEntity.ok(response);
        }

        Page<AdminResponse> pagination = adminService.getPage(PagingRequest.builder()
                .page(page)
                .size(size)
                .query(query)
                .build());

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .totalPages(pagination.getTotalPages())
                .totalElements(pagination.getTotalElements())
                .hasPrevious(pagination.hasPrevious())
                .hasNext(pagination.hasNext())
                .build();

        CommonResponse<List<AdminResponse>> response = CommonResponse.<List<AdminResponse>>builder()
                .data(pagination.getContent())
                .statusCode(HttpStatus.OK.value())
                .paging(pagingResponse)
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .build();

        log.info("Response get all admin with page: {}, size: {}, query: {}", page, size, query);
        return ResponseEntity.ok(response);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> update(@RequestBody AdminRequest request) {
        log.info("Request to update admin: {}", request);
        AdminResponse adminResponse = adminService.update(request);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .data(adminResponse)
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .build();
        log.info("Response update admin: {}", response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> delete(@PathVariable(name = "id") String id) {
        log.info("Request to delete admin by id: {}", id);
        adminService.delete(id);
        CommonResponse<AdminResponse> response = CommonResponse.<AdminResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        log.info("Response delete admin by id: {}", response);
        return ResponseEntity.ok(response);
    }

}
