/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.controller;

import gov.usda.ars.spieru.chalk.util.FindLastPictureFile;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.measure.ResultsTable;
import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wjrfo
 */
public class Analyze {

    public static void main(String[] args) {
        System.out.println("starting analyze");
        String filNam = FindLastPictureFile.getLastFileName();
        Analyze analyze = new Analyze(filNam);
    }

    public Analyze(String fileName) {
        ImagePlus ip0 = IJ.openImage(fileName);

        ip0.show();
        ImagePlus ip1 = ip0.duplicate();
        ip1.getProcessor().flipHorizontal();
        ip1.show();

        ImagePlus ip2 = test(ip1);
        ip2.show();
//        ip0.show();
//        ImagePlus ip1 = doIP(ip0, "lowTH", 60, "4");
//        delay();
//        ResultsTable rs1 = ResultsTable.getResultsTable();
//        rs1.show("rs1");
//        System.out.println("rs1 " + rs1);
//        System.out.println("hello there");
//        System.out.println("rs1 " + rs1);
//        delay();    
//
//        ImagePlus ip2 = doIP(ip0, "hiTH", 185, "0.5");
//        ip2.show();
//
//        ResultsTable rs2 = ResultsTable.getResultsTable();
//        rs2.show("rs2");
//        delay();
//        System.out.println("rs2 " + rs2);
//        delay();
    }

    private void delay() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Analyze.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private ImagePlus doIP(ImagePlus baseIP, String title, double threshold, String size) {
        ImagePlus ip = baseIP.duplicate();
        IJ.run(ip, "8-bit", "");
        ip.setTitle(title);
//        ip.show("8-bit");
        ip.getProcessor().setAutoThreshold("Default Dark");
        ip.getProcessor().setThreshold(threshold, 255, 0);
        IJ.run(ip, "Analyze Particles...", "size=minSz1-30 circularity=0.1-1.00 show=Outlines display");

        ip.show();

        return ip;
    }

    private ImagePlus test(ImagePlus baseIP) {
        int lowTH = 60;

        ImagePlus ip = baseIP.duplicate();
        IJ.run(ip, "Set Measurements...",
                "area centroid perimeter fit shape redirect=None decimal=2");
        IJ.run(ip, "8-bit", "");
        ip.getProcessor().setAutoThreshold("Default Dark");
        ip.getProcessor().setThreshold(lowTH, 255, 0);
        ResultsTable.getResultsTable().reset();
        IJ.run(ip, "Analyze Particles...", "size=minSz1-30 circularity=0.1-1.00 bounding rectanble");

        ResultsTable rs1 = ResultsTable.getResultsTable();

        System.out.println(rs1.getColumnHeadings());

        int xPoints[] = {100, 100, 200, 200};
        int yPoints[] = {100, 200, 200, 100};

        int xS[] = new int[rs1.getCounter()];
        int yS[] = new int[rs1.getCounter()];

        for (int idx = 0; idx < rs1.getCounter(); idx++) {
            xS[idx] = (int) rs1.getValue("X", idx);
            yS[idx] = (int) rs1.getValue("Y", idx);
        }        
        ip.setColor(Color.BLUE);
        
        ImagePlus bIP = ip;
        
        for (int idx = 0; idx < rs1.getCounter(); idx++) {

            xPoints[0] = xS[idx];
            xPoints[1] = xS[idx];
            xPoints[2] = xS[idx] + 200;
            xPoints[3] = xS[idx] + 200;

            yPoints[0] = yS[idx];
            yPoints[1] = yS[idx] + 200;
            yPoints[2] = yS[idx] + 200;
            yPoints[3] = yS[idx];

            Roi roi = new PolygonRoi(xPoints, yPoints, xPoints.length, Roi.POLYGON);
            ip.setRoi(roi);
            ip.show();
            ip = bIP.duplicate();
            System.out.println(xS[idx] + "   " + yS[idx]);
//            System.out.println(rs1.getRowAsString(idx));

        }

//        int xPoints[] = {100, 100, 200, 200};
//        int yPoints[] = {100, 200, 200, 100};
//
//        Roi roi = new PolygonRoi(xPoints, yPoints, xPoints.length, Roi.POLYGON);
//        ip.setRoi(roi);
//        ip.show();
//   
        return ip;
    }

