package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.entity.UserEntity;
import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.repository.UserRepository;
import co.edu.ucc.motivaback.service.UserService;
import co.edu.ucc.motivaback.util.CommonsService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserEntity findByIdentification(String identification) throws UsernameNotFoundException {
        var byUsername = this.userRepository.findByIdentification(identification);

        try {
            var userEntities = byUsername.collectList().block();

            if (userEntities != null && !userEntities.isEmpty())
                return userEntities.get(0);
            else
                throw new UsernameNotFoundException(CommonsService.USER_NOT_FOUND);
        } catch (Exception e) {
            throw new UsernameNotFoundException(CommonsService.USER_NOT_FOUND);
        }
    }

    @Override
    public List<UserDto> findAllByIdSupervisor(Integer idSupervisor) {
        var byUsername = this.userRepository.findAllByIdSupervisor(idSupervisor);

        try {
            var userEntities = byUsername.collectList().block();

            if (userEntities != null && !userEntities.isEmpty())
                return ObjectMapperUtils.mapAll(userEntities, UserDto.class);
            else
                return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    public UserDto save(UserDto userDto, Integer id) {
        var userEntity = ObjectMapperUtils.map(userDto, UserEntity.class);
        Long count = this.userRepository.count().block();

        userEntity.setCreatedAt(new Date());
        userEntity.setCreatedBy(id.longValue());
        userEntity.setStatus(RegisterStatusEnum.ACTIVE);
        userEntity.setIdSupervisor(id.longValue());
        userEntity.setIdUser(count != null ? count + 1 : 1);
        userEntity.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        return ObjectMapperUtils.map(this.userRepository.save(userEntity).block(), UserDto.class);
    }
}
