package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.PollDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PollService {
    Page<PollDto> findAll(Pageable pageable);

    Page<PollDto> findAllByIdCity(Pageable pageable, Integer idCity);

    Page<PollDto> findAllByIdCityAndIdUser(Pageable pageable, Integer idCity, Integer idUser);

    Page<PollDto> findAllByIdUsers(Pageable pageable, List<Long> idUsers);

    Page<PollDto> findAllByIdUser(Pageable pageable, Integer idUser);

    PollDto save(PollDto pollDto);
}
