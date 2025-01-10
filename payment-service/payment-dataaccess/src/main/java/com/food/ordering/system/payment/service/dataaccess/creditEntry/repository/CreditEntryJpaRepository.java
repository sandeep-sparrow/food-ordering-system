package com.food.ordering.system.payment.service.dataaccess.creditEntry.repository;

import com.food.ordering.system.payment.service.dataaccess.creditEntry.entity.CreditEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, UUID> {
    Optional<CreditEntryEntity> findByCustomerId(UUID customerId);
}
