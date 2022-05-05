/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.flour;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;
import static ij.plugin.frame.ColorThresholder.getLab;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.ColorSpaceConverter;
import ij.process.ImageProcessor;
import java.awt.Color;

/**
 *
 * @author wjrfo
 */
public class TestColorThreshold2 {

//<editor-fold defaultstate="collapsed" desc="getters/setters">
    
    /**
     * @return the minHue
     */
    public int getMinHue() {
        return minHue;
    }

    /**
     * @param minHue the minHue to set
     */
    public void setMinHue(int minHue) {
        this.minHue = minHue;
    }

    /**
     * @return the minSat
     */
    public int getMinSat() {
        return minSat;
    }

    /**
     * @param minSat the minSat to set
     */
    public void setMinSat(int minSat) {
        this.minSat = minSat;
    }

    /**
     * @return the minBri
     */
    public int getMinBri() {
        return minBri;
    }

    /**
     * @param minBri the minBri to set
     */
    public void setMinBri(int minBri) {
        this.minBri = minBri;
    }

    /**
     * @return the maxHue
     */
    public int getMaxHue() {
        return maxHue;
    }

    /**
     * @param maxHue the maxHue to set
     */
    public void setMaxHue(int maxHue) {
        this.maxHue = maxHue;
    }

    /**
     * @return the maxSat
     */
    public int getMaxSat() {
        return maxSat;
    }

    /**
     * @param maxSat the maxSat to set
     */
    public void setMaxSat(int maxSat) {
        this.maxSat = maxSat;
    }

    /**
     * @return the maxBri
     */
    public int getMaxBri() {
        return maxBri;
    }

    /**
     * @param maxBri the maxBri to set
     */
    public void setMaxBri(int maxBri) {
        this.maxBri = maxBri;
    }
//</editor-fold>

    private static final int HSB = 0, RGB = 1, LAB = 2, YUV = 3;
    private static final String[] colorSpaces = {"HSB", "RGB", "Lab", "YUV"};
    private boolean flag = false;
    private int colorSpace = YUV;
    private static final int RED = 0, WHITE = 1, BLACK = 2, BLACK_AND_WHITE = 3;
    private static final String[] modes = {"Red", "White", "Black", "B&W"};
    int mode = WHITE;

    byte[] hSource;
    byte[] sSource;
    byte[] bSource;

    boolean bandPassH = false, bandPassS = false, bandPassB = false;

    private int minHue = 0, minSat = 0, minBri = 0;
    private int maxHue = 255, maxSat = 255, maxBri = 255;

    public static void main(String[] args) {

        TestColorThreshold2 tc2 = new TestColorThreshold2();
        tc2.setMinHue(0);
        tc2.setMaxHue(211);
        tc2.setMinSat(118);
        tc2.setMaxSat(255);
        tc2.setMinBri(0);
        tc2.setMaxBri(255);
  
        String fileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\testPanel.png";
//        String fileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\FLOUR\\sm-5008_1200.bmp";
        System.out.println("test " + fileName);
        ImagePlus ip0 = IJ.openImage(fileName);
        ip0.show();
        ImagePlus ip1 = ip0.duplicate();
        ip1.getProcessor().smooth();
        
        tc2.setIP(ip1);
        
        tc2.apply(ip1);
  }

    public TestColorThreshold2() {
  //        ip0.show();
//        String testParams = "Y=0-211 U=118-255 V=0-255 YUV White dark";
//        ip1.getProcessor().autoThreshold();
//        ColorThresholder2 ct2 = new ColorThresholder2(ip1, testParams);
//        ip1.show();
    }
    
