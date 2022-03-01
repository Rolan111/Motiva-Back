package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.entity.TrackingSheetEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TrackingSheetService {

    List<TrackingSheetDto> getAll();
    Mono<TrackingSheetEntity> getAll2();

    TrackingSheetDto save(TrackingSheetDto trackingSheetDto);
}
