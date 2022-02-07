package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.RepComAgentDto;

import java.util.List;

public interface RepComAgentService {

    List<RepComAgentDto> findAll();

    RepComAgentDto create(RepComAgentDto repComAgentDto);

    RepComAgentDto update(RepComAgentDto repComAgentDto);

    boolean delete(String id);

    RepComAgentDto findById(String id);
}
