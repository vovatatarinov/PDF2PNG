package com.viepovsky;

import java.awt.*;

public class MainView {
    public static void main(String[] args) {
        FileDialog dialog = new FileDialog((Frame)null, "Select PDF to convert to JPG");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String filePath = dialog.getDirectory() + dialog.getFile();
        dialog.dispose();
        System.out.println(filePath + " chosen.");

        PdfToJpg.convert(filePath);
    }
}
