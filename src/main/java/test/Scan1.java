/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author wjrfo
 */

/*
 * Morena 7 - Image Acquisition Framework
 *
 * Copyright (c) 1999-2011 Gnome spol. s r.o. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Gnome spol. s r.o. You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms
 * of the license agreement you entered into with Gnome.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import eu.gnome.morena.Camera;
import eu.gnome.morena.Configuration;
import eu.gnome.morena.Device;
import eu.gnome.morena.Manager;
import eu.gnome.morena.Scanner;
import eu.gnome.morena.twain.TwainScanner;
import eu.gnome.morena.ui.ScannerUIDialog;
import eu.gnome.morena.wia.WIAScanner;
import ij.IJ;
import ij.ImagePlus;
import java.awt.Color;
import javax.swing.JFrame;

/**
 * MorenaExample demonstrates basic use of the Morena Framework in an
 * application environment. Process of scanning is asynchronous and application
 * is provided with the file containing an image.
 *
 * Requirements: 1. Java 1.5 or newer 2. Morena7 for image acquisition
 *
 * Usage: name pattern, batch, number of pages
 *
 */
public class Scan1 {

    static Manager manager;
    static String deviceName;
    static int pages = 0; // scan number of pages in one session (0 - designates scanning until ADF is empty)
    private int resolution = 600;          // pixels per inch
    private double pageWidth = 8.5;       // scan region width
    private double pageHeight = 11.0;     // scan region height
    private int pixelDelta = (20 * resolution) / 75; // used for scanning to search for other side of kernel

    public static void main(String args[]) {
        
        Scan1 example = new Scan1();
        System.err.println("MorenaExample(" + Arrays.toString(args) + ") ... started at " + new Date());
        // Loads native library and initialize logging
        try {
            Configuration.setLogLevel(Level.FINEST);               // setting max log detail
//      Configuration.setLogLevel(Level.ALL);               // setting max log detail
//      Configuration.addDeviceType(".*fficejet.*", true);  // workaround for HP scanners
            manager = Manager.getInstance();
            example.simpleScan();
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            manager.close();
        }
        System.err.println("Finished.");
    }

    /**
     * This example method shows how to scan single image from selected device
     * and default functional unit (flatbed)
     *
     * @throws Exception
     */
    private void simpleScan() throws Exception {
        Device device = selectDevice();

        if (device != null) { // for scanner device set the scanning parameters
            if (device instanceof Scanner) {
                Scanner scanner = (Scanner) device;

                
                WIAScanner wiaScanner = (WIAScanner) device;
                
//                wiaScanner.setMode(WIAScanner.WIA_HANDLING_SELECT_BACK_ONLY);
                List<Integer> list = scanner.getSupportedModes();
                for (int mode : list) {
                    System.out.println("supported modes " + mode);
                }
                System.out.println(Scanner.DUPLEX_AVAIL);
                System.out.println(">>mode " + scanner.getMode());
//                scanner.setMode(Scanner.RGB_8);
                scanner.setMode(-8);
                System.out.println(">>selected resolution " + scanner.getResolution());
//                System.out.println(">>supported resolutions " + scanner.getSupportedResolutions());
                scanner.setResolution(getResolution());
                wiaScanner.displayProperties();
//        scanner.setResolution(75);
                int pixelHeight = (int) (getPageHeight() * getResolution());
                int pixelWidth = (int) (getPageWidth() * getResolution());
                scanner.setFrame(0, 0, pixelWidth, pixelHeight);   // A4 8.3 x 11.7 ( 622 x 877 at 75 DPI) (for Lide110 - 622 x 874)
//        scanner.setFrame(0, 0, 622*4*4, 874*4*4);   // A4 8.3 x 11.7 ( 622 x 877 at 75 DPI) (for Lide110 - 622 x 874)

            } else if (device instanceof Camera) {
                // Camera specific settings
            }

            // start scan using default (0) functional unit
            BufferedImage bimage = SynchronousHelper.scanImage(device);
            System.err.println("scanned image info: size=(" + bimage.getWidth() + ", " + bimage.getHeight() + ")   bit mode=" + bimage.getColorModel().getPixelSize());
//        ImageOutputStream ios;
//        ios = ImageIO.createImageOutputStream(bimage);
//        FileOutputStream fos = new FileOutputStream((File) ios);
//        File of = new File()

            System.out.println(new File(System.getProperty("user.dir"), "data"));
            File dirF = new File(System.getProperty("user.dir"), "data");
            String fileName = "scanned.jpg";
//            String fileName = "" + System.currentTimeMillis() + ".jpg";
            File outF = new File(dirF, fileName);
            System.out.println(outF.getAbsolutePath());

            ImageIO.write(bimage, "jpg", outF);
//            // do image processing if necessary
//            // ...
//            //Thread.sleep(30000);
//            ImagePlus ip0 = IJ.openImage(outF.getAbsolutePath());
//            ip0.show();
//            ImagePlus ip1 = ip0.duplicate();
//            IJ.run(ip1, "8-bit", "");
////            ip1.show("8-bit");
//
//            int hist[] = makeHist(ip1);
//
//            int row = 0;
//            for (int histV : hist) {
//                System.out.printf("%3d %5d\n", row++, histV);
//            }
//
//            ImagePlus ip2 = ip1.duplicate();
////            ip2.show("ip2");
//
//            ImagePlus maskIP = maskHist(ip2, hist, 110, 255);
//            maskIP = maskHist(maskIP, hist, 0, 80);
//            maskIP.show();
//
//            ImagePlus colorIP = maskBlue(ip0);
//            colorIP.show();
//            ImagePlus maskIP2 = maskHist(colorIP, hist, 120, 255);
//            maskIP2 = maskHist(maskIP2, hist, 0, 80);
//            maskIP2.setTitle("test");
//            maskIP2.show();
//
//            ImagePlus fillIP = fillIn(maskIP2);
//            fillIP.setTitle("fill in");
//            fillIP.show();
//
//            ImagePlus mergedIP = merge(ip0, fillIP);
//            mergedIP.setTitle("fill in");
//            mergedIP.show();
//
//            fileName = "processed.jpg";
////            String fileName = "" + System.currentTimeMillis() + ".jpg";
//            outF = new File(dirF, fileName);
//            System.out.println(outF.getAbsolutePath());
//
//            ImageIO.write(mergedIP.getBufferedImage(), "jpg", outF);
        }
    }

