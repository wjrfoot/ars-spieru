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
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author wjrfo
 */
public class ColorThresholder {

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

    public static final int HSB = 0, RGB = 1, LAB = 2, YUV = 3;
    private static final String[] colorSpaces = {"HSB", "RGB", "Lab", "YUV"};
    private boolean flag = false;
    private int colorSpace = RGB;
    public static final int RED = 0, WHITE = 1, BLACK = 2, BLACK_AND_WHITE = 3;
    private static final String[] modes = {"Red", "White", "Black", "B&W"};
    private int mode = WHITE;

    byte[] hSource;
    byte[] sSource;
    byte[] bSource;

    boolean bandPassH = false, bandPassS = false, bandPassB = false;

    private int minHue = 0, minSat = 0, minBri = 0;
    private int maxHue = 255, maxSat = 255, maxBri = 255;

    public static void main(String[] args) {

        ColorThresholder colorThresholder = new ColorThresholder();
        colorThresholder.setMinHue(140);
        colorThresholder.setMaxHue(255);
        colorThresholder.setMinSat(115);
        colorThresholder.setMaxSat(255);
        colorThresholder.setMinBri(115);
        colorThresholder.setMaxBri(255);
        colorThresholder.setColorSpace(YUV);

        String fileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\testPanel.bmp";
//        String fileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\FLOUR\\sm-5008_1200.bmp";
        System.out.println("test " + fileName);
        ImagePlus ip0 = IJ.openImage(fileName);
        ip0.show();
        
        ImagePlus ip1 = ip0.duplicate();

        colorThresholder.setIP(ip1);

        colorThresholder.checkSquares(ip1.getProcessor());
        
        colorThresholder.apply(ip1);
        
        colorThresholder.checkSquares(ip1.getProcessor());
        
        ip1.show();
    }

    public ColorThresholder() {
    }
    
    /**
     * sets params for thresholding
     * 
     * @param minHue min value for Hue, Red, L or Y
     * @param maxHue max value for Hue, Red, L or Y
     * @param minSat min value for Sat, Green, A or U
     * @param maxSat max value for Sat, Green, A or U
     * @param minBri min value for Bri, Blue, B or V
     * @param maxBri max Value for Bri, Blue, B or V
     * @param colorSpace HSB, RGB, LAB or YUV
     * @param mode fill color, RED, WHITE, BLACK or BLACK_AND_WHITE
     */
    public void setParams(int minHue, int maxHue, int minSat, int maxSat, 
            int minBri, int maxBri, int colorSpace, int mode) {
        setMinHue(minHue);
        setMaxHue(maxHue);
        setMinSat(minSat);
        setMaxSat(maxSat);
        setMinBri(minBri);
        setMaxBri(maxBri);
        setColorSpace(colorSpace);
        setMode(mode);
    }

    void setIP(ImagePlus ip1) {
        
        
        ColorProcessor colorProcessor = (ColorProcessor) ip1.getProcessor();

        int numPixels = colorProcessor.getPixelCount();

        hSource = new byte[numPixels];
        sSource = new byte[numPixels];
        bSource = new byte[numPixels];

        switch (getColorSpace()) {
            case RGB:
                colorProcessor.getRGB(hSource, sSource, bSource);
                break;
            case HSB:
                colorProcessor.getHSB(hSource, sSource, bSource);
                break;
            case LAB:
                getLab(colorProcessor, hSource, sSource, bSource);
                break;
            case YUV:
                getYUV(colorProcessor, hSource, sSource, bSource);
                break;
            default:
                break;
        }

//       System.out.println("");

    }

    /**
    *   Returns YUV in 3 byte arrays.
    * 
    *   RGB <--> YUV Conversion Formulas from http://www.cse.msu.edu/~cbowen/docs/yuvtorgb.html
    *   R = Y + (1.4075 * (V - 128));
    *   G = Y - (0.3455 * (U - 128) - (0.7169 * (V - 128));
    *   B = Y + (1.7790 * (U - 128);
    *
    *   Y = R *  .299 + G *  .587 + B *  .114;
    *   U = R * -.169 + G * -.332 + B *  .500 + 128.;
    *   V = R *  .500 + G * -.419 + B * -.0813 + 128.;
    * 
     * @param ip
     * @param Y
     * @param U
     * @param V 
     */
   
