package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.entity.TrackingSheetEntity;
import co.edu.ucc.motivaback.repository.TrackingSheetRepository;
import co.edu.ucc.motivaback.service.TrackingSheetService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TrackingSheetServiceImpl implements TrackingSheetService {

    private final TrackingSheetRepository trackingSheetRepository;

    public TrackingSheetServiceImpl(TrackingSheetRepository trackingSheetRepository) {
        this.trackingSheetRepository = trackingSheetRepository;
    }

    @Override
    public List<TrackingSheetDto> getAll() {
        List<TrackingSheetEntity> trackingSheetEntities = this.trackingSheetRepository.findAll().collectList().block();
        if (trackingSheetEntities != null && !trackingSheetEntities.isEmpty()) {
            return ObjectMapperUtils.mapAll(trackingSheetEntities, TrackingSheetDto.class);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Mono<TrackingSheetEntity> getAll2() {
        /*if (trackingSheetEntities != null) {
            return trackingSheetEntities;
        } else {
            return null;
        }*/
        //return "hola mundo";
        return this.trackingSheetRepository.findById("");
    }

    @Override
    public TrackingSheetDto save(TrackingSheetDto trackingSheetDto) {
        TrackingSheetEntity trackingSheetEntity = ObjectMapperUtils.map(trackingSheetDto, TrackingSheetEntity.class);

        trackingSheetEntity.setCreatedAt(new Date());

        TrackingSheetEntity entity = this.trackingSheetRepository.save(trackingSheetEntity).block();

        if (entity != null)
            return ObjectMapperUtils.map(entity, TrackingSheetDto.class);
        else
            return null;
    }

}
