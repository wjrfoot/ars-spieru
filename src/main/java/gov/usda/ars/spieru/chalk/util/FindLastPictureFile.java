/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.util;

import test.*;
import test.Find_Corners;
import gov.usda.ars.spieru.chalk.model.*;
import ij.IJ;
import ij.ImagePlus;
import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author wjrfo
 */
public class FindLastPictureFile {

    public static void main(String[] args) {
        FindLastPictureFile flpf = new FindLastPictureFile();
        System.out.println("file name " + flpf.getLastFileName());
        ImagePlus ip0 = IJ.openImage(flpf.getLastFileName());
        ip0.show();
        ImagePlus ip1 = ip0.duplicate();
        Find_Corners fc = new Find_Corners();
        fc.run(ip1.getProcessor());

    }

    private static String picDir = "C:\\Users\\wjrfo\\OneDrive\\Pictures";

    public static String getLastFileName() {

        String filNam;

        File dirFil = new File(picDir);
        File[] files = dirFil.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase().startsWith("img");
            }
        });

        filNam = files[files.length - 1].getAbsolutePath();

        return filNam;
    }
}
