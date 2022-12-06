package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.RasmDto;
import co.edu.ucc.motivaback.entity.RASMEntity;
import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.repository.RASMRepository;
import co.edu.ucc.motivaback.service.RASMService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author nagredo
 * @project motiva-back
 * @class AlertServiceImpl
 */
@Service
public class RASMServiceImpl implements RASMService {

    private final RASMRepository rasmRepository;

    public RASMServiceImpl(RASMRepository rasmRepository) {
        this.rasmRepository = rasmRepository;
    }

    @Override
    public RasmDto create(RasmDto rasmDto) {

        RASMEntity rasmEntity = ObjectMapperUtils.map(rasmDto, RASMEntity.class);

        rasmEntity.setCreatedAt(new Date());
        rasmEntity.setStatus(RegisterStatusEnum.ACTIVE);

        RASMEntity entity = this.rasmRepository.save(rasmEntity).block();

        if (entity != null)
            return ObjectMapperUtils.map(entity, RasmDto.class);
        else
            return null;
    }


}
