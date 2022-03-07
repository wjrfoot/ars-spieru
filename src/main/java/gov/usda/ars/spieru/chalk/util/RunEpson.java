/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.util;

import java.io.IOException;

/**
 *
 * @author wjrfo
 */
public class RunEpson {

    public static void main(String[] args) {
        run(defProgName);
    }
    
    private static String defProgName = "C:\\Windows\\twain_32\\escndv\\escndv.exe";

    public static void run(String progName) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process p = rt.exec(progName);
            int rtn = p.waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
