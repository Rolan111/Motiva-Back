package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.payload.UserForm;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class UserService
 */
public interface UserService {
    List<UserDto> findAll();

    UserDto create(UserForm userForm);

    UserDto update(UserForm userForm);

    boolean delete(String id);

    UserDto findById(String id);
}
