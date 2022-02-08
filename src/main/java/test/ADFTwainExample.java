package test;

import SK.gnome.morena.*;
import SK.gnome.twain.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageConsumer;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ADFTwainExample {

    public static void main(String[] args) throws MorenaException, Exception {
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

    static void makeBufferedImage(MorenaImage morenaImage, int idx) {
      Image image=Toolkit.getDefaultToolkit().createImage(morenaImage); 
      BufferedImage bimg=new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
      bimg.createGraphics().drawImage(image, 0, 0, null);
        try {
            File outF = new File("test" + idx + ".jpg");
            System.out.println(outF.getAbsolutePath());
            ImageIO.write(bimg,"jpg", outF);
        } catch (IOException ex) {
            Logger.getLogger(ADFTwainExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
