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
public class Test1 {

    private static final String FILENAME = "./data/bunnyWithPancakeOnHead.png";
//    private static final String fileName = "./data/bunnyWithPahckaeOnHead.png";

    public static void main(String[] args) {

        ImagePlus imp = IJ.openImage(FILENAME);
        imp.show();

        ImagePlus dup = imp.duplicate();
        ImageProcessor ip = dup.getProcessor();

        ip.flipHorizontal();

        dup.show();

        ImagePlus dup2 = dup.duplicate();
        
        My_Inverter_A mia = new My_Inverter_A();
        
        mia.setup("", dup2);
        
        mia.run(dup2.getProcessor());

        dup2.show();
//    try {

        /* 
                        File input = new File(fileName);

                        BufferedImage image = ImageIO.read(input);

            
            Opener opener = new Opener();

        
        ImagePlus imp = new ImagePlus(fileName);

        ImageProcessor ip = imp.getProcessor();

        StackProcessor sp = new StackProcessor(imp.getStack(), ip);
         */
 /*       Opener opener = new Opener();

        ImagePlus imp = opener.openImage(fileName);  

        ImageProcessor ip = imp.getProcessor();

        StackProcessor sp = new StackProcessor(imp.getStack(), ip);*/
 /*       } catch (Exception e) {
            e.printStackTrace();
        }*/
        ImagePlus imgPlus = new ImagePlus(FILENAME);
        ImageProcessor imgProcessor = imgPlus.getProcessor();
       // imgProcessor.invert();
        RunCommand rc = new RunCommand();
        rc.setup("", imgPlus);
        rc.run(imgPlus.getProcessor());
        
        imgPlus.show();
        //      imgPlus.show();
        FileSaver fs = new FileSaver(imgPlus);
        fs.saveAsJpeg("path-to-inverted.jpg");

        /*       Opener opener = new Opener();
        ImagePlus imp = opener.openImage(fileName);
        imp.show();*/
 /*
        System.out.println("current dir " + System.getProperty("user.dir"));
        ImagePlus imp = IJ.openImage("./data/bunnyWithPahckaeOnHead.png");
        imp.show();

// Without getting back a pointer, and automatically showing it:
        IJ.open("./data/bunnyWithPahckaeOnHead.png");*/
    }
}