    public void getYUV(ImageProcessor ip, byte[] Y, byte[] U, byte[] V) {

        int  r, g, b;
        double yf;

        int pixels[] = (int[]) ip.getPixels();

        for (int pdx = 0; pdx < pixels.length; pdx++) {
            
//            if ((pdx % (ip.getPixelCount() / 125)) == 0) {
//                System.out.printf("%6d %x\n", pdx, pixels[pdx]);
//            }

            r = ((pixels[pdx] & 0xff0000) >> 16);//R
            g = ((pixels[pdx] & 0x00ff00) >> 8);//G
            b = (pixels[pdx] & 0x0000ff); //B 

            // Kai's plugin
            yf = (0.299 * r + 0.587 * g + 0.114 * b);
            Y[pdx] = (byte) ((int) Math.floor(yf + 0.5));
            U[pdx] = (byte) (128 + (int) Math.floor((0.493 * (b - yf)) + 0.5));
            V[pdx] = (byte) (128 + (int) Math.floor((0.877 * (r - yf)) + 0.5));

        }
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
    
    private static void dumpPixels(ImageProcessor imageProcessor, int mod) {
        
        int[] pixels = (int[]) imageProcessor.getPixels();
                System.out.printf("%6d %x\n", 0, pixels[0]);
                System.out.printf("%6d %x\n", pixels.length-1, pixels[pixels.length-1]);
        for (int pdx = 0; pdx < pixels.length; pdx++) {
            if ((pdx % mod) == 1000) {
                System.out.printf("%6d %x\n", pdx, pixels[pdx]);
            }
        }
    }

    Color thresholdColor() {
        Color color = null;
        switch (getMode()) {
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

        ImageProcessor fillMaskIP = new ByteProcessor(imp.getWidth(), imp.getHeight());
        imp.setProperty("Mask", fillMaskIP);

//        byte[] fillMask = new byte[imp.getWidth() * imp.getHeight()];
        byte[] fillMask = (byte[]) fillMaskIP.getPixels();
        byte fill = (byte) 255;
        byte keep = (byte) 0;
        int numPixels = fillMask.length;
 
        // todo add in the bandpass flag
        for (int pdx = 0; pdx < numPixels; pdx++) {
            
            int hue = hSource[pdx] & 0xff;
            boolean hueFlg = (hue >= getMinHue()) && (hue <= getMaxHue()); // && bandPassH;

            int sat = sSource[pdx] & 0xff;
            boolean satFlg = (sat >= getMinSat()) && (sat <= getMaxSat()); // && bandPassS;

            int bri = bSource[pdx] & 0xff;
            boolean briFlg = (bri >= getMinBri()) && (bri <= getMaxBri()); // && bandPassH;
                
            fillMask[pdx] = (hueFlg && satFlg && briFlg) ? fill : keep;
        }

        ImageProcessor ip = imp.getProcessor();
        if (ip == null) {
            return;
        }
        if (getMode() == BLACK_AND_WHITE) {
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
    
    private void checkSquares(ImageProcessor imageProcessor) {
        
        ArrayList<HashMap> mapList = new ArrayList<>();

        // assumes that there are 5 different colors leading to an image
        // with 25x5 (w x h) boxes and each box is 100 x 100 pixels
        
        for (int rdx = 0; rdx < 5; rdx++ ) {
            for (int gdx = 0; gdx < 5; gdx++ ) {
                for (int bdx = 0; bdx < 5; bdx++ ) {
                    HashMap<Integer, Integer> hashMap = new HashMap<>();
                    mapList.add(hashMap);
                    int initX = (5 * rdx + gdx) * 100;
                    int initY = bdx * 100;
                    for (int xdx = 0; xdx < 100; xdx++) {
                        for (int ydx = 0; ydx < 100; ydx++) {
                            int pixel = imageProcessor.getPixel(xdx + initX, ydx + initY);
                            Integer total = hashMap.get(pixel);
                            if (total == null) {
                                hashMap.put(pixel, 1);
                            } else {
                                int tot = total.intValue();
                                tot++;
                                hashMap.put(pixel, tot);
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("debug");
    }

    //<editor-fold defaultstate="collapsed" desc="original code">
    
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
//			ColorProcessor colorProcessor = (ColorProcessor)ip;
//			IJ.showStatus("Converting colour space...");
//			if(colorSpace==RGB)
//				colorProcessor.getRGB(hSource,sSource,bSource);
//			else if(colorSpace==HSB)
//				colorProcessor.getHSB(hSource,sSource,bSource);
//			else if(colorSpace==LAB)
//				getLab(colorProcessor, hSource,sSource,bSource);
//			else if(colorSpace==YUV)
//				getYUV(colorProcessor, hSource,sSource,bSource);
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
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    
    /**
     * @return the colorSpace
     */
    public int getColorSpace() {
        return colorSpace;
    }

    /**
     * @param colorSpace the colorSpace to set
     */
    public void setColorSpace(int colorSpace) {
        this.colorSpace = colorSpace;
    }
//</editor-fold>

    /**
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }
}
