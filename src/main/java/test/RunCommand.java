/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import static ij.plugin.filter.PlugInFilter.DOES_ALL;
import ij.process.ImageProcessor;

/**
 *
 * @author wjrfo
 */
public class RunCommand implements
        PlugInFilter {

    ImagePlus im;
    String args;

    @Override
    public int setup(String args, ImagePlus im) {
        this.im = im;
        this.args = args;
        return DOES_ALL;
    }

    @Override
    public void run(ImageProcessor ip) {
        im.unlock(); // unlock im to run other commands
        IJ.run(im, "8-bit", ""); // run “Invert” command on im
//        IJ.run(im, "Analyze Particles...", "size=0.5-100 circularity=0.1-1.00 show=Outlines display"); 
        im.lock(); // lock im again (to be safe)
        // ... continue with this plugin
    }
}
