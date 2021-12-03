package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.LoginDto;
import co.edu.ucc.motivaback.payload.LoginForm;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class UserService
 */
public interface UserService {
    List<LoginDto> findAll();

    LoginDto create(LoginForm loginForm);

    LoginDto update(LoginForm loginForm);

    boolean delete(String id);

    LoginDto findById(String id);
}
