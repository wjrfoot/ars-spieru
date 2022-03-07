/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

/**
 *
 * @author wjrfo
 */
public class Util {

    private static String defPicDir = "C:\\Users\\wjrfo\\OneDrive\\Pictures";

    public static String getLastFileName() {
        return getLastFileName(defPicDir);
    }

    public static String getLastFileName(String picDir) {

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
