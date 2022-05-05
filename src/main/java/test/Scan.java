package test;

import SK.gnome.morena.*;
import SK.gnome.twain.*;
import gov.usda.ars.spieru.chalk.model.Config;
import ij.IJ;
import ij.ImagePlus;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Scan {

    static String fileName = System.getProperty("user.home") + "\\AppData\\Local\\ARS-SPIERU\\temp.png";

    public static void main(String[] args) throws MorenaException, Exception {
        Config config = new Config();
        scan(config);
        ImagePlus imagePlus = IJ.openImage(fileName);
//        new My_Inverter_A().run(imagePlus.getProcessor());
        imagePlus.show();
    }

    public static BufferedImage scan(Config config) {
        BufferedImage bufferedImage = null;

        try {
            TwainSource[] sources = TwainManager.listSources();
//            TwainSource source = TwainManager.selectSource(sources[0]);
            TwainSource source = sources[0];
            System.err.println("Selected source is " + source);
            if (source != null) {
                source.setVisible(false);
                // must set X & Y; generic setResolution doesn't appear to work
                source.setXResolution(300.0);
                source.setYResolution(300.0);
//                source.setColorMode();
                // set scan area -- must use pixels; inches don't appear to work
                source.setUnits(TwainConstants.TWUN_PIXELS);
                source.setFrame(00, 1500, 1200, 3000);
//                source.setFrame(00, 00, 2400, 3000);
//                source.setSupportedSizes(TwainConstants.TWSS_USLETTER);
                // not quite sure what this does
                source.setLightSource(1);
                double temp[] = source.getSupportedThreshold();
                double[] test2 = source.getSupportedExposureTime();
//                source.setThreshold(200.0);
                double [] tim = source.getSupportedExposureTime();
//                source.setBrightness(200.0);
//                source.setContrast(100.0);
//                source.setGamma(1.2);
//                source.setBehaviorMask(40);
//                source.setAutoBright(false);
//                source.getsup
                
//                double[] contrast = source.getSupportedContrast();
//                source.setContrast(1000);
//                source.setBrightness(1000);
//                
//                source.setGamma(2.2);
                
//                source.setFlipRotation(180);
//                source.setRotation(180);
 
//                double[] brightness = source.getSupportedBrightness();
//                source.setBrightness(500);
                
                // todo update use config to select
                source.setLightPath(1);
                MorenaImage morenaImage = new MorenaImage(source);
                System.err.println("Size of acquired image is "
                        + morenaImage.getWidth() + " x "
                        + morenaImage.getHeight() + " x "
                        + morenaImage.getPixelSize());

                Image image = Toolkit.getDefaultToolkit().createImage(morenaImage);

                bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
                bufferedImage.createGraphics().drawImage(image, 0, 0, null);
                String debugFileName = System.getProperty("user.home") + "\\AppData\\Local\\ARS-SPIERU\\debug.png";
                File debugFile = new File(debugFileName);
                System.out.println("Output file name: " + debugFileName);
                ImageIO.write(bufferedImage, "png", debugFile);
                bufferedImage = invert(bufferedImage);
                File tempFile = new File(fileName);
                ImageIO.write(bufferedImage, "png", tempFile);

            }
            TwainManager.close();
            return bufferedImage;
        } catch (TwainException te) {
            JOptionPane.showMessageDialog(null, "Scanner not found\nCheck to make sure that it is turned on",
                    "Error: Scan failed", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            Logger.getLogger(Scan.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * inverts image
     *
     * @param bufferedImage
     * @return
     */
    private static BufferedImage invert(BufferedImage bufferedImage) {
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                //Retrieving contents of a pixel
                int pixel = bufferedImage.getRGB(x, y);
                int dpixel = pixel;
                pixel ^= 0x00ffffff;
//                if (dpixel != 0xff000000) {
//                    System.out.printf("%x %x\n", dpixel, pixel);
//                }
                bufferedImage.setRGB(x, y, pixel);
            }
        }

        return bufferedImage;
    }

}
