/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gov.usda.ars.spieru.chalk.view;

import gov.usda.ars.spieru.chalk.model.Config;
import gov.usda.ars.spieru.chalk.util.RunProgram;
import gov.usda.ars.spieru.chalk.model.DataStore;
import gov.usda.ars.spieru.chalk.util.FindLastPictureFile;

import ij.IJ;
import ij.ImagePlus;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author wjrfo
 */
public class MainFrame extends javax.swing.JFrame {

    private DataStore dataStore = DataStore.dataStoreFactory();

    /**
     * Creates new form NewJFrame
     */
    public MainFrame() {
        initComponents();
        this.setTitle("Durham wheat chalk analyzer");
        dataStore = DataStore.dataStoreFactory();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        OpenFile = new javax.swing.JFileChooser();
        canvas1 = new java.awt.Canvas();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMOpen = new javax.swing.JMenuItem();
        jMSave = new javax.swing.JMenuItem();
        jMConfig = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMAnalyze = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("test");

        jMenu1.setText("File");

        jMOpen.setMnemonic('O');
        jMOpen.setText("Open");
        jMOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openActionPerformed(evt);
            }
        });
        jMenu1.add(jMOpen);

        jMSave.setMnemonic('x');
        jMSave.setText("Save");
        jMSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });
        jMenu1.add(jMSave);

        jMConfig.setText("Config");
        jMConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMConfigActionPerformed(evt);
            }
        });
        jMenu1.add(jMConfig);
        jMenu1.add(jSeparator1);

        jMExit.setText("Exit");
        jMenu1.add(jMExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenu3.setText("Scan");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem3.setText("Scan");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Analyze");

        jMAnalyze.setText("Analyze");
        jMAnalyze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMAnalyzeActionPerformed(evt);
            }
        });
        jMenu4.add(jMAnalyze);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Help");

        jMenuItem5.setText("About");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, 777, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openActionPerformed
        // TODO add your handling code here:
        Config config = dataStore.getConfig();
        File dirf = new File(config.getScanDirectory());
        JFileChooser jfc = new JFileChooser(dirf);
//        OpenFile.setCurrentDirectory(new File(config.getScanDirectory()));
        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                try {
                    System.out.println(f.getCanonicalPath());

                    return f.getCanonicalPath().endsWith(".png") || f.getCanonicalPath().endsWith(".tif") || f.getCanonicalPath().endsWith(".jpg") || f.isDirectory();
                } catch (IOException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return ".png, *.tif, *.jpg";
            }
        };

//        OpenFile.setFileFilter(fileFilter);
        jfc.setFileFilter(fileFilter);
        //Handle open button action.
        int returnVal = jfc.showOpenDialog(MainFrame.this);
//        int returnVal = OpenFile.showOpenDialog(MainFrame.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

//            File file = OpenFile.getSelectedFile();
            File file = jfc.getSelectedFile();
            System.out.println("selected file " + file.getName());
//            try {
//                System.out.println("current directory : " + new File(".").getCanonicalPath());
//                //     File input = new File(imageName);
//                BufferedImage image = ImageIO.read(file);
//                canvas1.getGraphics().drawImage(image, 0, 0, null);
//                canvas4.getGraphics().drawImage(image, 0, 0, null);
//            } catch (IOException ie) {
//                System.out.println("Error:" + ie.getMessage());
//            }

            ImagePlus imp = IJ.openImage(file.getAbsolutePath());

            dataStore.setIp(imp);
            dataStore.setIp1(dataStore.getDupIP());
            dataStore.setIp2(dataStore.getDupIP());
            dataStore.setIp3(dataStore.getDupIP());
            dataStore.setIp4(dataStore.getDupIP());

            int height = dataStore.getDupIP().getBufferedImage().getHeight();
            int width = dataStore.getDupIP().getBufferedImage().getWidth();
            canvas1.getGraphics().drawImage(dataStore.getIp1().getBufferedImage(), 0, 0, null);
            canvas1.getGraphics().drawImage(dataStore.getIp1().getBufferedImage(), 400, 300, null);


//            canvas1.setSize(width, height);
//            SwingUtilities.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//            canvas1.setSize(height, width);
//            canvas1.getGraphics().drawImage(dataStore.getIp1().getBufferedImage(), 0, 0, null);
//                    }
//            });
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//            canvas1.setSize(width, height);
//            canvas1.getGraphics().drawImage(dataStore.getIp1().getBufferedImage(), 0, 0, null);
//            }
//        });
// 
            dataStore.getIp4().unlock(); // unlock im to run other commands
            IJ.run(dataStore.getIp4(), "8-bit", ""); // run “Invert” command on im
//        IJ.run(im, "Analyze Particles...", "size=0.5-100 circularity=0.1-1.00 show=Outlines display"); 
            dataStore.getIp4().lock(); // lock im again (to be safe)

            //     dataStore.getIp4().getProcessor().rotate(90);
//            Panel panel = null;
/*
            try {
                panel = new ShowImage(file.getCanonicalPath());
                panel.setSize(200, 200);
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.getContentPane().add(panel);
            this.setSize(500, 500);
            this.repaint();
             */
            //This is where a real application would open the file.
//            log.append("Opening: " + file.getName() + "." + newline);
        } else {
//            log.append("Open command cancelled by user." + newline);
        }

    }//GEN-LAST:event_openActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
//        Scan scan = new Scan();
//        try {
////            scan.simpleScan();
//        } catch (Exception ex) {
//            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
            RunProgram rp = new RunProgram();
            rp.runProg();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMAnalyzeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMAnalyzeActionPerformed
        System.out.println("analyze");        // TODO add your handling code here:    FindLastPictureFile flpf = new FindLastPictureFile();
        FindLastPictureFile flpf = new FindLastPictureFile();
        System.out.println("file name " + flpf.getLastFileName());
        ImagePlus ip0 = IJ.openImage(flpf.getLastFileName());
        ip0.show();

    }//GEN-LAST:event_jMAnalyzeActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
JOptionPane.showMessageDialog(this,
    "Eggs are not supposed to be green.\ntest test test\nmore",
    "Durham wheat chalk analyzer",
    JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMConfigActionPerformed
        new ConfigDialog(this, rootPaneCheckingEnabled).setVisible(true);
    }//GEN-LAST:event_jMConfigActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser OpenFile;
    private java.awt.Canvas canvas1;
    private javax.swing.JMenuItem jMAnalyze;
    private javax.swing.JMenuItem jMConfig;
    private javax.swing.JMenuItem jMExit;
    private javax.swing.JMenuItem jMOpen;
    private javax.swing.JMenuItem jMSave;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    // End of variables declaration//GEN-END:variables

}