    private ImagePlus processKernel(ImagePlus baseIP) {

        int lowTH = 60;

        ImagePlus ip = baseIP.duplicate();
//    function processKernel(X,Y,W,H,windowPattern,shouldWait,fileVar){
//	// make a selection and process the kernel
//
//	// make our selection, multiplying in order to convert to pixels from mm
//	makeRectangle(X * 11.5, Y * 11.5,
//	W * 11.5, H * 11.5);
//	if(shouldWait){
//		showMessageWithCancel("Action Required",
//		"Selection Made");
//	}//end if we should wait
//	// make a copy to work with
//	// get a little debugging info first
//	
//	run("Duplicate...", "title=[" + windowPattern + "]");
//	if(shouldWait){
//		showMessageWithCancel("Action Required",
//		"Selection Duplicated");
//	}//end if we should wait
//	// take a snapshot so we can reset later
//	makeBackup("Dup");
//	// set which things should be measured
//	run("Set Measurements...",
//	"area centroid perimeter fit shape redirect=None decimal=2");
        IJ.run(ip, "Set Measurements...",
                "area centroid perimeter fit shape redirect=None decimal=2");
//	// set the threshold
//	run("8-bit");
        IJ.run(ip, "8-bit", "");
//	setAutoThreshold("Default dark");
        ip.getProcessor().setAutoThreshold("Default Dark");
//	setThreshold(lowTH, 255);
        ip.getProcessor().setThreshold(lowTH, 255, 0);
//	if(shouldWait){
//		showMessageWithCancel("Action Required",
//		"Kernel Threshold Set");
//	}//end if we should wait
//	resultsBefore = nResults;
//	// analyze particles
//	run("Analyze Particles...",
//	"size=minSz1-30 circularity=0.1-1.00" + 
//	" show=[Overlay Masks] display");

        ResultsTable.getResultsTable().reset();
        IJ.run(ip, "Analyze Particles...", "size=minSz1-30 circularity=0.1-1.00 bounding rectanble");

        ResultsTable rs1 = ResultsTable.getResultsTable();

        System.out.println(rs1.getColumnHeadings());
        for (int idx = 0; idx < rs1.getCounter(); idx++) {
            System.out.println(rs1.getRowAsString(idx));
        }

        int xPoints[] = {100, 100, 200, 200};
        int yPoints[] = {100, 200, 200, 100};

        Roi roi = new PolygonRoi(xPoints, yPoints, xPoints.length, Roi.POLYGON);
        ip.setRoi(roi);
        ip.show();
//	if(shouldWait){
//		showMessageWithCancel("Action Required",
//		"Kernel Particles Analyzed");
//	}//end if we should wait
//	resultsAfter = nResults;
//	if(resultsAfter - resultsBefore > 0){
//		// print kernel results over the log
//		kernelStuff = getAllResults(columns);
//		kernelForLog = newArray(lengthOf(columns));
//		kCount = lengthOf(kernelForLog);
//		for(j = 0; j < kCount; j++){
//			kernelForLog[j] = twoDArrayGet(kernelStuff,
//			lengthOf(kernelStuff)/lengthOf(columns),
//			lengthOf(columns), nResults-1, j);
//		}//end looping over all the columns of this line
//		currentLine++;
//		print(currentLine + "     " + dATS(1, kernelForLog, "     "));
//		if(fileVar != false){
//			print(fileVar, currentLine + "\t" + dATS(1, kernelForLog, "\t"));
//		}//end if we're doing a file
//		if(shouldWait){
//			showMessageWithCancel("Action Required",
//			"Kernel Results Printed");
//		}//end if we should wait
//	}//end if we detected a kernel
//}//end processKernel(x,y,width,height)
        return ip;
    }

}
