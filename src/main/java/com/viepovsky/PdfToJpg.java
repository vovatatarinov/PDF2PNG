package com.viepovsky;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

abstract class PdfToJpg {
    static void convert(String pathToPdf, String pathToJpg) {
        try {
            PDDocument document = PDDocument.load(new File(pathToPdf));

            PDFRenderer renderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                String random = UUID.randomUUID().toString();
                String path = String.format("%s%d-%s.jpg", pathToJpg, i, random);
                BufferedImage image = renderer.renderImageWithDPI(i, 300);
                ImageIO.write(image, "jpg", new File(path));
            }

            document.close();
        } catch (IOException e) {
            System.out.println("Could not convert PDF to JPG");
            System.out.println(e.getMessage());
        }
    }
}
