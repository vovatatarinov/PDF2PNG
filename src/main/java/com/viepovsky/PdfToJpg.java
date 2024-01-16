package com.viepovsky;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

abstract class PdfToJpg {
    private static File pdf;
    static void convert(String pathToPdf) {
        try {
            pdf = new File(pathToPdf);

            PDDocument document = PDDocument.load(pdf);

            PDFRenderer renderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                String jpgPath = generatePathAndFileName(page);
                BufferedImage jpgImage = renderer.renderImageWithDPI(page, 300);
                ImageIO.write(jpgImage, "jpg", new File(jpgPath));
            }

            document.close();
        } catch (IOException e) {
            System.out.println("Could not convert PDF to JPG");
            System.out.println(e.getMessage());
        }
    }

    private static String generatePathAndFileName(int page) {
        String pdfName = pdf.getName();
        String uuid = UUID.randomUUID().toString();
        return String.format("%s%d-%s", pdfName, page, uuid);
    }
}
