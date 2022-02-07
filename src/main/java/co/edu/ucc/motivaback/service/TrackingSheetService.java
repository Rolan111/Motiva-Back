package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.TrackingSheetDto;

import java.util.List;

public interface TrackingSheetService {

    List<TrackingSheetDto> findAll();

    TrackingSheetDto create(TrackingSheetDto trackingSheetDto);

    TrackingSheetDto update(TrackingSheetDto trackingSheetDto);

    boolean delete(String id);

    TrackingSheetDto findById(String id);
}
