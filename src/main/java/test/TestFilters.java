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
import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageProcessor;
import imagingbook.pub.corners.Corner;
import java.awt.Color;

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
public class TestFilters {

    static Manager manager;
    static String deviceName;
    static int pages = 0; // scan number of pages in one session (0 - designates scanning until ADF is empty)
    private int resolution = 600;          // pixels per inch
    private double pageWidth = 8.5;       // scan region width
    private double pageHeight = 11.0;     // scan region height
    private int pixelDelta = (20 * resolution) / 75; // used for scanning to search for other side of kernel

    public static void main(String args[]) {
        TestFilters example = new TestFilters();
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
        FindLastPictureFile flpf = new FindLastPictureFile();
        // do image processing if necessary
        // ...
        //Thread.sleep(30000);
        ImagePlus ip0 = IJ.openImage(flpf.getLastFileName());
        ip0.show();
        ImagePlus ip1 = ip0.duplicate();
        IJ.run(ip1, "8-bit", "");
//            ip1.show("8-bit");

        int hist[] = makeHist(ip1);

        int row = 0;
        for (int histV : hist) {
            System.out.printf("%3d %5d\n", row++, histV);
        }

        ImagePlus ip2 = ip1.duplicate();
        makeBack(ip2);
        ip2.show("ip2");

        ImagePlus ip3 = ip0.duplicate();
        addBoxes(ip3);
        ip3.show("ip2");

    }

    private ImagePlus makeBack(ImagePlus ip) {
        int maxWidth = ip.getWidth();
        int maxHeight = ip.getHeight();

        for (int iW = 0; iW < maxWidth; iW++) {
            for (int iH = 0; iH < maxHeight; iH++) {
                int[] pixVal = ip.getPixel(iW, iH);
                if (pixVal[0] == 0) {
                    pixVal[0] = 127;
                    pixVal[1] = 127;
                    pixVal[2] = 127;
                    pixVal[3] = 127;
                    ip.getProcessor().putPixel(iW, iH, pixVal);
                }
            }
        }
        return ip;
    }

    private ImagePlus addBoxes(ImagePlus ip) {
        int maxWidth = ip.getWidth();
        int maxHeight = ip.getHeight();

        Color color = Color.RED;
        ip.getProcessor().setColor(color);
        for (int iW = 0; iW < maxWidth; iW++) {
            for (int iH = 0; iH < maxHeight; iH++) {
                int[] pixVal = ip.getPixel(iW, iH);
                pixVal[0] = 0;
                pixVal[1] = 255;
                pixVal[2] = 0;
                pixVal[3] = 0;
                ip.getProcessor().putPixel(iW, iH, pixVal);
            }
        }
            Color cornerColor = Color.RED;
            ip.setColor(cornerColor);
            for (int idx = 0; idx < 10; idx++) {

//                ip.getProcessor().fillOval(10 + idx * 200, 20 + idx * 200, 2000, 200);
ip.getProcessor().setLineWidth(20);
ip.getProcessor().lineTo(300, 600);

//ip.getProcessor().drawLine(0, 0, 1500, 3000);
//                ip.draw(0, 0, 4000, 4000);
//        ip.drawLine(x, y-size, x, y+size);
            }
            return ip;
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