    private ImagePlus merge(ImagePlus baseIP, ImagePlus maskIP) {

        ImagePlus mergedIP = baseIP.duplicate();

        int maxWidth = baseIP.getWidth();
        int maxHeight = baseIP.getHeight();

        for (int iW = 0; iW < maxWidth; iW++) {
            for (int iH = 0; iH < maxHeight; iH++) {
                int[] pixVal = maskIP.getPixel(iW, iH);
                if (pixVal[0] == 0 && pixVal[2] == 0) {
                    mergedIP.getProcessor().putPixel(iW, iH, pixVal);
                }

            }

        }
        return mergedIP;
    }

    private int[] makeHist(ImagePlus ip) {

        int hist[] = new int[256];

        int maxWidth = ip.getWidth();
        int maxHeight = ip.getHeight();

        for (int iW = 0; iW < maxWidth; iW++) {
            for (int iH = 0; iH < maxHeight; iH++) {
                int pixVal = ip.getPixel(iW, iH)[0];
                hist[pixVal]++;
            }
        }

        return hist;
    }

    private ImagePlus fillIn(ImagePlus ip) {

        ImagePlus fillIP = ip.duplicate();

        int maxWidth = ip.getWidth();
        int maxHeight = ip.getHeight();

        for (int iW = 0; iW < maxWidth - 20 * pixelDelta; iW++) {
            for (int iH = 1; iH < maxHeight; iH++) {
                int[] pixValLag = {0, 0, 0, 0};            //set up lag value to pixel not displayed
                int[] pixVal = ip.getPixel(iW, iH);
                if (pixValLag[0] > 0 || pixValLag[2] > 0) { // previous pixel not blank so skip to next pixel
                    pixValLag = pixVal;
                    continue;
                }
                pixValLag = pixVal;
                if (pixVal[0] > 0 || pixVal[2] > 0) {
                    for (int idx = 1; idx < pixelDelta; idx++) {
                        int[] pixVal2 = ip.getPixel(iW + idx, iH);
                        if (pixVal2[0] > 0 || pixVal2[2] > 0) {
                            for (int jdx = 0; jdx < idx; jdx++) {
                                pixVal2[0] = 255;
                                pixVal2[1] = 255;
                                pixVal2[3] = 255;
                                pixVal2[2] = 0;
                                fillIP.getProcessor().putPixel(iW + jdx, iH, pixVal2);

                            }
                        }
                    }
                }
            }
        }

        for (int iW = 0; iW < maxWidth; iW++) {
            for (int iH = 1; iH < maxHeight - 20 * pixelDelta; iH++) { // skipping the last 20 since there won't be any kernels there
                int[] pixValLag = {0, 0, 0, 0};            //set up lag value to pixel not displayed
                int[] pixVal = ip.getPixel(iW, iH);
                if (pixValLag[0] > 0 || pixValLag[2] > 0) { // previous pixel not blank so skip to next pixel
                    pixValLag = pixVal;
                    continue;
                }
                pixValLag = pixVal;
                if (pixVal[0] > 0 || pixVal[2] > 0) {
                    for (int idx = 1; idx < pixelDelta; idx++) {
                        int[] pixVal2 = ip.getPixel(iW, iH + idx);
                        if (pixVal2[0] > 0 || pixVal2[2] > 0) {
                            for (int jdx = 0; jdx < idx; jdx++) {
                                pixVal2[0] = 255;
                                pixVal2[1] = 255;
                                pixVal2[3] = 255;
                                pixVal2[2] = 0;
                                fillIP.getProcessor().putPixel(iW, iH + jdx, pixVal2);

                            }
                        }
                    }
                }
            }
        }

        return fillIP;
    }

