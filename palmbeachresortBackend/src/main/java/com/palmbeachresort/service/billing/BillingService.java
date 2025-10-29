package com.palmbeachresort.service.billing;

import com.palmbeachresort.dto.billing.BillingResponse;

public interface BillingService {
    BillingResponse getCustomerBill(Long customerId);
    BillingResponse getCustomerBillForStay(Long customerId, String bookingReference);
}