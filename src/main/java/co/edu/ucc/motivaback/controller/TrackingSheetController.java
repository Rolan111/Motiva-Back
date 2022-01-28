package co.edu.ucc.motivaback.controller;


import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.payload.TrackingSheetForm;
import co.edu.ucc.motivaback.service.TrackingSheetService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list Tracking sheet", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/tracking-sheet-create")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> create(@RequestBody TrackingSheetForm trackingSheetForm) {
        try {
            var trackingSheetDto = this.trackingSheetService.create(trackingSheetForm);

            if (trackingSheetDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(trackingSheetDto, "created", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "not created", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/tracking-sheet-update")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> update(@Valid @RequestBody TrackingSheetForm trackingSheetForm) {
        try {
            var trackingSheetDto = this.trackingSheetService.update(trackingSheetForm);

            return new ResponseEntity<>(new GeneralBodyResponse<>(trackingSheetDto, "update Tracking Sheet", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("tracking-sheet-delete/{id}")
    public ResponseEntity<GeneralBodyResponse<Boolean>> delete(@PathVariable String id) {
        try {
            if (this.trackingSheetService.delete(id))
                return new ResponseEntity<>(new GeneralBodyResponse<>(true, "delete Tracking Sheet ok", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(false, "not delete", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("tracking-sheet/{id}")
    public ResponseEntity<GeneralBodyResponse<TrackingSheetDto>> findById(@PathVariable String id) {
        try {
            var trackingSheetDto = this.trackingSheetService.findById(id);

            return new ResponseEntity<>(new GeneralBodyResponse<>(trackingSheetDto, "find rep-com-agent", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