    private ImagePlus maskBlue(ImagePlus inpIP) {

        ImagePlus outIP;

        outIP = inpIP.duplicate();

        int maxWidth = inpIP.getWidth();
        int maxHeight = inpIP.getHeight();

        outIP.setColor(Color.BLACK);
        for (int iW = 0; iW < maxWidth; iW++) {
            for (int iH = 0; iH < maxHeight; iH++) {
                int[] pixVal = inpIP.getPixel(iW, iH);
//               pixVal[0] = 0;
                pixVal[1] = 0;
//               pixVal[3] = 0;
                pixVal[2] = 0;
                outIP.getProcessor().putPixel(iW, iH, pixVal);

            }
        }
        return outIP;
    }

    private ImagePlus maskHist(ImagePlus inpIP, int[] hist, int low, int high) {
        ImagePlus outIP = inpIP.duplicate();

        if (low < 0 || low > 255 || high < 0 || high > 255 || high < low) {
            throw new IllegalArgumentException("high or low out of range or high < low");
        }

        int maxWidth = inpIP.getWidth();
        int maxHeight = inpIP.getHeight();

        outIP.setColor(Color.BLACK);
        for (int iW = 0; iW < maxWidth; iW++) {
            for (int iH = 0; iH < maxHeight; iH++) {
                int[] pixVal = inpIP.getPixel(iW, iH);
                if (pixVal[0] >= low && pixVal[0] <= high) {
                    pixVal[0] = 0;
                }
                outIP.getProcessor().putPixel(iW, iH, pixVal);

            }
        }
        return outIP;
    }

    /**
     * This example method shows how to scan multiple images from selected
     * device with ADF (automatic document feeder). If ADF unit is not found or
     * recognized the default unit (0) is used. Scanned image files are
     * converted to jpeg format and placed in temporary directory
     * (System.getProperty("java.io.tmpdir"))
     *
     * @throws Exception
     */
    // Selecting a device (1st device available is selected if deviceName not specified)
    private static Device selectDevice() {
        List<? extends Device> devices = manager.listDevices();
        Device device = null;
        if (devices.size() > 0) {
            if (deviceName != null) // search for device name match
            {
                for (Device dev : devices) {
                    System.err.println("connected device " + dev);
                    if (dev.toString().startsWith(deviceName)) {
                        device = dev;
                    }
                }
            } else // select first device
            {
                device = devices.get(1); // changed from 0 to 1
            }
        } else {
            System.out.println("No device connected!!!");
        }
        System.err.println("device selected = " + device);
        return device;
    }

    private void test() {
        WIAScanner scanner = null;
        scanner.setMode(WIAScanner.WIA_CATEGORY_FILM);
    }

    /**
     * @return the resolution
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * @param resolution the resolution to set
     */
    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    /**
     * @return the pageWidth
     */
    public double getPageWidth() {
        return pageWidth;
    }

    /**
     * @param pageWidth the pageWidth to set
     */
    public void setPageWidth(double pageWidth) {
        this.pageWidth = pageWidth;
    }

    /**
     * @return the pageHeight
     */
    public double getPageHeight() {
        return pageHeight;
    }

    /**
     * @param pageHeight the pageHeight to set
     */
    public void setPageHeight(double pageHeight) {
        this.pageHeight = pageHeight;
    }

}
