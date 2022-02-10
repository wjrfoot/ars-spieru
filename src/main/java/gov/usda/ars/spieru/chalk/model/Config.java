/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @TODO this class should use reflection to do the loads and save to the
 * properties file
 */
/**
 *
 * @author wjrfo
 */
public class Config {


    private Properties properties = new Properties();
    private String propertiesFileName = "chalk.xml";
    public static String ChalkThresholdLow = "ChalkThresholdLow";
    public static String ChalkThresholdHigh = "ChalkThresholdHigh";
    public static String KernelThresholdLow = "KernelThresholdLow";
    public static String KernelThresholdHigh = "KernelThresholdHigh";
    public static String MeasurementParamsBase = "MeasurementParamsBase";
    public static String MeasurementParamsKernel = "MeasurementParamsKernel";
    public static String MeasurementParamsChalk = "MeasurementParamsChalk";
    public static String AnalyzeBase = "AnalyzeBase";
    public static String AnalyzeKernel = "AnalyzeKernel";
    public static String AnalyzeChalk = "AnalyzeChalk";
    public static String ScanDirectory = "ScanDirectory";
    public static String LightSource = "LightSource";
    public static String DPI = "DPI";

    public static void main(String[] args) {  // quick and dirty instead of using junit
        String dirS = System.getProperty("user.home");
        System.out.println(dirS);
        File localDir = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU");
        System.out.println(localDir.getPath() + " " + localDir.exists());
        localDir.mkdir();
        System.out.println(localDir.getPath() + " " + localDir.exists());
        Config config = new Config();
        config.saveProperties();

    }

//    private int lowThreshold1 = 80;      // pass 1 analyze particles threshold 
//    private int hiThreshold1 = 255;
//    private int lowThreshold2 = 185;     // pass 2 analyze particles threshold 
//    private int hiThreshold2 = 255;
    private String lowThresholdKernel = "80";
    private String hiThresholdKernel = "255";
    private String lowThresholdChalk = "185";
    private String hiThresholdChalk = "255";
    private String measureParamsBase = "area centroid perimeter fit shape redirect=None decimal=2 bounding rectangle";
    private String measureParamsKernel = "area centroid perimeter fit shape redirect=None decimal=2";
    private String measureParamsChalk = "area centroid perimeter fit shape redirect=None decimal=2";
    private String analyzeBase = "size=100-10000 circularity=0.1-1.00 bounding rectanble";
    private String analyzeKernel = "size=10-30000 circularity=0.1-1.00";
    private String analyzeChalk = "size=10-30000 circularity=0.1-1.00";
    private String scanDirectory = System.getProperty("user.home") + File.separator + "Pictures";
    public static String lightSource = "Back lit";
    private String dPI = "300";
    
    /**
     * makes directory in AppData/Local for this application
     */
    private void makeLocalDir() {
        String dirS = System.getProperty("user.home");
        File localDir = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU");
        if (!localDir.exists()) {
            localDir.mkdir();
        }
    }

