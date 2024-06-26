package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.DashboardResponse;
import ac.unindra.roemah_duren_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIUrl.DASHBOARD)
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getDashboardInfo() {
        DashboardResponse dashboardResponse = dashboardService.getDashboardInfo();
        CommonResponse<DashboardResponse> response = CommonResponse.<DashboardResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(dashboardResponse)
                .build();
        return ResponseEntity.ok(response);
    }
}
