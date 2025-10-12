package com.hotelmanagement.scru167.controller;


import com.hotelmanagement.scru167.dto.InvoiceResponseDTO;
import com.hotelmanagement.scru167.Services.InvoiceService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService s) { this.invoiceService = s; }

    /** JSON invoice (for UI preview) */
    @GetMapping("/{bookingId}")
    public ResponseEntity<InvoiceResponseDTO> get(@PathVariable Long bookingId) {
        return ResponseEntity.ok(invoiceService.buildInvoice(bookingId));
    }

    /** Downloadable PDF */
    @GetMapping("/{bookingId}/pdf")
    public ResponseEntity<byte[]> pdf(@PathVariable Long bookingId) {
        byte[] pdf = invoiceService.buildPdf(bookingId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + bookingId + ".pdf")
                .body(pdf);
    }
}
