package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.CareSheetDto;

import java.util.List;

public interface CareSheetService {

    List<CareSheetDto> getAll();

    CareSheetDto save(CareSheetDto careSheetDto);
}
