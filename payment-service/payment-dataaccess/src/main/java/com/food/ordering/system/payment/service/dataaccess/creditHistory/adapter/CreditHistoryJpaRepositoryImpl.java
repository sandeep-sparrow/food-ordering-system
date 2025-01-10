package com.food.ordering.system.payment.service.dataaccess.creditHistory.adapter;

import com.food.ordering.system.domain.valueObject.CustomerId;
import com.food.ordering.system.payment.service.dataaccess.creditHistory.entity.CreditHistoryEntity;
import com.food.ordering.system.payment.service.dataaccess.creditHistory.mapper.CreditHistoryDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.creditHistory.repository.CreditHistoryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.CreditHistory;
import com.food.ordering.system.payment.service.domain.ports.output.respository.CreditHistoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CreditHistoryJpaRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final CreditHistoryDataAccessMapper creditHistoryDataAccessMapper;

    public CreditHistoryJpaRepositoryImpl(CreditHistoryJpaRepository creditHistoryJpaRepository,
                                          CreditHistoryDataAccessMapper creditHistoryDataAccessMapper) {
        this.creditHistoryJpaRepository = creditHistoryJpaRepository;
        this.creditHistoryDataAccessMapper = creditHistoryDataAccessMapper;
    }

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return creditHistoryDataAccessMapper.creditHistoryEntityToCreditHistory(creditHistoryJpaRepository
                .save(creditHistoryDataAccessMapper.creditHistoryToCreditHistoryEntity(creditHistory)));
    }

    @Override
    public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
        Optional<List<CreditHistoryEntity>> creditHistory =
                creditHistoryJpaRepository.findByCustomerId(customerId.getValue());

        return creditHistory
                .map(creditHistoryList ->
                    creditHistoryList.stream()
                            .map(creditHistoryDataAccessMapper::creditHistoryEntityToCreditHistory)
                            .collect(Collectors.toList()));
    }
}