    void setIP(ImagePlus ip1) {
        ColorProcessor cp = (ColorProcessor) ip1.getProcessor();
        ImageStack stack = cp.getHSBStack();
        ImagePlus imp2 = new ImagePlus("HSBstack", stack);
//        imp2.show("imp2");

        ImagePlus imp3 = (new ColorSpaceConverter()).RGBToLab(ip1);
//        imp3.show("imp3");

        stack = imp3.getStack();
        int width = stack.getWidth();
        int height = stack.getHeight();
        int numPixels = width * height;

        hSource = new byte[numPixels];
        sSource = new byte[numPixels];
        bSource = new byte[numPixels];

        if (colorSpace == RGB) {
            cp.getRGB(hSource, sSource, bSource);
        } else if (colorSpace == HSB) {
            cp.getHSB(hSource, sSource, bSource);
        } else if (colorSpace == LAB) {
            getLab(cp, hSource, sSource, bSource);
        } else if (colorSpace == YUV) {
            getYUV(cp, hSource, sSource, bSource);
        }

        ImageStack stack2 = ip1.getStack();

        System.out.println("");

    }

    public void getYUV(ImageProcessor ip, byte[] Y, byte[] U, byte[] V) {
        // Returns YUV in 3 byte arrays.

        //RGB <--> YUV Conversion Formulas from http://www.cse.msu.edu/~cbowen/docs/yuvtorgb.html
        //R = Y + (1.4075 * (V - 128));
        //G = Y - (0.3455 * (U - 128) - (0.7169 * (V - 128));
        //B = Y + (1.7790 * (U - 128);
        //
        //Y = R *  .299 + G *  .587 + B *  .114;
        //U = R * -.169 + G * -.332 + B *  .500 + 128.;
        //V = R *  .500 + G * -.419 + B * -.0813 + 128.;
        int c, x, y, i = 0, r, g, b;
        double yf;

        int width = ip.getWidth();
        int height = ip.getHeight();

        for (y = 0; y < height; y++) {
            for (x = 0; x < width; x++) {
                c = ip.getPixel(x, y);

                r = ((c & 0xff0000) >> 16);//R
                g = ((c & 0x00ff00) >> 8);//G
                b = (c & 0x0000ff); //B 

                // Kai's plugin
                yf = (0.299 * r + 0.587 * g + 0.114 * b);
                Y[i] = (byte) ((int) Math.floor(yf + 0.5));
                U[i] = (byte) (128 + (int) Math.floor((0.493 * (b - yf)) + 0.5));
                V[i] = (byte) (128 + (int) Math.floor((0.877 * (r - yf)) + 0.5));

                //Y[i] = (byte) (Math.floor( 0.299 * r + 0.587 * g + 0.114  * b)+.5);
                //U[i] = (byte) (Math.floor(-0.169 * r - 0.332 * g + 0.500  * b + 128.0)+.5);
                //V[i] = (byte) (Math.floor( 0.500 * r - 0.419 * g - 0.0813 * b + 128.0)+.5);
                i++;
            }
        }
    }

    /**
     * Converts the current image from RGB to CIE L*a*b* and stores the results
     * in the same RGB image R=L*, G=a*, B=b*. Values are therfore offset and
     * rescaled.
     */
    public static void RGBtoLab() {
        ImagePlus imp = IJ.getImage();
        if (imp.getBitDepth() == 24) {
            imp.setProcessor(RGBtoLab(imp.getProcessor()));
        }
    }

    private static ImageProcessor RGBtoLab(ImageProcessor ip) {
        int n = ip.getPixelCount();
        byte[] L = new byte[n];
        byte[] a = new byte[n];
        byte[] b = new byte[n];
        getLab(ip, L, a, b);
        ColorProcessor cp = new ColorProcessor(ip.getWidth(), ip.getHeight());
        cp.setRGB(L, a, b);
        return cp;
    }

    /**
     * Returns Lab in 3 byte arrays.
     */
    public static void getLab(ImageProcessor ip, byte[] L, byte[] a, byte[] b) {
        ColorSpaceConverter converter = new ColorSpaceConverter();
        int[] pixels = (int[]) ip.getPixels();
        for (int i = 0; i < pixels.length; i++) {
            double[] values = converter.RGBtoLAB(pixels[i]);
            int L1 = (int) (values[0] * 2.55);
            int a1 = (int) (Math.floor((1.0625 * values[1] + 128) + 0.5));
            int b1 = (int) (Math.floor((1.0625 * values[2] + 128) + 0.5));
            L[i] = (byte) ((int) (L1 < 0 ? 0 : (L1 > 255 ? 255 : L1)) & 0xff);
            a[i] = (byte) ((int) (a1 < 0 ? 0 : (a1 > 255 ? 255 : a1)) & 0xff);
            b[i] = (byte) ((int) (b1 < 0 ? 0 : (b1 > 255 ? 255 : b1)) & 0xff);
        }
    }

