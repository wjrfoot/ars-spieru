package gov.usda.ars.spieru.chalk.controller;

import SK.gnome.morena.*;
import SK.gnome.twain.*;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import gov.usda.ars.spieru.chalk.model.Config;
import javax.swing.JOptionPane;

public class Scan {

    private Config config = null;
    
    public static void main(String[] args) throws MorenaException, Exception {
            TwainSource tss[] = TwainManager.listSources();
            for (TwainSource ts : tss) {
                System.out.println(ts.toString());
            }
        TwainSource source = TwainManager.selectSource(null);
        System.err.println("Selected source is " + source);
        if (source != null) {
            source.setFeederEnabled(true);
            source.setAutoFeed(true);
            source.setTransferCount(5);
            source.setVisible(false);
            int[] i1 = source.getSupportedLightSource();
            int[] i2 = source.getSupportedLightPath();
            System.out.println("i1 " + i1.toString() + " i2 " + i2.toString());
            int count = 1;
            MorenaImage image = null;
            for (int idx = 0; idx < i2.length; idx++) {
                source.setLightPath(idx);
                image = new MorenaImage(source);

                System.err.println("Size of acquired image " + (count++) + " is "
                        + image.getWidth() + " x "
                        + image.getHeight() + " x "
                        + image.getPixelSize());
                makeBufferedImage(image, idx);
            }
        }
        TwainManager.close();
    }

    public Scan(Config config) {
        this.config = config;
    }
   
    public String run() {
        TwainSource source;
        String fileName = null;
        
        try {
            TwainSource tss[] = TwainManager.listSources();
            for (TwainSource ts : tss) {
                System.out.println(ts.toString());
            }
            source = TwainManager.selectSource(null);
            System.err.println("Selected source is " + source);
            if (source != null) {
//                source.setFeederEnabled(true);
//                source.setAutoFeed(true);
//                source.setTransferCount(5);
                source.setVisible(false);
                source.setIndicators(false);
                if (config.getLightSource().equals(Config.lightSource)) {
                    source.setLightPath(1);
                } else {
                    source.setLightPath(0);
                }

                MorenaImage image = null;
                image = new MorenaImage(source);

                System.err.println("Size of acquired image " + 99 + " is "
                        + image.getWidth() + " x "
                        + image.getHeight() + " x "
                        + image.getPixelSize());
                fileName = makeBufferedImage(image, 99);
            } else {
                JOptionPane.showMessageDialog(null, "Could not find scanner", "Scanner error", JOptionPane.ERROR_MESSAGE);

            }
            TwainManager.close();
        } catch (TwainException ex) {
            Logger.getLogger(Scan.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Did not complete scan successfully", "Scanner error", JOptionPane.ERROR_MESSAGE);

        }
        return fileName;
    }

    static String makeBufferedImage(MorenaImage morenaImage, int idx) {
        Image image = Toolkit.getDefaultToolkit().createImage(morenaImage);
        BufferedImage bimg = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bimg.createGraphics().drawImage(image, 0, 0, null);
        try {
            File outF = new File("test" + idx + ".jpg");
            System.out.println(outF.getAbsolutePath());
            ImageIO.write(bimg, "jpg", outF);
            return outF.getAbsolutePath();
        } catch (IOException ex) {
            Logger.getLogger(Scan.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
}
