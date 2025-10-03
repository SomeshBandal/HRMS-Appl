package com.hrms.User;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {

    @Schema(description = "Email of User", example = "example@example.com")
    private String email;

    @Schema(description = "UserId of User", example = "10011")
    private String userId;

    @Schema(description = "Username to create an account", example = "AkashB")
    private String username;

    @Schema(description = "Password to create an account", example = "Pass@1234")
    private String password;

    @Schema(description = "Address of the user", example = "A/P Pune Main Street Block no 8")
    private String address;

    @Schema(description = "First Name of the user", example = "John")
    private String firstName;

    @Schema(description = "Last Name of the user", example = "Doe")
    private String lastName;

    @Schema(description = "Mobile Number of the user", example = "9822222212")
    private Long mobileNumber;

    @Schema(description = "Roles of the User", example = "[\"USER\", \"ADMIN\"]")
    private Set<String> roles;
}
