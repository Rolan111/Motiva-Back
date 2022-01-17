package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.RepComAgentDto;
import co.edu.ucc.motivaback.payload.RepComAgentForm;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RepComAgentService {
    List<RepComAgentDto> findAll();

    RepComAgentDto create(RepComAgentForm repComAgentForm);

    RepComAgentDto update(RepComAgentForm repComAgentForm);

    boolean delete(String id);

    RepComAgentDto findById(String id) throws ExecutionException, InterruptedException;
}
