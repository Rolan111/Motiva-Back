package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.CityDto;
import co.edu.ucc.motivaback.entity.CityEntity;
import co.edu.ucc.motivaback.repository.CityRepository;
import co.edu.ucc.motivaback.service.CityService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<CityDto> getAll() {
        List<CityEntity> cityEntities = this.cityRepository.findAll().collectList().block();
        if (cityEntities != null && !cityEntities.isEmpty()) {
            return ObjectMapperUtils.mapAll(cityEntities, CityDto.class);
        } else {
            return Collections.emptyList();
        }
    }
}
