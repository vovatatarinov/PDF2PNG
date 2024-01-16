package com.viepovsky;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

abstract class PdfToJpg {

    private static File pdfToConvert;

    private static PDDocument pdfDocument;

    private static PDFRenderer pdfRenderer;

    static void convert(String pathToPdf) {
        try {
            initPdfFileDocumentAndRenderer(pathToPdf);

            ZipOutputStream zipOutputStream = createZipOutputStream();

            for (int page = 0; page < pdfDocument.getNumberOfPages(); page++) {
                String jpgPath = createOutputPath(page);
                convertPdfToImage(jpgPath, page);
                putImageInZip(zipOutputStream, jpgPath);
            }

            zipOutputStream.closeEntry();
            zipOutputStream.close();

            pdfDocument.close();
        } catch (IOException e) {
            System.out.println("Could not convert PDF to JPG");
            System.out.println(e.getMessage());
        }
    }

    private static void initPdfFileDocumentAndRenderer(String pathToPdf) throws IOException {
        pdfToConvert = new File(pathToPdf);
        pdfDocument = PDDocument.load(pdfToConvert);
        pdfRenderer = new PDFRenderer(pdfDocument);
    }

    private static ZipOutputStream createZipOutputStream() throws FileNotFoundException {
        String zipPath = createOutputPath();
        FileOutputStream out = new FileOutputStream(zipPath);
        return new ZipOutputStream(out);
    }

    private static String createOutputPath() {
        String path = pdfToConvert.getParent();
        String name = getPdfNameWithoutExtension();
        String uuid = UUID.randomUUID().toString();

        return path + "\\" + name + "-" + uuid + ".zip";
    }

    private static String createOutputPath(int page) {
        String path = pdfToConvert.getParent();
        String name = getPdfNameWithoutExtension();
        String uuid = UUID.randomUUID().toString();

        return String.format("%s\\%s-%d-%s.jpg", path, name, page, uuid);
    }

    private static String getPdfNameWithoutExtension() {
        return pdfToConvert.getName()
                .toLowerCase()
                .replaceAll(".pdf", "");
    }

    private static void convertPdfToImage(String jpgPath, int page) throws IOException {
        BufferedImage jpgImage = pdfRenderer.renderImageWithDPI(page, 300);
        ImageIO.write(jpgImage, "jpg", new File(jpgPath));
    }

    private static void putImageInZip(ZipOutputStream zipOutputStream, String jpgPath) throws IOException {
        File fileToZip = new File(jpgPath);
        ZipEntry ze = new ZipEntry(fileToZip.getName());
        zipOutputStream.putNextEntry(ze);
        Files.copy(fileToZip.toPath(), zipOutputStream);
        fileToZip.delete();
    }
}
