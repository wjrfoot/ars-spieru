/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.flour;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ByteProcessor;
import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;

/**
 * Analyze flour smut images.
 *
 * @author wjrfo
 */
public class Smut {

    String fileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\SMUT\\SG-5008_600-001.tif";

    public static void main(String[] args) {
        new Smut().run();
    }

    public Smut() {

    }

    public void run() {
        System.out.println("test " + fileName);
        ImagePlus ip0 = IJ.openImage(fileName);
        ip0.show();
        ImagePlus ip1 = ip0.duplicate();

        ip1.getProcessor().smooth();
        int[] pixels = (int[]) ip1.getProcessor().getPixels();

        System.out.println("pixel length " + pixels.length);

        for (int idx = 0; idx < pixels.length; idx++) {
            pixels[idx] &= 0xff0000;
        }

        int width = ip1.getWidth();
        int height = ip1.getHeight();
        byte[] maskBits = new byte[pixels.length];
        for (int idx = 0; idx < maskBits.length; idx++) {
            maskBits[idx] = (byte) (((idx % 2) == 0) ? 0 : 1);
        }
        ByteProcessor bpHue = new ByteProcessor(width, height, maskBits);
        ImagePlus impHue = new ImagePlus("Hue", bpHue);

        ip1.getProcessor().setPixels(pixels);

        ip1.getProcessor().setMask(bpHue);

//        ip1.show();
        boolean[] mask = makeMask(ip0, 0, 211, 118, 255, 0, 255, YUV);

        ImagePlus ip2 = applyMask(ip0, mask, 0x00FFFFFF);

        ip2.show();
    }

    private ImagePlus applyMask(ImagePlus ip, boolean[] mask, int falsePixel) {
        ImagePlus rtnIP = ip.duplicate();

        int trueCnt = 0, falseCnt = 0;

        int pixels[] = (int[]) rtnIP.getProcessor().getPixels();

        for (int idx = 0; idx < pixels.length; idx++) {
            pixels[idx] = mask[idx] ? falsePixel : pixels[idx];
            if (mask[idx]) {
                trueCnt++;
            } else {
                falseCnt++;
            }
        }

        rtnIP.getProcessor().setPixels(pixels);
        
        System.out.println("true " + trueCnt + " false " + falseCnt);

        return rtnIP;
    }

    public static final int RGB = 0, HSB = 1, YUV = 2, LAB = 3;

    /**
     * Makes a mask for pixels to include or exclude pixels in ip based upon
     * limits specified for params using specified color space
     *
     * @param ip
     * @param minRed
     * @param maxRed
     * @param minGreen
     * @param maxGreen
     * @param minBlue
     * @param maxBlue
     * @param colorSpace
     * @return
     */
    private boolean[] makeMask(ImagePlus ip, int minRed, int maxRed, int minGreen, int maxGreen, int minBlue, int maxBlue, int colorSpace) {

        int pixels[] = (int[]) ip.getProcessor().getPixels();

        boolean[] mask = new boolean[pixels.length];

        for (int idx = 0; idx < pixels.length; idx++) {
            switch (colorSpace) {
                case RGB:
                    int[] rgb = INTtoRGB(pixels[idx]);
                    mask[idx] = (rgb[RED] >= minRed) && (rgb[RED] <= maxRed) && (rgb[GREEN] >= minGreen) && (rgb[GREEN] <= maxGreen)
                            && (rgb[BLUE] >= minBlue) && (rgb[BLUE] <= maxBlue);
                    break;
                case HSB:
                    float[] hsb = INTtoHSB(pixels[idx]);
                    mask[idx] = (hsb[H] >= minRed) && (hsb[H] <= maxRed) && (hsb[S] >= minGreen) && (hsb[S] <= maxGreen)
                            && (hsb[B] >= minBlue) && (hsb[B] <= maxBlue);
                    break;
                case YUV:
                    float[] yuv = INTtoYUV(pixels[idx]);
//                    System.out.printf("%f %f %f\n", yuv[0], yuv[1], yuv[2]);
                    mask[idx] = (yuv[Y] >= minRed) && (yuv[Y] <= maxRed) && (yuv[U] >= minGreen) && (yuv[U] <= maxGreen)
                            && (yuv[V] >= minBlue) && (yuv[V] <= maxBlue);
                    break;
                case LAB:
                    throw new IllegalArgumentException("LAB colorspace not implemented");
            }
        }
        return mask;
    }

    private static final int RED = 0, GREEN = 1, BLUE = 2;

    /**
     * Split RGB pixel into RGB components
     *
     * @param pixel
     * @return
     */
    private int[] INTtoRGB(int pixel) {

        int[] rgb = new int[3];

        rgb[RED] = ((pixel & 0xff0000) >> 16);//R
        rgb[GREEN] = ((pixel & 0x00ff00) >> 8);//G
        rgb[BLUE] = (pixel & 0x0000ff); //B 

        return rgb;
    }

    private static final int Y = 0, U = 1, V = 2;

    /**
     * Split RGB pixel into YUV components
     *
     * @param pixel
     * @return
     */
    private float[] INTtoYUV(int pixel) {

        int rgb[] = INTtoRGB(pixel);

        float[] yuv = new float[3];

        float yf = (float) (0.299 * rgb[RED] + 0.587 * rgb[GREEN] + 0.114 * rgb[BLUE]);
//        yuv[Y] = (byte) ((int) Math.floor(yf + 0.5));
//        yuv[U] = (byte) (128 + (int) Math.floor((0.493 * (rgb[BLUE] - yf)) + 0.5));
//        yuv[V] = (byte) (128 + (int) Math.floor((0.877 * (rgb[RED] - yf)) + 0.5));
        yuv[Y] = ((int) Math.floor(yf + 0.5));
        yuv[U] = (128 + (int) Math.floor((0.493 * (rgb[BLUE] - yf)) + 0.5));
        yuv[V] = (128 + (int) Math.floor((0.877 * (rgb[RED] - yf)) + 0.5));

        return yuv;
    }

    private static final int H = 0, S = 1, B = 2;

    /**
     * Split RGB pixel into HSB components
     *
     * @param pixel
     * @return
     */
    private float[] INTtoHSB(int pixel) {

        int rgb[] = INTtoRGB(pixel);

        float[] hsb = Color.RGBtoHSB(rgb[RED], rgb[GREEN], rgb[BLUE], null);

        return hsb;
    }

}
