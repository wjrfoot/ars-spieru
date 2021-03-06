/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.controller;

import gov.usda.ars.spieru.chalk.controller.*;
import gov.usda.ars.spieru.chalk.model.Config;
import gov.usda.ars.spieru.chalk.util.FindLastPictureFile;
import gov.usda.ars.spieru.chalk.view.SubImagePlusFrame;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.ResultsTable;
import ij.plugin.filter.Analyzer;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.text.DecimalFormat;
import gov.usda.ars.spieru.chalk.view.ResultsBarChart;

/**
 *
 * @author wjrfo
 */
public class AnalyzeScan {

    /**
     * subImagePlusList is the main data object. The object contains a list of
     * SubImagePlus instances. The SubImagePlus object contains an ImagePlus
     * instance holds the image and meta-data of an individual kernel, the
     * bounding box for where the individual kernel is located in the input
     * scan, and the result strings from the kernel and chalk analyze particles
     * runs.
     *
     */
    private final List<SubImagePlus> subImagePlusList = new ArrayList<>();
    private int lowTH = 60;
    private int hiTH = 185;
    private String fileName = null;
    private Config config = null;

    public static void main(String[] args) {
        System.out.println("starting analyze");
        AnalyzeScan analyze = new AnalyzeScan();
        analyze.run();
    }

    /**
     * uses the last created file in picture directory
     */
    public AnalyzeScan() {
        File dirF = new File(System.getProperty("user.dir"), "data");
        dirF = new File(dirF, "img042.jpg");
        setFileName(dirF.getAbsolutePath());
        setConfig(new Config());
//        setFileName(FindLastPictureFile.getLastFileName());
    }

    /**
     * uses specified file
     *
     * @param fileName
     */
    public AnalyzeScan(String fileName, Config config) {
        setFileName(fileName);
        setConfig(config);

    }

    private class MyMouseListner implements MouseListener {

        private int canvasHeight;
        private int canvasWidth;
        private int imageHeight;
        private int imageWidth;

        public MyMouseListner(ImagePlus ip) {
            canvasHeight = ip.getCanvas().getHeight();
            canvasWidth = ip.getCanvas().getWidth();
            imageHeight = ip.getHeight();
            imageWidth = ip.getWidth();
        }

