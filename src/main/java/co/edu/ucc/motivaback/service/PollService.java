package co.edu.ucc.motivaback.service;

import co.edu.ucc.motivaback.dto.PollDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PollService {
    Page<PollDto> findAll(Pageable pageable);

    Page<PollDto> findAll(Pageable pageable, Integer idUser);
}
