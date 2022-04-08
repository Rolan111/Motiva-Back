package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.AlertDto;
import co.edu.ucc.motivaback.entity.AlertEntity;
import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.repository.AlertRepository;
import co.edu.ucc.motivaback.service.AlertService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertServiceImpl
 */
@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<AlertDto> findAll() {
        return null;
    }

    @Override
    public AlertDto create(AlertDto alertDto) {
        AlertEntity alertEntity = ObjectMapperUtils.map(alertDto, AlertEntity.class);

        alertEntity.setCreatedAt(new Date());
        alertEntity.setStatus(RegisterStatusEnum.ACTIVE);

        AlertEntity entity = this.alertRepository.save(alertEntity).block();

        if (entity != null)
            return ObjectMapperUtils.map(entity, AlertDto.class);
        else
            return null;
    }
}
