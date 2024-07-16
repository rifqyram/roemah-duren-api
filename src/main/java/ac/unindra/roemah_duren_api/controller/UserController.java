package ac.unindra.roemah_duren_api.controller;

import ac.unindra.roemah_duren_api.constant.APIUrl;
import ac.unindra.roemah_duren_api.constant.ResponseMessage;
import ac.unindra.roemah_duren_api.dto.response.CommonResponse;
import ac.unindra.roemah_duren_api.dto.response.UserResponse;
import ac.unindra.roemah_duren_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIUrl.USER_API)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> getInfoByToken() {
        UserResponse userResponse = userService.getUserInfoByContext();
        CommonResponse<UserResponse> response = CommonResponse.<UserResponse>builder()
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(userResponse)
                .statusCode(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }

}
