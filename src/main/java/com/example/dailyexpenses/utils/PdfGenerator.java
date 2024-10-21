package com.example.dailyexpenses.utils;

import com.example.dailyexpenses.model.Expense;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

public class PdfGenerator {

    private static final Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font bodyFont = new Font(Font.FontFamily.HELVETICA, 12);

    public static ByteArrayOutputStream createUserBalanceSheetPdf(List<Expense> expenses, Long userId) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Paragraph title = new Paragraph("Balance Sheet of User ID: " + userId, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = createExpenseTable(expenses);
        document.add(table);

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        document.add(new Paragraph("Total Amount: " + totalAmount.toString(), bodyFont));

        document.close();
        return outputStream;
    }

    public static ByteArrayOutputStream createAllUsersBalanceSheetPdf(List<Expense> expenses) throws DocumentException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        Paragraph title = new Paragraph("Balance Sheet of All Users", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        PdfPTable table = createExpenseTable(expenses);
        document.add(table);

        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        document.add(new Paragraph("Total Amount: " + totalAmount.toString(), bodyFont));

        document.close();
        return outputStream;
    }

    private static PdfPTable createExpenseTable(List<Expense> expenses) {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        try {
            table.setWidths(new float[]{2f, 4f, 2f, 3f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        addTableHeader(table);

        for (Expense expense : expenses) {
            addExpenseRow(table, expense);
        }

        return table;
    }

    private static void addTableHeader(PdfPTable table) {
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("User ID", headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Description", headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Amount", headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Date", headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }

    private static void addExpenseRow(PdfPTable table, Expense expense) {
        PdfPCell cell;
    
        cell = new PdfPCell(new Paragraph(String.valueOf(expense.getUser().getId()), bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    
        cell = new PdfPCell(new Paragraph(expense.getDescription(), bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    
        cell = new PdfPCell(new Paragraph(expense.getAmount().toString(), bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        String createdAtFormatted = expense.getCreatedAt() != null ? expense.getCreatedAt().toString() : "N/A";
        cell = new PdfPCell(new Paragraph(createdAtFormatted, bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
    
}
