package co.edu.ucc.motivaback.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@EnableTransactionManagement
public class ApiSecurityConf extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
    private final KeyPairComponent keyPairComponent;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public ApiSecurityConf(KeyPairComponent keyPairComponent) {
        this.keyPairComponent = keyPairComponent;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "HEAD", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
//                .addFilter(new AuthenticationFilter(authenticationManager(), this.keyPairComponent.getKeysPair(), "/auth"))
//                .addFilter(new AuthorizationFilter(authenticationManager(), this.keyPairComponent.getKeysPair()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
