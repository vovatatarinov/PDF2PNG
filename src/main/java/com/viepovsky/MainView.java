package com.viepovsky;

import java.awt.*;

public class MainView {
    public static void main(String[] args) {
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String filePath = dialog.getDirectory();
        dialog.dispose();
        System.out.println(filePath + " chosen.");

        FileDialog dialog2 = new FileDialog((Frame)null, "Select File to Save");
        dialog2.setMode(FileDialog.LOAD);
        dialog2.setVisible(true);
        String filePath2 = dialog2.getDirectory();
        dialog2.dispose();
        System.out.println(filePath2 + " chosen.");
        PdfToJpg.convert(filePath, filePath2);
    }
}
