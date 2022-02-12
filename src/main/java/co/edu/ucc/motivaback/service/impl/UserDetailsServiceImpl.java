package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.entity.UserEntity;
import co.edu.ucc.motivaback.repository.UserRepository;
import co.edu.ucc.motivaback.util.CommonsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identification) throws UsernameNotFoundException {
        Flux<UserEntity> byIdentification = this.userRepository.findByIdentification(identification);

        try {
            List<UserEntity> userEntities = byIdentification.collectList().block();

            if (userEntities != null && !userEntities.isEmpty()) {
                return userEntities.get(0);
            } else {
                throw new UsernameNotFoundException(CommonsService.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new UsernameNotFoundException(CommonsService.USER_NOT_FOUND);
        }
    }
}
