/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.util;

import java.io.IOException;


/**
 *
 * @author wjrfo
 * 
 * Runs external application from this Java program. It blocks until the program returns.
 */
public class RunProgram {
 
    private static String progName = "C:\\Windows\\twain_32\\escndv\\escndv.exe";
    
    public static void main(String[] args) {
        RunProgram rp = new RunProgram();
        rp.runProg();
    }
 
    /**
     * 
     */
    public void runProg() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process p = rt.exec(getProgName());
            int rtn = p.waitFor();
        }catch(IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the progName
     */
    public static String getProgName() {
        return progName;
    }

    /**
     * @param aProgName the progName to set
     */
    public static void setProgName(String aProgName) {
        progName = aProgName;
    }
}