        @Override
        public void mouseClicked(MouseEvent arg0) {

            int x = arg0.getX();
            int y = arg0.getY();
            System.out.println("mouseClicked " + arg0.getY() + " " + arg0.getX());
            System.out.println("canvas H " + canvasHeight + "  W  " + canvasWidth);
            System.out.println("image H " + imageHeight + "  W  " + imageWidth);
            int x1 = (x * imageWidth) / canvasWidth;
            int y1 = (y * imageHeight) / canvasHeight;
            System.out.println("scaled H " + y1 + "  W  " + x1);
            Point point = new Point(x1, y1);

            for (SubImagePlus sip : subImagePlusList) {
                Rectangle rect = sip.getBoundingBox();
                System.out.println(rect.toString());
//                processResults(sip, null);
                if (rect.contains(point)) {
                    processResults(sip, null);
                    System.out.println("found");
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent arg0) {
            System.out.println("mousePressed");
        }

        @Override
        public void mouseReleased(MouseEvent arg0) {
            System.out.println("mouseReleased");
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {
            System.out.println("mouseEntered");
        }

        @Override
        public void mouseExited(MouseEvent arg0) {
            System.out.println("mouseExited");
        }

    }

    /**
     * entry point to run analysis
     */
    public void run() {

        ImagePlus ip0 = IJ.openImage(getFileName());

        ImagePlus ip1 = ip0.duplicate();
        ip1.getProcessor().flipHorizontal();
        ip1.show();
        ip1.getCanvas().addMouseListener(new MyMouseListner(ip1));

        System.out.println(ip1.getHeight() + " " + ip1.getCanvas().getHeight());

        ImagePlus ip2 = analyze(ip1);
        ip2.show();
    }

    /**
     * perform chalk analysis
     *
     * @param baseIP
     * @return
     */
    private ImagePlus analyze(ImagePlus baseIP) {

        ImagePlus ip = baseIP.duplicate();
//        IJ.run(ip, "Set Measurements...",
//                "area centroid perimeter fit shape redirect=None decimal=2 bounding rectangle");
        IJ.run(ip, "Set Measurements...", getConfig().getMeasureParamsBase());
        IJ.run(ip, "8-bit", "");
        ip.getProcessor().setAutoThreshold("Default Dark");
        ip.getProcessor().setThreshold(getConfig().getLowThresholdKernel(hiTH),
                getConfig().getHiThresholdKernel(hiTH), 0);
        ResultsTable.getResultsTable().reset();
        IJ.run(ip, "Analyze Particles...", getConfig().getAnalyzeBase());
//        IJ.run(ip, "Analyze Particles...", "size=100-10000 circularity=0.1-1.00 bounding rectanble");
        dumpResultsTable(ResultsTable.getResultsTable());
//        ResultsTable.getResultsTable().reset();;
//        IJ.run(ip, "Find Edges", "");
        ip.setTitle("analyze particles");
        ip.show();

        ResultsTable rs1 = ResultsTable.getResultsTable();

//        System.out.println(rs1.getColumnHeadings());
//        ip.setColor(Color.BLUE);
        ip.setTitle("xyz");
        ip.show();

        List<Rectangle> roiRects = findROIs(rs1);

        setROIs(ip.duplicate(), roiRects);

        process();

        return ip;
    }

    /**
     * processes each kernel object for full kernel and chalk areas
     */
    private void process() {

        PrintWriter pw = null;
        try {
            File dirF = new File(System.getProperty("user.dir"), "data");
            String fileName = System.currentTimeMillis() + ".txt";
            File outF = new File(dirF, fileName);
            pw = new PrintWriter(outF);
            //

            System.out.println("process");
            for (SubImagePlus sip : subImagePlusList) {

                processKernel(sip);

                processChalk(sip);

//                processResults(sip, pw);
            }
            System.out.println("done process");
        } catch (IOException ex) {
            Logger.getLogger(AnalyzeScan.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }

        EventQueue.invokeLater(() -> {

            ResultsBarChart ex = new ResultsBarChart(subImagePlusList);
            ex.setVisible(true);
        });

    }

    /**
     * this should be greatly expanded to analyze the results instead of just
     * printing them to a text file
     *
     * @param sip
     * @param pw
     */
    private void processResults(SubImagePlus sip, PrintWriter pw) {
        if (pw != null) {
            pw.println(sip.getKernelResults().split("\\t")[1] + "  ---  " + sip.getChalkResults().split("\\t")[1]);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            SubImagePlusFrame sipf = new SubImagePlusFrame();
            sipf.getChalkJTF().setText(sip.getChalkResults().split("\\t")[1]);
            sipf.getKernelJTF().setText(sip.getKernelResults().split("\\t")[1]);
            sipf.getKernelPitJL().setIcon(new ImageIcon(sip.getKernelIP().getImage()));
            sipf.getChalkPictJL().setIcon(new ImageIcon(sip.getChalkIP().getImage()));
            sipf.getOriginalPitJL().setIcon(new ImageIcon(sip.getOriginalIP().getImage()));
            double kernel = Double.parseDouble(sip.getKernelResults().split("\\t")[1]);
            double chalk = Double.parseDouble(sip.getChalkResults().split("\\t")[1]);
            String temp = (kernel == chalk) ? "invalid"
                    : new DecimalFormat("#.0#").format(chalk / kernel) + "%"; // rounded to 2 decimal places
            sipf.getPercentJTF().setText(temp);
            sipf.setVisible(true);
        });

    }

    /**
     * goes thru the results table and save the bounding box for each kernel for
     * later processing. note that touching kernels may wind up in one bounding
     * box.
     *
     * @param rs1
     * @return
     */
    private List<Rectangle> findROIs(ResultsTable rs1) {

        ArrayList<Rectangle> roiRects = new ArrayList<>();

        for (int idx = 0; idx < rs1.getCounter(); idx++) {

            int x = (int) rs1.getValue("BX", idx);
            int y = (int) rs1.getValue("BY", idx);
            int width = (int) rs1.getValue("Width", idx);
            int height = (int) rs1.getValue("Height", idx);
            Rectangle inpRect = new Rectangle(x, y, width, height);
            roiRects.add(inpRect);
        }
        return roiRects;
    }

    private void dumpResultsTable(ResultsTable rs) {
        System.out.println("Results Table " + rs.getCounter());
        System.out.println(rs.getColumnHeadings());
        for (int idx = 0; idx < rs.getCounter(); idx++) {
            System.out.println(rs.getRowAsString(idx));

        }
    }

    /**
     * takes the bounding box for each kernel found in analyze particles and
     * splits each kernel into a separate, fairly small, image object
     *
     * @param bIP
     * @param roiList
     */
    private void setROIs(ImagePlus bIP, List<Rectangle> roiList) {

        int cnt = 0;

        for (Rectangle roi : roiList) {
            ImagePlus tIP = bIP.duplicate();
            tIP.setRoi(roi);

            ImagePlus qIP = new ImagePlus("temp", tIP.getProcessor().crop());
            qIP.setTitle("kernel " + cnt++);
//            qIP.show();
            SubImagePlus sip = new SubImagePlus();
            sip.setBoundingBox(roi);
            sip.setKernelIP(qIP);
            writeIP(qIP);
            subImagePlusList.add(sip);
        }
    }

    /**
     * copies individual kernel onto larger canvas so that analyze particles
     * will work (if the kernel occupies most of the canvas then AP doesn't
     * recognize it as a particle. therefore, the kernel image is put onto a
     * much larger canvas and AP seems to work).
     *
     * @param inpIP
     * @return
     */
    private ImagePlus enlarge(ImagePlus inpIP) {

        ImagePlus imp2 = IJ.createImage("blank", 100, 100, 1, 8);
//        imp2.show();
        ImagePlus imp3 = imp2.duplicate();
        imp3.getProcessor().copyBits(inpIP.getProcessor(), 10, 10, 0);
//        imp3.show();
        return imp3;

    }

    /**
     * process full kernel
     *
     * @param sip
     */
    private void processKernel(SubImagePlus sip) {

        ImagePlus ip = enlarge(sip.getKernelIP().duplicate());
        sip.setOriginalIP(ip.duplicate());

//	// analyze particles
//      run("Set Measurements...",
//	"area centroid perimeter fit shape redirect=None decimal=2");
        IJ.run(ip, "Set Measurements...", getConfig().getMeasureParamsBase());

//	// set the threshold
//	run("8-bit");
        IJ.run(ip, "8-bit", "");

//	setAutoThreshold("Default dark");
        ip.getProcessor().setAutoThreshold("Default Dark");

//	setThreshold(lowTH, 255);
        ip.getProcessor().setThreshold(getConfig().getLowThresholdKernel(hiTH),
                getConfig().getHiThresholdKernel(hiTH), 0);

//	// analyze particles
//	run("Analyze Particles...",
//	"size=minSz1-30 circularity=0.1-1.00" + 
//	" show=[Overlay Masks] display");
        // use different size since we are working in pixels instead of mm
        IJ.run(ip, "Analyze Particles...", getConfig().getAnalyzeKernel());
//        Analyzer analyzer = new Analyzer();
//        analyzer.setup("size=10-30000 circularity=0.1-1.00", ip);
//        analyzer.run(ip.getProcessor());

        // save last results, full, table entry into kernel object
        String resultsStr = ResultsTable.getResultsTable().getRowAsString(ResultsTable.getResultsTable().getCounter() - 1);
        sip.setKernelResults(resultsStr);
        sip.setKernelIP(ip);
    }

    /**
     * processes chalk portion of kernel
     *
     * @param sip
     */
    private void processChalk(SubImagePlus sip) {

        ImagePlus ip = enlarge(sip.getKernelIP().duplicate());

//	// process the duplicate for chalk
//	run("Set Measurements...",
//	"area centroid perimeter fit shape redirect=None decimal=2");
        IJ.run(ip, "Set Measurements...", getConfig().getMeasureParamsBase());

        // try to smooth image and trim tips
//	run("Subtract Background...", "rolling=5 create");
        IJ.run(ip, "Subtract Background...", "rolling=5 create");

//	// set the threshold for the chalk
//	run("8-bit");
        IJ.run(ip, "8-bit", "");

//	setAutoThreshold("Default dark");
        ip.getProcessor().setAutoThreshold("Default Dark");

//	setThreshold(lowTH, 255);
        ip.getProcessor().setThreshold(getHiTH(), 255, 0);

//	run("Analyze Particles...",
//	"size=minSz1-30 circularity=0.1-1.00" + 
//	" show=[Overlay Masks] display");
        // changed size because measurements are pixels instead of mm
        IJ.run(ip, "Analyze Particles...", "size=10-30000 circularity=0.1-1.00");

        // save last results, chalk< table entry into kernel object
        String resultsStr = ResultsTable.getResultsTable().getRowAsString(ResultsTable.getResultsTable().getCounter() - 1);
        sip.setChalkResults(resultsStr);
        sip.setChalkIP(ip);
    }

    /**
     * writes the image of one kernel into file for later viewing
     *
     * @param ip
     */
    private void writeIP(ImagePlus ip) {
//             System.out.println(new File(System.getProperty("user.dir"), "data"));
        File dirF = new File(System.getProperty("user.dir"), "data\\roiTest");
        if (!dirF.exists()) {
            dirF.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
//        fileName = "fred" + ".jpg";
        File outF = new File(dirF, fileName);
//        System.out.println(outF.getAbsolutePath());

        try {
            ImageIO.write(ip.getBufferedImage(), "jpg", outF);
        } catch (IOException ex) {
            Logger.getLogger(AnalyzeScan.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    /**
     * @return the lowTH
     */
    public int getLowTH() {
        return lowTH;
    }

    /**
     * @param lowTH the lowTH to set
     */
    public void setLowTH(int lowTH) {
        this.lowTH = lowTH;
    }

    /**
     * @return the hiTH
     */
    public int getHiTH() {
        return hiTH;
    }

    /**
     * @param hiTH the hiTH to set
     */
    public void setHiTH(int hiTH) {
        this.hiTH = hiTH;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public final void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * @return the config
     */
    public Config getConfig() {
        return config;
    }

//</editor-fold>
}
