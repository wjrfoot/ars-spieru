/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.io.Opener;
import ij.process.ImageProcessor;
import ij.process.StackProcessor;
// import org.apache.log4j.Logger;



/**
 *
 * @author wjrfo
 */
public class Thumbnail {

    // private final static Logger logger = Logger.getLogger(Thumbnail.class.getName());
      private static final String inFilName = "./data/test.jpg";
      private static final String outFileName = "./data/testThumbNail.jpg";
  
    public static void main(String[] args) {
        cropAndResize(inFilName,outFileName);
    }
    
    private static String cropAndResize(String inFilNam, String outFilNam) {

        try {

            Opener opener = new Opener();

            ImagePlus imp = opener.openImage(inFilNam);

            ImageProcessor ip = imp.getProcessor();

            StackProcessor sp = new StackProcessor(imp.getStack(), ip);

            int width = imp.getWidth();

            int height = imp.getHeight();

            int cropWidth = 0;

            int cropHeight = 0;

            if (width > height) {

                cropWidth = height;

                cropHeight = height;

            } else {

                cropWidth = width;

                cropHeight = width;

            }

            int x = -1;

            int y = -1;

            if (width == height) {

                x = 0;

                y = 0;

            } else if (width > height) {

                x = (width - height) / 2;

                y = 0;

            } else if (width < height) {

                x = 0;

                y = (height - width) / 2;

            }

            // logger.debug(imp.getWidth());

            // logger.debug(imp.getHeight());

            // logger.debug("cropWidth " + cropWidth);

            // logger.debug("cropHeight" + cropHeight);

            ImageStack croppedStack = sp.crop(x, y, cropWidth, cropHeight);

            imp.setStack(null, croppedStack);

            // logger.debug(imp.getWidth());

            // logger.debug(imp.getHeight());

            sp = new StackProcessor(imp.getStack(), imp.getProcessor());

            ImageStack resizedStack = sp.resize(100, 100, true);

            imp.setStack(null, resizedStack);


            IJ.save(imp, outFilNam);

            return outFilNam;

        } catch (Exception e) {

            // logger.error("Error while resizing Image.");

            e.printStackTrace();

            return null;

        }

    }
}
