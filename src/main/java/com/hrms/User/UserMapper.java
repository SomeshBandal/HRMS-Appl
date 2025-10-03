package com.hrms.User;

import com.hrms.Entity.Role;
import com.hrms.Entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getId().toString());
        userDto.setEmail(userDto.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setAddress(user.getAddress());
        userDto.setMobileNumber(user.getMobileNumber());


        if (user.getRoles() != null) {
            userDto.setRoles(user.getRoles().stream()
                    .map(Role::getName)
                    .collect(Collectors.toSet()));

        }
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(userDto.getAddress());
        user.setMobileNumber(userDto.getMobileNumber());

        return user;
    }
}