    public void loadProperties() {
        File localDir = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU");
        if (localDir.exists()) {
            File localFile = new File(localDir, propertiesFileName);
            if (localFile.exists()) {
                try {
                    InputStream is = new FileInputStream(localFile);
                    properties.loadFromXML(is);
                    String str = properties.getProperty(KernelThresholdLow);
                    int stri = Integer.parseInt(str.trim());
                    setLowThresholdKernel((String) properties.get(KernelThresholdLow));
                    setHiThresholdKernel((String) properties.get(KernelThresholdHigh));
                    setLowThresholdChalk((String) properties.get(ChalkThresholdLow));
                    setHiThresholdChalk((String) properties.get(ChalkThresholdHigh));
                    setMeasureParamsBase((String)properties.get(MeasurementParamsBase));
                    setMeasureParamsChalk((String)properties.get(MeasurementParamsChalk));
                    setMeasureParamsKernel((String)properties.get(MeasurementParamsKernel));
                    setAnalyzeBase((String)properties.get(AnalyzeBase));
                    setAnalyzeChalk((String)properties.get(AnalyzeChalk));
                    setAnalyzeKernel((String)properties.get(AnalyzeKernel));
                    setScanDirectory((String) properties.getProperty(ScanDirectory));
                    setdPI((String) properties.getProperty(DPI));
                    setMeasureParamsChalk(measureParamsChalk);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void saveProperties() {
        properties.setProperty(KernelThresholdLow, getLowThresholdKernel());
        properties.setProperty(KernelThresholdHigh, getHiThresholdKernel());
        properties.setProperty(ChalkThresholdLow, getLowThresholdChalk());
        properties.setProperty(ChalkThresholdHigh, getHiThresholdChalk());
        properties.setProperty(MeasurementParamsBase, getMeasureParamsBase());
        properties.setProperty(MeasurementParamsChalk, getMeasureParamsChalk());
        properties.setProperty(MeasurementParamsKernel, getMeasureParamsKernel());
        properties.setProperty(AnalyzeBase, getAnalyzeBase());
        properties.setProperty(AnalyzeChalk, getAnalyzeChalk());
        properties.setProperty(AnalyzeKernel, getAnalyzeKernel());
        properties.setProperty(AnalyzeKernel, getAnalyzeKernel());
        properties.setProperty(ScanDirectory, getScanDirectory());
        properties.setProperty(LightSource, getLightSource());
        properties.setProperty(DPI, getdPI());
        File localDir = new File(System.getProperty("user.home"), "AppData\\Local\\ARS-SPIERU");
        if (!localDir.exists()) {
            localDir.mkdir();
        }
        File localFile = new File(localDir, propertiesFileName);
        OutputStream os;
        try {
            os = new FileOutputStream(localFile);
            properties.storeToXML(os, "hi ho");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    
    /**
     * @return the lowThresholdKernel
     */
    public String getLowThresholdKernel() {
        return lowThresholdKernel;
    }
    
    public int getLowThresholdKernel(int arg) {
        return Integer.parseInt(lowThresholdKernel);
    }

    /**
     * @param lowThresholdKernel the lowThresholdKernel to set
     */
    public void setLowThresholdKernel(String lowThresholdKernel) {
        this.lowThresholdKernel = lowThresholdKernel;
    }

    /**
     * @return the hiThresholdKernel
     */
    public String getHiThresholdKernel() {
        return hiThresholdKernel;
    }

    public int getHiThresholdKernel(int arg) {
        return Integer.parseInt(hiThresholdKernel);
    }

    /**
     * @param hiThresholdKernel the hiThresholdKernel to set
     */
    public void setHiThresholdKernel(String hiThresholdKernel) {
        this.hiThresholdKernel = hiThresholdKernel;
    }

    /**
     * @return the lowThresholdChalk
     */
    public String getLowThresholdChalk() {
        return lowThresholdChalk;
    }

    public int getLowThresholdChalk(int arg) {
        return Integer.parseInt(lowThresholdChalk);
    }

    /**
     * @param lowThresholdChalk the lowThresholdChalk to set
     */
    public void setLowThresholdChalk(String lowThresholdChalk) {
        this.lowThresholdChalk = lowThresholdChalk;
    }

    /**
     * @return the hiThresholdChalk
     */
    public String getHiThresholdChalk() {
        return hiThresholdChalk;
    }

    public int getHiThresholdChalk(int arg) {
        return Integer.parseInt(hiThresholdChalk);
    }

    /**
     * @param hiThresholdChalk the hiThresholdChalk to set
     */
    public void setHiThresholdChalk(String hiThresholdChalk) {
        this.hiThresholdChalk = hiThresholdChalk;
    }

    /**
     * @return the analyzeBase
     */
    public String getAnalyzeBase() {
        return analyzeBase;
    }

    /**
     * @param analyzeBase the analyzeBase to set
     */
    public void setAnalyzeBase(String analyzeBase) {
        this.analyzeBase = analyzeBase;
    }

    /**
     * @return the analyzeKernel
     */
    public String getAnalyzeKernel() {
        return analyzeKernel;
    }

    /**
     * @param analyzeKernel the analyzeKernel to set
     */
    public void setAnalyzeKernel(String analyzeKernel) {
        this.analyzeKernel = analyzeKernel;
    }

    /**
     * @return the analyzeChalk
     */
    public String getAnalyzeChalk() {
        return analyzeChalk;
    }

    /**
     * @param analyzeChalk the analyzeChalk to set
     */
    public void setAnalyzeChalk(String analyzeChalk) {
        this.analyzeChalk = analyzeChalk;
    }

        /**
     * @return the measureParamsBase
     */
    public String getMeasureParamsBase() {
        return measureParamsBase;
    }

    /**
     * @param measureParamsBase the measureParamsBase to set
     */
    public void setMeasureParamsBase(String measureParamsBase) {
        this.measureParamsBase = measureParamsBase;
    }

  
    /**
     * @return the scanDirectory
     */
    public String getScanDirectory() {
        return scanDirectory;
    }

    /**
     * @param scanDirectory the scanDirectory to set
     */
    public void setScanDirectory(String scanDirectory) {
        this.scanDirectory = scanDirectory;
    }
    
    /**
     * @return the lightSource
     */
    public String getLightSource() {
        return lightSource;
    }

    /**
     * @param lightSource the lightSource to set
     */
    public void setLightSource(String lightSource) {
        this.lightSource = lightSource;
    }

    /**
     * @return the dPI
     */
    public String getdPI() {
        return dPI;
    }

    /**
     * @param dPI the dPI to set
     */
    public void setdPI(String dPI) {
        this.dPI = dPI;
    }


//</editor-fold>

    /**
     * @return the measureParamsKernel
     */
    public String getMeasureParamsKernel() {
        return measureParamsKernel;
    }

    /**
     * @param measureParamsKernel the measureParamsKernel to set
     */
    public void setMeasureParamsKernel(String measureParamsKernel) {
        this.measureParamsKernel = measureParamsKernel;
    }

    /**
     * @return the measureParamsChalk
     */
    public String getMeasureParamsChalk() {
        return measureParamsChalk;
    }

    /**
     * @param measureParamsChalk the measureParamsChalk to set
     */
    public void setMeasureParamsChalk(String measureParamsChalk) {
        this.measureParamsChalk = measureParamsChalk;
    }

}
