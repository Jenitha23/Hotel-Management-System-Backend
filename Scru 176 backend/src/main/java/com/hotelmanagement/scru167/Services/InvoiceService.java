package com.hotelmanagement.scru167.Services;

import com.hotelmanagement.scru167.domain.*;
import com.hotelmanagement.scru167.dto.*;
import com.hotelmanagement.scru167.Repositories.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Service
public class InvoiceService {
    private final BookingRepository bookingRepo;
    private final OrderItemRepository itemRepo;

    // Configure (you can move to application.yml if you like)
    private static final BigDecimal TAX_RATE = new BigDecimal("0.125"); // 12.5%
    private static final BigDecimal SERVICE_RATE = new BigDecimal("0.00"); // change if needed

    public InvoiceService(BookingRepository b, OrderItemRepository i) {
        this.bookingRepo = b; this.itemRepo = i;
    }

    public InvoiceResponseDTO buildInvoice(Long bookingId) {
        var booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking not found: " + bookingId));
        var items = itemRepo.findByBooking(booking);

        int nights = (int) ChronoUnit.DAYS.between(booking.getCheckIn(), booking.getCheckOut());
        var lines = new ArrayList<InvoiceLineDTO>();

        // Room charge line
        var roomRate = booking.getRoom().getDailyRate();
        var roomTotal = roomRate.multiply(BigDecimal.valueOf(nights));
        lines.add(new InvoiceLineDTO(
                "Room", "Room " + booking.getRoom().getRoomNumber() + " (" + nights + " nights)",
                nights, roomRate, roomTotal));

        // Orders lines
        for (var it : items) {
            var lineTotal = it.getUnitPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
            var cat = it.getType().name().replace('_',' ');
            lines.add(new InvoiceLineDTO(cat, it.getDescription(), it.getQuantity(), it.getUnitPrice(), lineTotal));
        }

        // Totals
        var subTotal = lines.stream()
                .map(InvoiceLineDTO::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var tax = subTotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        var service = subTotal.multiply(SERVICE_RATE).setScale(2, RoundingMode.HALF_UP);
        var grand = subTotal.add(tax).add(service).setScale(2, RoundingMode.HALF_UP);

        return new InvoiceResponseDTO(
                booking.getId(),
                booking.getCustomer().getFullName(),
                booking.getRoom().getRoomNumber(),
                booking.getCheckIn(),
                booking.getCheckOut(),
                nights,
                lines,
                subTotal.setScale(2, RoundingMode.HALF_UP),
                tax,
                service,
                grand
        );
    }

    public byte[] buildPdf(Long bookingId) {
        var dto = buildInvoice(bookingId);
        try (var out = new ByteArrayOutputStream()) {
            var doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, out);
            doc.open();

            var title = new Paragraph("Hotel Invoice", new Font(Font.HELVETICA, 18, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Booking ID: " + dto.bookingId()));
            doc.add(new Paragraph("Guest: " + dto.guestName()));
            doc.add(new Paragraph("Room: " + dto.roomNumber()));
            doc.add(new Paragraph("Stay: " + dto.checkIn() + " to " + dto.checkOut() + " (" + dto.nights() + " nights)"));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{20, 40, 10, 15, 15});
            addHeader(table, "Category","Description","Qty","Unit","Total");
            for (var l : dto.lines()) {
                table.addCell(l.category());
                table.addCell(l.description());
                table.addCell(String.valueOf(l.quantity()));
                table.addCell(l.unitPrice().toString());
                table.addCell(l.lineTotal().toString());
            }
            doc.add(table);
            doc.add(new Paragraph(" "));

            var totals = new Paragraph(
                    "Subtotal: " + dto.subTotal() +
                            "\nTax: " + dto.tax() +
                            "\nService: " + dto.serviceCharge() +
                            "\nGrand Total: " + dto.grandTotal(),
                    new Font(Font.HELVETICA, 12, Font.BOLD));
            doc.add(totals);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create invoice PDF", e);
        }
    }

    private void addHeader(PdfPTable t, String... cols) {
        for (var c : cols) {
            PdfPCell cell = new PdfPCell(new Paragraph(c, new Font(Font.HELVETICA, 12, Font.BOLD)));
            t.addCell(cell);
        }
    }
}
