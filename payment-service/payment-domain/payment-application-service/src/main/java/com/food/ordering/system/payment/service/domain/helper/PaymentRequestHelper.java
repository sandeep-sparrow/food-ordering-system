package com.food.ordering.system.payment.service.domain.helper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.payment.service.domain.PaymentDomainService;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.entities.CreditEntry;
import com.food.ordering.system.payment.service.domain.entities.CreditHistory;
import com.food.ordering.system.payment.service.domain.entities.Payment;
import com.food.ordering.system.payment.service.domain.event.PaymentEvent;
import com.food.ordering.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordering.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCancelledMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentCompletedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.message.publisher.PaymentFailedMessagePublisher;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordering.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

@Slf4j
@Component
@AllArgsConstructor
public class PaymentRequestHelper {

    private final PaymentDomainService paymentDomainService;
    private final PaymentDataMapper paymentDataMapper;
    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedMessagePublisher paymentCompletedEventDomainEventMessagePublisher;
    private final PaymentCancelledMessagePublisher paymentCancelledEventDomainEventPublisher;
    private final PaymentFailedMessagePublisher paymentFailedEventDomainEventPublisher;

    @Transactional
    public PaymentEvent persistPayment(PaymentRequest paymentRequest) {
        log.info("Received complete payment event for order id: {}", paymentRequest.getOrderId());
        Payment payment = paymentDataMapper.paymentRequestModelToPayment(paymentRequest);
        return processPayment(payment,
                (p, ctx) -> paymentDomainService.validateAndInitiatePayment(
                        p,
                        ctx.creditEntry(),
                        ctx.creditHistory(),
                        ctx.failureMessages(),
                        paymentCompletedEventDomainEventMessagePublisher,
                        paymentFailedEventDomainEventPublisher));
    }

    @Transactional
    public PaymentEvent persistCancelPayment(PaymentRequest paymentRequest) {
        log.info("Received cancel payment event for order id: {}", paymentRequest.getOrderId());
        Optional<Payment> paymentResponse = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()));
        if (paymentResponse.isEmpty()) {
            log.error("Payment not found for order id: {}", paymentRequest.getOrderId());
            throw new PaymentApplicationServiceException("Payment not found for order id: " + paymentRequest.getOrderId());
        }
        return processPayment(paymentResponse.get(),
                (p, ctx) -> paymentDomainService.validateAndCancelPayment(
                        p,
                        ctx.creditEntry(),
                        ctx.creditHistory(),
                        ctx.failureMessages(),
                        paymentCancelledEventDomainEventPublisher,
                        paymentFailedEventDomainEventPublisher));
    }

    private List<CreditHistory> getCreditHistory(CustomerId customerId) {
        Optional<List<CreditHistory>> creditHistoryOptional = creditHistoryRepository.findByCustomerId(customerId);
        if (creditHistoryOptional.isEmpty()) {
            log.error("No credit history found for customerId: {}", customerId);
            throw new PaymentApplicationServiceException("No credit history found for customerId: " + customerId);
        }
        return creditHistoryOptional.get();
    }

    private CreditEntry getCreditEntry(CustomerId customerId) {
        Optional<CreditEntry> creditEntryOptional = creditEntryRepository.findByCustomerId(customerId);
        if (creditEntryOptional.isEmpty()) {
            log.error("Credit entry not found for order id: {}", customerId.getValue());
            throw new PaymentApplicationServiceException("Credit entry not found for order id: " + customerId.getValue());
        }
        return creditEntryOptional.get();
    }

    private void persistDbObjects(Payment payment,
                                  List<String> failureMessages,
                                  CreditEntry creditEntry,
                                  List<CreditHistory> creditHistory) {
        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistory.get(creditHistory.size()-1));
        }
    }

    private record ProcessingContext(
            CreditEntry creditEntry,
            List<CreditHistory> creditHistory,
            List<String> failureMessages
    ) {}

    private PaymentEvent processPayment(
            Payment payment,
            BiFunction<Payment, ProcessingContext, PaymentEvent> validator
    ) {
        CreditEntry creditEntry = getCreditEntry(payment.getCustomerId());
        List<CreditHistory> creditHistory = getCreditHistory(payment.getCustomerId());
        List<String> failureMessages = new ArrayList<>();

        ProcessingContext ctx = new ProcessingContext(creditEntry, creditHistory, failureMessages);

        PaymentEvent event = validator.apply(payment, ctx);

        persistDbObjects(payment, failureMessages, creditEntry, creditHistory);

        return event;
    }

}
