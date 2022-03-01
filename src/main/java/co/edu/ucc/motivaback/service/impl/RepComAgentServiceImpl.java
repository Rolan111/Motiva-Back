package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.AnswerDto;
import co.edu.ucc.motivaback.dto.OptionAnswerDto;
import co.edu.ucc.motivaback.dto.RepComAgentDto;
import co.edu.ucc.motivaback.entity.AnswerEntity;
import co.edu.ucc.motivaback.entity.OptionAnswerEntity;
import co.edu.ucc.motivaback.entity.RepComAgentEntity;
import co.edu.ucc.motivaback.repository.RepComAgentRepository;
import co.edu.ucc.motivaback.service.RepComAgentService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class RepComAgentServiceImpl implements RepComAgentService {

    private final RepComAgentRepository repComAgentRepository;

    public RepComAgentServiceImpl(RepComAgentRepository repComAgentRepository) {
        this.repComAgentRepository = repComAgentRepository;
    }

    @Override
    public List<RepComAgentDto> getAll() {
        List<RepComAgentEntity> repComAgentEntities = this.repComAgentRepository.findAll().collectList().block();
        if (repComAgentEntities != null && !repComAgentEntities.isEmpty()) {
            return ObjectMapperUtils.mapAll(repComAgentEntities, RepComAgentDto.class);
        } else {
            return Collections.emptyList();
        }
    }

    /*
    @Override
    public List<RepComAgentDto> getAll2() {
        List<RepComAgentEntity> repComAgentEntities = this.repComAgentRepository.finAll2().collectList().block();
        if (repComAgentEntities != null && !repComAgentEntities.isEmpty()) {
            return ObjectMapperUtils.mapAll(repComAgentEntities, RepComAgentDto.class);
        } else {
            return Collections.emptyList();
        }
    }*/

    @Override
    public RepComAgentDto save(RepComAgentDto repComAgentDto) {
        RepComAgentEntity repComAgentEntity = ObjectMapperUtils.map(repComAgentDto, RepComAgentEntity.class);
        Long count = this.repComAgentRepository.count().block();

        repComAgentEntity.setCreatedAt(new Date());

        RepComAgentEntity entity = this.repComAgentRepository.save(repComAgentEntity).block();

        if (entity != null)
            return ObjectMapperUtils.map(entity, RepComAgentDto.class);
        else
            return null;
    }

}
