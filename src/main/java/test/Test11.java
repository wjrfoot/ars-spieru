/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import ij.IJ;
import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;

/**
 *
 * @author wjrfo
 */
public class Test11 {

    private static final String FILENAME = "./data/bunnyWithPancakeOnHead.png";
//    private static final String fileName = "./data/bunnyWithPahckaeOnHead.png";

    public static void main(String[] args) {

        ImagePlus imp2 = IJ.createImage("blank", 500, 500, 1, 8);
        imp2.show();
        ImagePlus imp = IJ.openImage(FILENAME);
//        imp.show();

                        IJ.run(imp, "8-bit", "");
                        imp.show();
                        ImagePlus imp3 = imp2.duplicate();
                        imp3.getProcessor().copyBits(imp.getProcessor(), 10, 10, 0);
                        imp3.show();
     }
}
