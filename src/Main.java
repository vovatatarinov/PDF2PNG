/*
    See original project at https://github.com/viepovsky/pdf-to-jpg-converter
 */
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        FileDialog dialog = new FileDialog((Frame)null, "Select PDF to convert to PNG");
        dialog.setMode(FileDialog.LOAD);
        dialog.setVisible(true);
        String filePath = dialog.getDirectory() + dialog.getFile();
        dialog.dispose();
        JOptionPane.showMessageDialog(null,filePath + " chosen.");
        final JDialog[] jDialog = new JDialog[1];
        final JOptionPane[] jOptionPane = new JOptionPane[1];
        jDialog[0] = new JDialog();
        jDialog[0].setModal(true);
        jDialog[0].setTitle("Message");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Object oldColorOptionPane = UIManager.get("OptionPane.background");
                Object oldColorPanel = UIManager.get("Panel.background");
                UIManager.put("OptionPane.background", Color.white);
                UIManager.put("Panel.background", Color.white);
                jOptionPane[0] = new JOptionPane("Converting PDF to PNG...", JOptionPane.PLAIN_MESSAGE,
                        JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
                UIManager.put("OptionPane.background", oldColorOptionPane);
                UIManager.put("Panel.background", oldColorPanel);
                jDialog[0].setContentPane(jOptionPane[0]);
                jDialog[0].setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                jDialog[0].setBounds(10, 10, 300, 100);
                jDialog[0].setUndecorated(true);
                jDialog[0].getRootPane().setWindowDecorationStyle(JRootPane.NONE);
                jDialog[0].setLocationRelativeTo(null);
                jDialog[0].pack();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        jDialog[0].setVisible(true);
                    }
                }).start();
            }
        }).start();
        PdfToPng.convert(filePath);
        jDialog[0].dispose();
        JOptionPane.showMessageDialog(null, "Program terminated.");
        System.exit(0);
    }
}