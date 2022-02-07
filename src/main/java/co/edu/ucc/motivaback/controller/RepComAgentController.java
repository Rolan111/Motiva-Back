package co.edu.ucc.motivaback.controller;

import co.edu.ucc.motivaback.dto.RepComAgentDto;
import co.edu.ucc.motivaback.service.RepComAgentService;
import co.edu.ucc.motivaback.util.GeneralBodyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static co.edu.ucc.motivaback.util.CommonsService.*;

@RestController
@RequestMapping("/api")
public class RepComAgentController {

    private final RepComAgentService repComAgentService;

    public RepComAgentController(RepComAgentService repComAgentService) {
        this.repComAgentService = repComAgentService;
    }

    @GetMapping(value = "/rep-com-agents")
    public ResponseEntity<GeneralBodyResponse<List<RepComAgentDto>>> getAll() {
        try {
            List<RepComAgentDto> postDTOS = this.repComAgentService.findAll();

            if (!postDTOS.isEmpty())
                return new ResponseEntity<>(new GeneralBodyResponse<>(postDTOS, "list Rep Com Agent", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, "empty", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/rep-com-agent-create")
    public ResponseEntity<GeneralBodyResponse<RepComAgentDto>> create(@RequestBody RepComAgentDto repComAgentDto) {
        try {
            RepComAgentDto sheetDto = this.repComAgentService.create(repComAgentDto);

            if (sheetDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(sheetDto, CREATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, CREATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rep-com-agent-update")
    public ResponseEntity<GeneralBodyResponse<RepComAgentDto>> update(@Valid @RequestBody RepComAgentDto repComAgentDto) {
        try {
            var sheetDto = this.repComAgentService.update(repComAgentDto);
            if (sheetDto != null)
                return new ResponseEntity<>(new GeneralBodyResponse<>(this.repComAgentService.update(repComAgentDto), UPDATED_OK, null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(null, UPDATED_FAIL, null), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("rep-com-agent-delete/{id}")
    public ResponseEntity<GeneralBodyResponse<Boolean>> delete(@PathVariable String id) {
        try {
            if (this.repComAgentService.delete(id))
                return new ResponseEntity<>(new GeneralBodyResponse<>(true, "delete Rep Com Agent ok", null), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GeneralBodyResponse<>(false, "not delete", null), HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("rep-com-agent/{id}")
    public ResponseEntity<GeneralBodyResponse<RepComAgentDto>> findById(@PathVariable String id) {
        try {
            var repComAgentDto = this.repComAgentService.findById(id);

            return new ResponseEntity<>(new GeneralBodyResponse<>(repComAgentDto, "find rep-com-agent", null), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new GeneralBodyResponse<>(null, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }
}
