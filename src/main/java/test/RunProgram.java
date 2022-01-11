/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author wjrfo
 */
/**
 * Runs external application from this Java program
 */
public class RunProgram {
 
    private static String progName = "C:\\Windows\\twain_32\\escndv\\escndv.exe";
    
    public static void main(String[] args) {
        RunProgram rp = new RunProgram();
        rp.openNotePad();
    }
 
    /**
     * Runs Notepad program in the Windows system. Please note that this assumes
     * you are running this program on Windows.
     */
    private void openNotePad() {
        Runtime rt = Runtime.getRuntime();
        try {
            Process p = rt.exec(progName);
            int rtn = p.waitFor();
            System.out.println("rtn " + rtn);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}