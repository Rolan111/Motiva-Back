package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.AlertDto;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertService
 */
public interface AlertService {
    List<AlertDto> findAll();

    AlertDto create(AlertDto alertDto);
}
