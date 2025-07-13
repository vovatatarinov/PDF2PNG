import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

abstract class PdfToPng {

    private static File pdfToConvert;

    private static PDDocument pdfDocument;

    private static PDFRenderer pdfRenderer;

    static void convert(String pathToPdf) {
        try {
            initPdfFileDocumentAndRenderer(pathToPdf);

            ZipOutputStream zipOutputStream = createZipOutputStream();

            for (int page = 0; page < pdfDocument.getNumberOfPages(); page++) {
                String pngPath = createOutputPath(page);
                convertPdfToImage(pngPath, page);
                putImageInZip(zipOutputStream, pngPath);
            }

            zipOutputStream.closeEntry();
            zipOutputStream.close();

            pdfDocument.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not convert PDF to PNG" +
                    "\r\n" + e.getMessage());
        }
    }

    private static void initPdfFileDocumentAndRenderer(String pathToPdf) throws IOException {
        pdfToConvert = new File(pathToPdf);
        pdfDocument = Loader.loadPDF(pdfToConvert);
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

        return String.format("%s\\%s-%06d-%s.png", (Object) path, (Object) name, (Object) page, uuid);
    }

    private static String getPdfNameWithoutExtension() {
        return pdfToConvert.getName()
                .toLowerCase()
                .replaceAll(".pdf", "");
    }

    private static void convertPdfToImage(String pngPath, int page) throws IOException {
        BufferedImage pngImage = pdfRenderer.renderImageWithDPI(page, 300);
        ImageIO.write(pngImage, "png", new File(pngPath));
    }

    private static void putImageInZip(ZipOutputStream zipOutputStream, String pngPath) throws IOException {
        File fileToZip = new File(pngPath);
        ZipEntry ze = new ZipEntry(fileToZip.getName());
        zipOutputStream.putNextEntry(ze);
        Files.copy(fileToZip.toPath(), zipOutputStream);
        boolean delRes = fileToZip.delete();
    }
}