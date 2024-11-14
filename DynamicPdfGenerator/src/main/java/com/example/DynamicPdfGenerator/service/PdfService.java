package com.example.DynamicPdfGenerator.service;

import com.example.DynamicPdfGenerator.model.InvoiceRequest;
import com.example.DynamicPdfGenerator.repository.PdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PdfService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private PdfRepository pdfRepository;

    public ByteArrayOutputStream generatePdf(InvoiceRequest request) throws Exception {
        String pdfId = pdfRepository.getPdfIdByRequest(request);
        if (pdfId != null) {
            return getPdf(pdfId);
        }

        Context context = new Context();
        context.setVariable("invoice", request);

        String htmlContent = templateEngine.process("invoice", context);
        ByteArrayOutputStream pdfStream = pdfRepository.convertHtmlToPdf(htmlContent);
        pdfRepository.savePdf(pdfStream, request);

        return pdfStream;
    }

    public ByteArrayOutputStream getPdf(String id) throws Exception {
        Path path = Paths.get("pdf-storage/" + id + ".pdf");

        if (!Files.exists(path)) {
            throw new FileNotFoundException("PDF not found for ID: " + id);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Files.copy(path, outputStream);
        return outputStream;
    }

}
