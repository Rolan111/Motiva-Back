package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.QuestionDto;

import java.util.List;

public interface QuestionService {
    List<QuestionDto> findAllQuestionary(String type);
}