    Color thresholdColor() {
        Color color = null;
        switch (mode) {
            case RED:
                color = Color.red;
                break;
            case WHITE:
                color = Color.white;
                break;
            case BLACK:
                color = Color.black;
                break;
            case BLACK_AND_WHITE:
                color = Color.black;
                break;
        }
        return color;
    }

    void apply(ImagePlus imp) {
        System.out.println("apply");
        if (IJ.debugMode) {
            IJ.log("ColorThresholder.apply");
        }
//        ImageProcessor fillMaskIP = (ImageProcessor) imp.getProperty("Mask");
//        if (fillMaskIP == null) {
//            System.out.println("no mask");
//            return;
//        }

	
			ImageProcessor fillMaskIP = new ByteProcessor(imp.getWidth(), imp.getHeight());
			imp.setProperty("Mask", fillMaskIP);


//        byte[] fillMask = new byte[imp.getWidth() * imp.getHeight()];
       byte[] fillMask = (byte[]) fillMaskIP.getPixels();
        byte fill = (byte) 255;
        byte keep = (byte) 0;
        int numPixels = fillMask.length;

        if (bandPassH && bandPassS && bandPassB) { //PPP All pass
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue < getMinHue()) || (hue > getMaxHue())) || ((sat < getMinSat()) || (sat > getMaxSat())) || ((bri < getMinBri()) || (bri > getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        } else if (!bandPassH && !bandPassS && !bandPassB) { //SSS All stop
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue >= getMinHue()) && (hue <= getMaxHue())) || ((sat >= getMinSat()) && (sat <= getMaxSat())) || ((bri >= getMinBri()) && (bri <= getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        } else if (bandPassH && bandPassS && !bandPassB) { //PPS
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue < getMinHue()) || (hue > getMaxHue())) || ((sat < getMinSat()) || (sat > getMaxSat())) || ((bri >= getMinBri()) && (bri <= getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        } else if (!bandPassH && !bandPassS && bandPassB) { //SSP
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue >= getMinHue()) && (hue <= getMaxHue())) || ((sat >= getMinSat()) && (sat <= getMaxSat())) || ((bri < getMinBri()) || (bri > getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        } else if (bandPassH && !bandPassS && !bandPassB) { //PSS
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue < getMinHue()) || (hue > getMaxHue())) || ((sat >= getMinSat()) && (sat <= getMaxSat())) || ((bri >= getMinBri()) && (bri <= getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        } else if (!bandPassH && bandPassS && bandPassB) { //SPP
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue >= getMinHue()) && (hue <= getMaxHue())) || ((sat < getMinSat()) || (sat > getMaxSat())) || ((bri < getMinBri()) || (bri > getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        } else if (!bandPassH && bandPassS && !bandPassB) { //SPS
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue >= getMinHue()) && (hue <= getMaxHue())) || ((sat < getMinSat()) || (sat > getMaxSat())) || ((bri >= getMinBri()) && (bri <= getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        } else if (bandPassH && !bandPassS && bandPassB) { //PSP
            for (int j = 0; j < numPixels; j++) {
                int hue = hSource[j] & 0xff;
                int sat = sSource[j] & 0xff;
                int bri = bSource[j] & 0xff;
                if (((hue < getMinHue()) || (hue > getMaxHue())) || ((sat >= getMinSat()) && (sat <= getMaxSat())) || ((bri < getMinBri()) || (bri > getMaxBri()))) {
                    fillMask[j] = keep;
                } else {
                    fillMask[j] = fill;
                }
            }
        }

        ImageProcessor ip = imp.getProcessor();
        if (ip == null) {
            return;
        }
        if (mode == BLACK_AND_WHITE) {
            int[] pixels = (int[]) ip.getPixels();
            int fcolor = Prefs.blackBackground ? 0xffffffff : 0xff000000;
            int bcolor = Prefs.blackBackground ? 0xff000000 : 0xffffffff;
            for (int i = 0; i < numPixels; i++) {
                if (fillMask[i] != 0) {
                    pixels[i] = fcolor;
                } else {
                    pixels[i] = bcolor;
                }
            }
        } else {
            ip.setColor(thresholdColor());
            ip.fill(fillMaskIP);
        }
        
        imp.setTitle("applied");
        imp.show("apply !!!");
    }

    
//    boolean setup(ImagePlus imp) {
//		if (IJ.debugMode) IJ.log("ColorThresholder.setup");
//		ImageProcessor ip;
//		int type = imp.getType();
//		if (type!=ImagePlus.COLOR_RGB)
//			return false;
//		ip = imp.getProcessor();
//		int id = imp.getID();
//		int slice = imp.getCurrentSlice();
//		if ((id!=previousImageID)||(slice!=previousSlice)||(flag) ) {
//			Undo.reset();
//			flag = false; //if true, flags a change of colour model
//			numSlices = imp.getStackSize();
//			stack = imp.getStack();
//			width = stack.getWidth();
//			height = stack.getHeight();
//			numPixels = width*height;
//
//			hSource = new byte[numPixels];
//			sSource = new byte[numPixels];
//			bSource = new byte[numPixels];
//			
//			ImageProcessor mask = new ByteProcessor(width, height);
//			imp.setProperty("Mask", mask);
//
//			//Get hsb or rgb from image.
//			ColorProcessor cp = (ColorProcessor)ip;
//			IJ.showStatus("Converting colour space...");
//			if(colorSpace==RGB)
//				cp.getRGB(hSource,sSource,bSource);
//			else if(colorSpace==HSB)
//				cp.getHSB(hSource,sSource,bSource);
//			else if(colorSpace==LAB)
//				getLab(cp, hSource,sSource,bSource);
//			else if(colorSpace==YUV)
//				getYUV(cp, hSource,sSource,bSource);
//
//			IJ.showStatus("");
//
//			//Create a spectrum ColorModel for the Hue histogram plot.
//			Color c;
//			byte[] reds = new byte[256];
//			byte[] greens = new byte[256];
//			byte[] blues = new byte[256];
//			for (int i=0; i<256; i++) {
//				c = Color.getHSBColor(i/255f, 1f, 1f);
//				reds[i] = (byte)c.getRed();
//				greens[i] = (byte)c.getGreen();
//				blues[i] = (byte)c.getBlue();
//			}
//			ColorModel cm = new IndexColorModel(8, 256, reds, greens, blues);
//
//			//Make an image with just the hue from the RGB image and the spectrum LUT.
//			//This is just for a hue histogram for the plot.  Do not show it.
//			//ByteProcessor bpHue = new ByteProcessor(width,height,h,cm);
//			ByteProcessor bpHue = new ByteProcessor(width,height,hSource,cm);
//			ImagePlus impHue = new ImagePlus("Hue",bpHue);
//			//impHue.show();
//
//			ByteProcessor bpSat = new ByteProcessor(width,height,sSource,cm);
//			ImagePlus impSat = new ImagePlus("Sat",bpSat);
//			//impSat.show();
//
//			ByteProcessor bpBri = new ByteProcessor(width,height,bSource,cm);
//			ImagePlus impBri = new ImagePlus("Bri",bpBri);
//			//impBri.show();
//
//			plot.setHistogram(impHue, 0);
//			splot.setHistogram(impSat, 1);
//			bplot.setHistogram(impBri, 2);
//
//			if (!applyingStack)
//				autoSetThreshold();
//			imp.updateAndDraw();
//		}
//		previousImageID = id;
//		previousSlice = slice;
//		return ip!=null;
//	}

}
