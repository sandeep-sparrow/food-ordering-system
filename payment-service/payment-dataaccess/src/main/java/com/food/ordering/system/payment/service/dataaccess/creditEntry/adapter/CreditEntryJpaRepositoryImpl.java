package com.food.ordering.system.payment.service.dataaccess.creditEntry.adapter;

import com.food.ordering.system.domain.valueObject.CustomerId;
import com.food.ordering.system.payment.service.dataaccess.creditEntry.mapper.CreditEntryDataAccessMapper;
import com.food.ordering.system.payment.service.dataaccess.creditEntry.repository.CreditEntryJpaRepository;
import com.food.ordering.system.payment.service.domain.entity.CreditEntry;
import com.food.ordering.system.payment.service.domain.ports.output.respository.CreditEntryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreditEntryJpaRepositoryImpl implements CreditEntryRepository {


    private final CreditEntryJpaRepository creditEntryJpaRepository;
    private final CreditEntryDataAccessMapper creditEntryDataAccessMapper;

    public CreditEntryJpaRepositoryImpl(CreditEntryJpaRepository creditEntryJpaRepository,
                                        CreditEntryDataAccessMapper creditEntryDataAccessMapper) {
        this.creditEntryJpaRepository = creditEntryJpaRepository;
        this.creditEntryDataAccessMapper = creditEntryDataAccessMapper;
    }

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        return creditEntryDataAccessMapper.creditEntryEntityToCreditEntry(
                creditEntryJpaRepository.save(creditEntryDataAccessMapper.creditEntryToCreditEntryEntity(creditEntry))
        );
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return creditEntryJpaRepository
                .findByCustomerId(customerId.getValue())
                .map(creditEntryDataAccessMapper::creditEntryEntityToCreditEntry);
    }
}