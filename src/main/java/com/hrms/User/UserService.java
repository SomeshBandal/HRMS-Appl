package com.hrms.User;

import com.hrms.Dto.UserUpdateRequest;
import com.hrms.util.BaseResponseDto;
import com.hrms.util.ResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {

    public BaseResponseDto registerUser(UserDto userDto);

    Page<UserDto> getAllUsers(int pageNo, int pageSize);

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UserUpdateRequest request);

}
