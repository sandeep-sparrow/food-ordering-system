package com.food.ordering.system.payment.service.domain.entities;

import com.food.ordering.system.domain.entities.BaseEntity;
import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.payment.service.domain.valueobject.CreditEntryId;
import lombok.Getter;

@Getter
public class CreditEntry extends BaseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;

    public void addCreditAmount(Money creditAmount) {
        this.totalCreditAmount = this.totalCreditAmount.add(creditAmount);
    }

    public void subtractCreditAmount(Money creditAmount) {
        this.totalCreditAmount = this.totalCreditAmount.subtract(creditAmount);
    }

    private CreditEntry(Builder builder) {
        setId(builder.creditEntryId);
        customerId = builder.customerId;
        totalCreditAmount = builder.totalCreditAmount;
    }

    public static final class Builder {
        private CreditEntryId creditEntryId;
        private CustomerId customerId;
        private Money totalCreditAmount;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder creditEntryId(CreditEntryId val) {
            creditEntryId = val;
            return this;
        }

        public Builder customerId(CustomerId val) {
            customerId = val;
            return this;
        }

        public Builder totalCreditAmount(Money val) {
            totalCreditAmount = val;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(this);
        }
    }
}
