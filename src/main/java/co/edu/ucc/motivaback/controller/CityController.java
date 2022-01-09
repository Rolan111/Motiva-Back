package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.dto.CityDto;
import co.edu.ucc.motivaback.payload.CityForm;
import co.edu.ucc.motivaback.service.CityService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class CityController
 */
@RestController
@RequestMapping("/api")
public class CityController {
    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping(value = "/cities")
    public ResponseEntity<GeneralBodyResponse<List<CityDto>>> getAll() {
        try {
            List<CityDto> postDTOS = this.cityService.findAll();

            if (!postDTOS.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list cities", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/city")
    public ResponseEntity<GeneralBodyResponse<CityDto>> create(@RequestBody CityForm cityForm) {
        try {
            var cityDto = this.cityService.create(cityForm);

            if (cityDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(cityDto, "created", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "not created", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/city")
    public ResponseEntity<GeneralBodyResponse<CityDto>> update(@Valid @RequestBody CityForm cityForm) {
        try {
            var cityDto = this.cityService.update(cityForm);

            return new ResponseEntity<>(new GeneralBodyResponse<>(cityDto, "update", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("city/{id}")
    public ResponseEntity<GeneralBodyResponse<Boolean>> delete(@PathVariable String id) {
        try {
            if (this.cityService.delete(id))
                return new ResponseEntity<>(new GeneralBodyResponse<>(true, "delete ok", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(false, "not delete", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("city/{id}")
    public ResponseEntity<GeneralBodyResponse<CityDto>> findById(@PathVariable String id) {
        try {
            var cityDto = this.cityService.findById(id);

            return new ResponseEntity<>(new GeneralBodyResponse<>(cityDto, "find city", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
