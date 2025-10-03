package com.hrms.User;

import com.hrms.util.ApiResponse;
import com.hrms.util.BaseResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDto>> registerUser(
            @Parameter(description = "user details")
            @RequestBody UserDto userDto
    ) {
        BaseResponseDto responseDto = userService.registerUser(userDto);
        return ResponseEntity.ok().body(ApiResponse.success("Account created successfully"));
    }

    @GetMapping("/byId")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(
            @Parameter(description = "unique id of user")
            @Min(1)
            @PathVariable Long id) {
        try {
            UserDto userById = userService.getUserById(id);
            return ResponseEntity.ok().body(ApiResponse.success("user details fetched successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST, "user not found", e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(HttpStatus.BAD_REQUEST,"Failed to fetch record", e.getMessage()));
        }
    }
//
//    @GetMapping("/allUsers")
//    public ResponseEntity<ApiResponse<UserDto>>getAllUsers(int pageNo, int pageSize){
//        try{
//        Page<UserDto> allUsers = userService.getAllUsers(pageNo, pageSize);
//        return ResponseEntity.ok().body(ApiResponse.success("record fetched successfully", (UserDto) allUsers));
//    }catch (){
//
//        }
}
