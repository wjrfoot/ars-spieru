/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import imagingbook.pub.corners.Corner;
import imagingbook.pub.corners.HarrisCornerDetector;
import java.awt.Color;
import java.util.List;

/**
 *
 * @author wjrfo
 */
public class Find_Corners implements PlugInFilter {

    @Override
    public int setup(String string, ImagePlus ip) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    HarrisCornerDetector.Parameters parms = new HarrisCornerDetector.Parameters();
   
            
    @Override
    public void run(ImageProcessor ip) {
        HarrisCornerDetector cd = new HarrisCornerDetector(ip, new HarrisCornerDetector.Parameters());   
 //       HarrisCornerDetector cd = new HarrisCornerDetector(ip, parms);   
        List<Corner> corners = cd.getCorners();
        drawCorners(ip, corners);
        ColorProcessor R = (ColorProcessor) ip.convertToRGB();
//        ImageProcessor R = ip.convertToRGB();
        new ImagePlus("Result", R).show();
                }

    void drawCorners(ImageProcessor ip, List<Corner> corners) {
        Color cornerColor = Color.RED;
        ip.setColor(cornerColor);
        for (Corner corner : corners) {
            drawCorner(ip, corner);
        }
    }
    
    void drawCorner(ImageProcessor ip, Corner corner) {
        int cornerSize = 10;
        int size = cornerSize;
        int x = (int) Math.round(corner.getX());
        int y = (int) Math.round(corner.getY());
        ip.drawLine(x-size, y, x+size, y);
        ip.drawLine(x, y-size, x, y+size);
        
    }
}
