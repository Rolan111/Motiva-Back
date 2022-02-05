package co.edu.ucc.motivaback.controller;


import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.service.TrackingSheetService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@RestController
@RequestMapping("/api")
public class TrackingSheetController {
    private final TrackingSheetService trackingSheetService;

    public TrackingSheetController(TrackingSheetService trackingSheetService) {
        this.trackingSheetService = trackingSheetService;
    }

    @GetMapping(value = "/tracking-sheets")
    public ResponseEntity<GeneralBodyResponse<List<TrackingSheetDto>>> getAll() {
        try {
            List<TrackingSheetDto> postDTOS = this.trackingSheetService.findAll();

            if (!postDTOS.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, LIST_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, EMPTY_LIST, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/tracking-sheet-create")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> create(@RequestBody TrackingSheetDto trackingSheetDto) {
        try {
            TrackingSheetDto sheetDto = this.trackingSheetService.create(trackingSheetDto);

            if (sheetDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(sheetDto, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/tracking-sheet-update")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> update(@Valid @RequestBody TrackingSheetDto trackingSheetDto) {
        try {
            var sheetDto = this.trackingSheetService.update(trackingSheetDto);
            if (sheetDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(this.trackingSheetService.update(trackingSheetDto), UPDATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, UPDATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("tracking-sheet-delete/{id}")
    public ResponseEntity<GeneralBodyResponse<Boolean>> delete(@PathVariable String id) {
        try {
            if (this.trackingSheetService.delete(id))
                return new ResponseEntity<>(new GeneralBodyResponse<>(true, DELETED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(false, DELETED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("tracking-sheet/{id}")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> findById(@PathVariable String id) {
        var byId = this.trackingSheetService.findById(id);
        if (byId != null)
            return new ResponseEntity<>(new GeneralBodyResponse<>(byId, FOUND_OBJECT, null), HttpStatus.OK);
        else
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, NOT_FOUND_OBJECT, null), HttpStatus.BAD_REQUEST);
    }
}
