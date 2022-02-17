package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.TrackingSheetDto;

import java.util.List;

public interface TrackingSheetService {

    List<TrackingSheetDto> getAll();

    TrackingSheetDto save(TrackingSheetDto trackingSheetDto);
}
