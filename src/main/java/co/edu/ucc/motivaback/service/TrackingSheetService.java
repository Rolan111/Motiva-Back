package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.TrackingSheetDto;
import co.edu.ucc.motivaback.payload.TrackingSheetForm;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TrackingSheetService {
    List<TrackingSheetDto> findAll();

    TrackingSheetDto create(TrackingSheetForm trackingSheetForm);

    TrackingSheetDto update(TrackingSheetForm trackingSheetForm);

    boolean delete(String id);

    TrackingSheetDto findById(String id) throws ExecutionException, InterruptedException;
}
