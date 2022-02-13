package co.edu.ucc.motivaback.service.impl;

import co.edu.ucc.motivaback.dto.PollDto;
import co.edu.ucc.motivaback.entity.PollEntity;
import co.edu.ucc.motivaback.enums.RegisterStatusEnum;
import co.edu.ucc.motivaback.repository.PollRepository;
import co.edu.ucc.motivaback.service.PollService;
import co.edu.ucc.motivaback.util.ObjectMapperUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PollServiceImpl implements PollService {
    private final PollRepository pollRepository;

    public PollServiceImpl(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    @Override
    public Page<PollDto> findAll(Pageable pageable) {
        List<PollEntity> pollEntityList = this.pollRepository.findAllByOrderByDateDesc().collectList().block();

        if (pollEntityList != null && !pollEntityList.isEmpty())
            return gePageFromList(pageable, pollEntityList);
        else
            return Page.empty();
    }

    @Override
    public Page<PollDto> findAllByIdUser(Pageable pageable, Integer idUser) {
        List<PollEntity> pollEntityList = this.pollRepository.findAllByIdUserOrderByDateDesc(idUser).collectList().block();

        if (pollEntityList != null && !pollEntityList.isEmpty())
            return gePageFromList(pageable, pollEntityList);
        else
            return Page.empty();
    }

    @Override
    public Page<PollDto> findAllByIdUsers(Pageable pageable, List<Long> idUsers) {
        List<PollEntity> pollEntityList = new ArrayList<>();
        for (Long idUser : idUsers) {
            List<PollEntity> pollEntities = this.pollRepository.findAllByIdUserOrderByDateDesc(idUser.intValue()).collectList().block();

            if (pollEntities != null && !pollEntities.isEmpty())
                pollEntityList.addAll(pollEntities);
        }

        if (!pollEntityList.isEmpty())
            return gePageFromList(pageable, pollEntityList);
        else
            return Page.empty();
    }

    @Override
    public Page<PollDto> findAllByIdCity(Pageable pageable, Integer idCity) {
        List<PollEntity> pollEntityList = this.pollRepository.findAllByIdCityOrderByDateDesc(idCity).collectList().block();

        if (pollEntityList != null && !pollEntityList.isEmpty())
            return gePageFromList(pageable, pollEntityList);
        else
            return Page.empty();
    }

    @Override
    public Page<PollDto> findAllByIdCityAndIdUser(Pageable pageable, Integer idCity, Integer idUser) {
        List<PollEntity> pollEntityList = this.pollRepository.findAllByIdUserAndIdCityOrderByDateDesc(idUser, idCity).collectList().block();

        if (pollEntityList != null && !pollEntityList.isEmpty())
            return gePageFromList(pageable, pollEntityList);
        else
            return Page.empty();
    }

    @Override
    public PollDto save(PollDto pollDto) {
        PollEntity pollEntity = ObjectMapperUtils.map(pollDto, PollEntity.class);
        Long count = this.pollRepository.count().block();

        pollEntity.setDate(new Date());
        pollEntity.setCreatedAt(new Date());
        pollEntity.setStatus(RegisterStatusEnum.ACTIVE);
        pollEntity.setIdPoll(count != null ? count.intValue() + 1 : (int) ((Math.random() * (Integer.MAX_VALUE - 100000)) + 100000));

        PollEntity entity = this.pollRepository.save(pollEntity).block();

        if (entity != null)
            return ObjectMapperUtils.map(entity, PollDto.class);
        else
            return null;
    }

    private Page<PollDto> gePageFromList(Pageable pageable, List<PollEntity> pollEntityList) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), pollEntityList.size());
        List<PollDto> pollDtoList = ObjectMapperUtils.mapAll(pollEntityList.subList(start, end), PollDto.class);

        return new PageImpl<>(pollDtoList, pageable, pollEntityList.size());
    }

}
