package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.dto.UserDto;
import co.edu.ucc.motivaback.payload.UserForm;
import co.edu.ucc.motivaback.service.UserService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class UserController
 */

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public ResponseEntity<GeneralBodyResponse<List<UserDto>>> getAll() {
        try {
            List<UserDto> postDTOS = this.userService.findAll();

            if (!postDTOS.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list users", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/user")
    public ResponseEntity<GeneralBodyResponse<UserDto>> create(@RequestBody UserForm userForm) {
        try {
            UserDto userDto = this.userService.create(userForm);

            if (userDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(userDto, "created", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "not created", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user")
    public ResponseEntity<GeneralBodyResponse<UserDto>> update(@Valid @RequestBody UserForm userForm) {
        try {
            UserDto userDto = this.userService.update(userForm);

            return new ResponseEntity<>(new GeneralBodyResponse<>(userDto, "update", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("user/{id}")
    public ResponseEntity<GeneralBodyResponse<Boolean>> delete(@PathVariable String id) {
        try {
            if (this.userService.delete(id))
                return new ResponseEntity<>(new GeneralBodyResponse<>(true, "delete ok", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(false, "not delete", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("user/{id}")
    public ResponseEntity<GeneralBodyResponse<UserDto>> findById(@PathVariable String id) {
        try {
            UserDto userDto = this.userService.findById(id);

            return new ResponseEntity<>(new GeneralBodyResponse<>(userDto, "find user", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
