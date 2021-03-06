package uk.co.rocketlawyer;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceCMYK;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.rendering.PDFRenderer;

public class PDFBoxPerformance {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("sun.java2d.cmm", "sun.java2d.cmm.kcms.KcmsServiceProvider");
        new PDFBoxPerformance().renderPages();
    }

    void renderPages() throws IOException {
        PDType1Font f = PDType1Font.COURIER;
        PDDeviceCMYK.INSTANCE.toRGB(new float[]{0,0,0,0});
        PDDeviceRGB.INSTANCE.toRGB(new float[]{0,0,0});
        int dpi = 600;

        try (PDDocument document = getPDFDocument("/test-pdf-18.pdf")) {
            renderPages(document, dpi);
            System.out.println("---------------");
        } catch (Exception e) {
            System.out.println("Ups. " + e.getMessage());
        }

        try (PDDocument document = getPDFDocument("/test-2p.pdf")) {
            renderPages(document, dpi);
            System.out.println("---------------");
        } catch (Exception e) {
            System.out.println("Ups. " + e.getMessage());
        }
    }

    private void renderPages(PDDocument document, float dpi) throws IOException {
        PDFRenderer renderer = new PDFRenderer(document);
        for (int currentPage = 0; currentPage < document.getNumberOfPages(); currentPage++) {
            LocalDateTime start = LocalDateTime.now();
            BufferedImage bufferedImage = renderer.renderImageWithDPI(currentPage, dpi);
            LocalDateTime finish = LocalDateTime.now();
            System.out.println(
                    String.format("Page %s rendered in %s ms ", currentPage, MILLIS.between(start, finish)));
        }
    }

    private PDDocument getPDFDocument(String file) throws IOException {
        return PDDocument.load(getClass().getResourceAsStream(file));
    }


}
