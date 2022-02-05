package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.UserDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author nagredo
 * @project motiva-back
 * @class UserService
 */
public interface UserService {
    UserDto findByUsername(String username) throws UsernameNotFoundException;
}
