package com.hrms.User;

import com.hrms.Dto.UserUpdateRequest;
import com.hrms.Entity.Role;
import com.hrms.Entity.User;
import com.hrms.Exception.BaseException;
import com.hrms.Exception.UserNotFoundExceptions;
import com.hrms.Repository.RoleRepository;
import com.hrms.util.BaseResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    public BaseResponseDto registerUser(UserDto userDto) {
        BaseResponseDto response = new BaseResponseDto();
        validateAccount(userDto);

        Set<Optional<Role>> roles  = new HashSet<>();
        Optional<Role> role = roleRepository.findByName(String.valueOf(userDto.getRoles()));
        if (role.isPresent()){
            roles.add(role);
        }
        try {
            User entity = userMapper.toEntity(userDto);
            userMapper.toDto(userRepository.save(entity));
            response.setCode(String.valueOf(HttpStatus.OK.value()));
            response.setMessage("Account registered successfully");
        }catch(Exception e){
            log.error("Error while creating account", e);
            response.setCode(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
            response.setMessage("Service unavailable");
        }
        return response;
    }


    @Override
    public Page<UserDto> getAllUsers(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<User> users = userRepository.findAll(pageable);

        return users.map(userMapper::toDto);
      }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExceptions("User not found"));

        return userMapper.toDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundExceptions("User not found with id: " + id));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getMobileNumber() != null) {
            user.setMobileNumber(request.getMobileNumber());
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

private void validateAccount(UserDto userDto) {
    if (ObjectUtils.isEmpty(userDto)) {
        throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "User data must not be empty");
    }

    User user = userRepository.findByEmail(userDto.getEmail());
    if (!ObjectUtils.isEmpty(user)) {
        throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Email is already registered !!");
    }

    List<String> roles = roleRepository.findAll().stream().map(Role::getName).toList();
    if (!roles.contains(userDto.getRoles())) {
        throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Invalid role");
    }

    User mobileNumber = userRepository.findByMobileNumber(userDto.getMobileNumber());
    if (!ObjectUtils.isEmpty(mobileNumber)) {
        throw new BaseException(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Mobile Number already registered !!");
    }
  }
}
