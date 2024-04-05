package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.request.BranchRequest;
import ac.unindra.roemah_duren_api.dto.request.PagingRequest;
import ac.unindra.roemah_duren_api.dto.response.BranchResponse;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.PagingResponse;
import ac.unindra.roemah_duren_api.service.BranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = APIUrl.BRANCH_API)
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody BranchRequest request) {
        BranchResponse response = branchService.create(request);
        CommonResponse<BranchResponse> commonResponse = CommonResponse.<BranchResponse>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable(name = "id") String id) {
        BranchResponse response = branchService.getOne(id);
        CommonResponse<BranchResponse> commonResponse = CommonResponse.<BranchResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPagination(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Page<BranchResponse> response = branchService.getPagination(PagingRequest.builder()
                .page(page)
                .size(size)
                .build());
        CommonResponse<List<BranchResponse>> commonResponse = CommonResponse.<List<BranchResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(response.getContent())
                .paging(PagingResponse.builder()
                        .page(page)
                        .size(size)
                        .hasNext(response.hasNext())
                        .hasPrevious(response.hasPrevious())
                        .totalPages(response.getTotalPages())
                        .totalElement(response.getTotalElements())
                        .build())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @GetMapping(path = "params", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBranches() {
        List<BranchResponse> responses = branchService.getAll();
        CommonResponse<List<BranchResponse>> commonResponse = CommonResponse.<List<BranchResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(responses)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateBranch(@RequestBody BranchRequest request) {
        BranchResponse response = branchService.update(request);
        CommonResponse<BranchResponse> commonResponse = CommonResponse.<BranchResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(response)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }

    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBranch(@PathVariable(name = "id") String id) {
        branchService.deleteById(id);
        CommonResponse<BranchResponse> commonResponse = CommonResponse.<BranchResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
    }
}
