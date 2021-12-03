package co.edu.ucc.motivaback.config.security.filter;

import co.edu.ucc.motivaback.config.security.GeneratedAccessToken;
import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.enums.TokenTypeEnum;
import co.edu.ucc.motivaback.payload.LoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private KeyPair keyPair;
    private LoginForm loginForm;
    private Authentication authentication;

    public AuthenticationFilter(
            AuthenticationManager authenticationManager,
            KeyPair keyPair,
            String loginUrlPattern
    ) {
        this.authenticationManager = authenticationManager;
        this.keyPair = keyPair;
        super.setFilterProcessesUrl(loginUrlPattern);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        try {
            loginForm = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginForm.class);
            UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    loginForm.getUsername(),
                    loginForm.getPassword(),
                    new ArrayList<>());
            return authenticationManager.authenticate(passwordAuthenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (AuthenticationException aue) {
            logger.error(aue.getMessage());
            throw new BadCredentialsException(aue.getMessage(), aue);
        }
    }

    /**
     * Create a token in case of success attempt
     *
     * @param req
     * @param res
     * @param chain
     * @param auth
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
        try {
            authentication = auth;
            UserDto userDto = (UserDto) authentication.getPrincipal();
            Date expiration = new Date(System.currentTimeMillis() + 3600000);//todo determine value
            String token = Jwts.builder()
                    .setIssuer("key_maker")
                    .setSubject(UUID.randomUUID().toString())
                    .setIssuedAt(new Date())
                    .setExpiration(expiration)
                    .setAudience(req.getRemoteAddr())
                    .setId(UUID.randomUUID().toString())
                    .claim("uid", userDto.getId())
                    .claim("username", userDto.getEmail())
                    .claim("fullname", userDto.getFullName())
                    .claim("rol", userDto.getUserRolEnum().name())
                    .signWith(this.keyPair.getPrivate(), SignatureAlgorithm.RS256)
                    .compact();
            GeneratedAccessToken generatedAccessToken = new GeneratedAccessToken(token, TokenTypeEnum.BEARER, expiration,
                    userDto.getUsername(), userDto.getFullName(), userDto.getUserRolEnum().name());

            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(new ObjectMapper().writeValueAsString(generatedAccessToken));
        } catch (IOException e) {
            logger.error(e);
            res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        if (failed != null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        } else {
            response.setStatus(500);
        }
    }
}
