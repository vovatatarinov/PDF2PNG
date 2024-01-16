package com.viepovsky;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

abstract class PdfToJpg {
    private static File pdfToConvert;
    private static File outputDirectory;

    static void convert(String pathToPdf) {
        try {
            pdfToConvert = new File(pathToPdf);

            PDDocument document = PDDocument.load(pdfToConvert);

            PDFRenderer renderer = new PDFRenderer(document);

            createOutputDirectory();

            for (int page = 0; page < document.getNumberOfPages(); page++) {
                String jpgPath = getJpgOutputPathAndUniqueName(page);
                BufferedImage jpgImage = renderer.renderImageWithDPI(page, 300);
                ImageIO.write(jpgImage, "jpg", new File(jpgPath));
            }

            document.close();
        } catch (IOException e) {
            System.out.println("Could not convert PDF to JPG");
            System.out.println(e.getMessage());
        }
    }

    private static void createOutputDirectory() {
        String path = pdfToConvert.getParent();
        String name = getPdfNameWithoutExtension();
        String uuid = UUID.randomUUID().toString();

        String directoryPathAndName = path + "\\" + name + "-" + uuid;
        outputDirectory = new File(directoryPathAndName);

        if (outputDirectory.mkdir()) System.out.println("Utworzono katalog");
        else System.out.println("Nie utworzono katalogu");
    }

    private static String getJpgOutputPathAndUniqueName(int page) {
        String path = outputDirectory.getPath();
        String name = getPdfNameWithoutExtension();
        String uuid = UUID.randomUUID().toString();

        return String.format("%s\\%s-%d-%s.jpg", path, name, page, uuid);
    }

    private static String getPdfNameWithoutExtension() {
        return pdfToConvert.getName()
                .toLowerCase()
                .replaceAll(".pdf", "");
    }
}
