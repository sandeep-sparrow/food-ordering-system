package com.food.ordering.system.order.service.domain.ports.output.message.publisher.restaurantapproval;

import com.food.ordering.system.domain.events.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidRestaurantApprovalRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
