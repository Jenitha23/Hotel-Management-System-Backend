// [file name]: CustomerBillingController.java
package com.palmbeachresort.controller.billing;

import com.palmbeachresort.dto.billing.BillingResponse;
import com.palmbeachresort.service.billing.BillingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/billing")
@CrossOrigin(origins = {"https://frontend-palmbeachresort.vercel.app", "http://localhost:3000"},
        allowCredentials = "true")
public class CustomerBillingController {

    @Autowired
    private BillingService billingService;

    /**
     * Get current bill for customer (active booking + all food orders)
     * GET /api/customer/billing/current
     */
    @GetMapping("/current")
    public ResponseEntity<BillingResponse> getCurrentBill(HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            BillingResponse bill = billingService.getCustomerBill(customerId);
            return ResponseEntity.ok(bill);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get bill for specific booking/stay
     * GET /api/customer/billing/booking/{bookingReference}
     */
    @GetMapping("/booking/{bookingReference}")
    public ResponseEntity<BillingResponse> getBillForStay(@PathVariable String bookingReference,
                                                          HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            BillingResponse bill = billingService.getCustomerBillForStay(customerId, bookingReference);
            return ResponseEntity.ok(bill);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Download bill as PDF (placeholder for future implementation)
     * GET /api/customer/billing/download/{bookingReference}
     */
    @GetMapping("/download/{bookingReference}")
    public ResponseEntity<String> downloadBill(@PathVariable String bookingReference,
                                               HttpSession session) {
        Long customerId = (Long) session.getAttribute("userId");
        if (customerId == null) {
            return ResponseEntity.status(401).build();
        }

        // This would generate and return a PDF in production
        return ResponseEntity.ok("PDF download endpoint - To be implemented");
    }
}