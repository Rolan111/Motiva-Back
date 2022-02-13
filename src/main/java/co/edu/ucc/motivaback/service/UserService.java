package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.entity.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class UserService
 */
public interface UserService {
    UserEntity findByIdentification(String identification) throws UsernameNotFoundException;

    List<UserDto> findAllByIdSupervisor(Integer idSupervisor);

    UserDto save(UserDto userDto, Integer id);
}
