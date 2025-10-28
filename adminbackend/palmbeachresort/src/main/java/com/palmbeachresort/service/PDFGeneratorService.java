package com.palmbeachresort.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.palmbeachresort.model.Order;
import com.palmbeachresort.model.OrderItem;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Service
public class PDFGeneratorService {

    public ByteArrayInputStream generateOrderBill(Order order) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
            Paragraph title = new Paragraph("Palm Beach Resort", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Subtitle
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 18, BaseColor.DARK_GRAY);
            Paragraph subtitle = new Paragraph("Food Order Receipt", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(30);
            document.add(subtitle);

            // Order details
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            // Customer info
            PdfPTable customerTable = new PdfPTable(2);
            customerTable.setWidthPercentage(100);
            customerTable.setSpacingAfter(20);

            addTableHeader(customerTable, "Customer Information", headerFont, 2);
            addTableCell(customerTable, "Order Number:", order.getOrderNumber(), headerFont, normalFont);
            addTableCell(customerTable, "Customer:", order.getCustomerName(), headerFont, normalFont);
            addTableCell(customerTable, "Email:", order.getCustomerEmail(), headerFont, normalFont);
            addTableCell(customerTable, "Phone:", order.getCustomerPhone(), headerFont, normalFont);
            addTableCell(customerTable, "Order Date:",
                    order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                    headerFont, normalFont);
            addTableCell(customerTable, "Status:", order.getStatus(), headerFont, normalFont);

            document.add(customerTable);

            // Order items
            PdfPTable itemsTable = new PdfPTable(4);
            itemsTable.setWidthPercentage(100);
            itemsTable.setSpacingAfter(20);

            // Table headers
            String[] itemHeaders = {"Item", "Quantity", "Price", "Total"};
            for (String header : itemHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setPadding(8);
                itemsTable.addCell(cell);
            }

            // Table rows
            for (OrderItem item : order.getItems()) {
                itemsTable.addCell(new Phrase(item.getMenuName(), normalFont));
                itemsTable.addCell(new Phrase(item.getQuantity().toString(), normalFont));
                itemsTable.addCell(new Phrase("$" + item.getPrice(), normalFont));
                BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                itemsTable.addCell(new Phrase("$" + itemTotal, normalFont));
            }

            document.add(itemsTable);

            // Totals
            PdfPTable totalsTable = new PdfPTable(2);
            totalsTable.setWidthPercentage(50);
            totalsTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            addTableCell(totalsTable, "Subtotal:", "$" + order.getSubtotal(), headerFont, normalFont);
            addTableCell(totalsTable, "Tax (8%):", "$" + order.getTax(), headerFont, normalFont);
            addTableCell(totalsTable, "Total:", "$" + order.getTotalAmount(), headerFont, normalFont);

            document.add(totalsTable);

            // Thank you message
            Paragraph thankYou = new Paragraph("\n\nThank you for dining with us!", normalFont);
            thankYou.setAlignment(Element.ALIGN_CENTER);
            document.add(thankYou);

            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addTableHeader(PdfPTable table, String header, Font font, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setColspan(colspan);
        cell.setBackgroundColor(new BaseColor(28, 161, 166)); // Teal color
        cell.setPadding(10);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private void addTableCell(PdfPTable table, String label, String value, Font headerFont, Font normalFont) {
        table.addCell(new Phrase(label, headerFont));
        table.addCell(new Phrase(value, normalFont));
    }
}