package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.CityDto;
import co.edu.ucc.motivaback.payload.CityForm;

import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class CityService
 */
public interface CityService {
    List<CityDto> findAll();

    CityDto create(CityForm cityForm);

    CityDto update(CityForm cityForm);

    boolean delete(String id);

    CityDto findById(String id);
}
