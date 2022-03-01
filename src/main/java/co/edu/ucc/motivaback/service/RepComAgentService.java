package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.AnswerDto;
import co.edu.ucc.motivaback.dto.RepComAgentDto;

import java.util.List;

public interface RepComAgentService {
    List<RepComAgentDto> getAll();

    RepComAgentDto save(RepComAgentDto repComAgentDto);

    //List<RepComAgentDto> getAll2();
}
