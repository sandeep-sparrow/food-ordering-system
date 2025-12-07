package com.food.ordering.system.order.service.domain.entities;

import com.food.ordering.system.domain.entities.AggregateRoot;
import com.food.ordering.system.domain.valueobject.CustomerId;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Customer extends AggregateRoot<CustomerId> {

    public Customer(CustomerId customerId) {
        super.setId(customerId);
    }
}
