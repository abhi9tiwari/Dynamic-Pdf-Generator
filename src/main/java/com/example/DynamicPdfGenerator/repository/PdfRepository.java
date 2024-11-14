package com.example.DynamicPdfGenerator.repository;

import com.example.DynamicPdfGenerator.model.InvoiceRequest;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Repository
public class PdfRepository {

    private static final String STORAGE_DIRECTORY = "pdf-storage/";

    public String getPdfIdByRequest(InvoiceRequest request) {
        try{
            String requestHash = generateHash(request);
            Path pdfPath = Paths.get(STORAGE_DIRECTORY + requestHash + ".pdf");

            // Check if PDF file with this hash already exists
            if (Files.exists(pdfPath)) {
                return requestHash;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String generateHash(InvoiceRequest request) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String uniqueString = request.getSeller() + request.getBuyer() + request.getItems().toString();
        byte[] hashBytes = digest.digest(uniqueString.getBytes());
        StringBuilder hashString = new StringBuilder();
        for (byte b : hashBytes) {
            hashString.append(String.format("%02x", b));
        }
        return hashString.toString();
    }

    public ByteArrayOutputStream convertHtmlToPdf(String html) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (OutputStream os = new BufferedOutputStream(outputStream)) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, "");
            builder.toStream(os);
            builder.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    public void savePdf(ByteArrayOutputStream pdfStream, InvoiceRequest request) throws Exception {
        Files.createDirectories(Paths.get("pdf-storage"));
        FileOutputStream fos = new FileOutputStream("pdf-storage/" + request.hashCode() + ".pdf");
        pdfStream.writeTo(fos);
        fos.close();
    }
}
