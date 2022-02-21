package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.CareSheetDto;
import co.edu.ucc.motivaback.entity.CareSheetEntity;
import co.edu.ucc.motivaback.repository.CareSheetRepository;
import co.edu.ucc.motivaback.service.CareSheetService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class CareSheetServiceImpl implements CareSheetService {

    private final CareSheetRepository careSheetRepository;

    public CareSheetServiceImpl(CareSheetRepository careSheetRepository) {
        this.careSheetRepository = careSheetRepository;
    }

    @Override
    public List<CareSheetDto> getAll() {
        List<CareSheetEntity> careSheetEntities = this.careSheetRepository.findAll().collectList().block();
        if (careSheetEntities != null && !careSheetEntities.isEmpty()) {
            return ObjectMapperUtils.mapAll(careSheetEntities, CareSheetDto.class);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public CareSheetDto save(CareSheetDto careSheetDto) {
        CareSheetEntity careSheetEntity = ObjectMapperUtils.map(careSheetDto, CareSheetEntity.class);
        Long count = this.careSheetRepository.count().block();

        careSheetEntity.setCreatedAt(new Date());

        CareSheetEntity entity = this.careSheetRepository.save(careSheetEntity).block();

        if (entity != null)
            return ObjectMapperUtils.map(entity, CareSheetDto.class);
        else
            return null;
    }
}
