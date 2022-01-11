/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author wjrfo
 */
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;
import gov.usda.ars.spieru.chalk.util.FindLastPictureFile;


public class My_Inverter_A implements PlugInFilter {
    
    public static void main(String[] args) {
        My_Inverter_A mia = new My_Inverter_A();
        String fileName = FindLastPictureFile.getLastFileName();
        ImagePlus ip = new ImagePlus(fileName);
        ip.show();
        mia.run(ip.getProcessor());
        ip.show();;
    }

    @Override
    public int setup(String args, ImagePlus im) {
        return DOES_8G; // this plugin accepts 8-bit grayscale images
    }

    @Override
    public void run(ImageProcessor ip) {
        int M = ip.getWidth();
        int N = ip.getHeight();

        // iterate over all image coordinates (u,v)
        for (int u = 0; u < M; u++) {
            for (int v = 0; v < N; v++) {
                int p = ip.getPixel(u, v);
                ip.putPixel(u, v, 255 - p);
            }
        }
    }
}
