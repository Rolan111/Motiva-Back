package co.edu.ucc.motivaback.config.security;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.enums.UserRolEnum;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SeederControl {
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SeederControl(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                new AuthenticatedUser(1L, "a@a.com"),
                "key"
        ));
    }

    @EventListener
    public void seed(ContextRefreshedEvent contextRefreshedEvent) {
        this.userSeed();
    }

    protected void userSeed() {
        UserDto userDto = new UserDto();
        userDto.setEmail("a@b.com");
        userDto.setFullName("Test test");
        userDto.setIdentification("5");
        userDto.setUserRolEnum(UserRolEnum.ADMIN);
        System.out.println("***************************************************");
        String encode = bCryptPasswordEncoder.encode("pruebas");
        System.out.println(encode);
        userDto.setPassword(encode);
        userDto.setPhone("456456");
//        userDto.setCreatedAt(new Date());
//        userDto.setCreatedBy(1L);
//        userDto.setStatus(RegisterStatusEnum.ACTIVE);

//        User newUserCreated = this.userRepository.save(userDto);
//        System.out.println(newUserCreated);
    }
}
