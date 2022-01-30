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

    private int lowThreshold1 = 80;      // pass 1 analyze particles threshold 
    private int hiThreshold1 = 255;
    private int lowThreshold2 = 185;     // pass 2 analyze particles threshold 
    private int hiThreshold2 = 255;
    private String measureParams = "area centroid perimeter fit shape redirect=None decimal=2 bounding rectangle";
    private String analyzeBase = "size=100-10000 circularity=0.1-1.00 bounding rectanble";
    private String analyzeKernel = "size=10-30000 circularity=0.1-1.00";
    private String analyzeChalk = "size=10-30000 circularity=0.1-1.00";

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
                    setLowThreshold1(Integer.getInteger((String) properties.get("lowThreshold1")));
                    setHiThreshold1(Integer.getInteger((String) properties.get("hiThreshold1")));
                    setLowThreshold2(Integer.getInteger((String) properties.get("lowThreshold2")));
                    setHiThreshold2(Integer.getInteger((String) properties.get("hiThreshold2")));
                    setMeasureParams((String)properties.get("measureParams"));
                    setAnalyzeBase((String)properties.get("analyzeBase"));
                    setAnalyzeChalk((String)properties.get("analyzeChalk"));
                    setAnalyzeKernel((String)properties.get("analyzeKernel"));
//                    se(Integer.getInteger((String) properties.get("hiThreshold2")));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void saveProperties() {
        properties.setProperty("lowThreshold1", Integer.toString(getLowThreshold1()));
        properties.setProperty("hiThreshold1", Integer.toString(getHiThreshold1()));
        properties.setProperty("lowThreshold2", Integer.toString(getLowThreshold2()));
        properties.setProperty("hiThreshold2", Integer.toString(getHiThreshold2()));
        properties.setProperty("measureParams", getMeasureParams());
        properties.setProperty("analyzeBase", getAnalyzeBase());
        properties.setProperty("analyzeChalk", getAnalyzeChalk());
        properties.setProperty("analyzeKernel", getAnalyzeKernel());
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
     * @return the lowThreshold1
     */
    public int getLowThreshold1() {
        return lowThreshold1;
    }

    /**
     * @param lowThreshold1 the lowThreshold1 to set
     */
    public void setLowThreshold1(int lowThreshold1) {
        this.lowThreshold1 = lowThreshold1;
    }

    /**
     * @return the hiThreshold1
     */
    public int getHiThreshold1() {
        return hiThreshold1;
    }

    /**
     * @param hiThreshold1 the hiThreshold1 to set
     */
    public void setHiThreshold1(int hiThreshold1) {
        this.hiThreshold1 = hiThreshold1;
    }

    /**
     * @return the lowThreshold2
     */
    public int getLowThreshold2() {
        return lowThreshold2;
    }

    /**
     * @param lowThreshold2 the lowThreshold2 to set
     */
    public void setLowThreshold2(int lowThreshold2) {
        this.lowThreshold2 = lowThreshold2;
    }

    /**
     * @return the hiThreshold2
     */
    public int getHiThreshold2() {
        return hiThreshold2;
    }

    /**
     * @param hiThreshold2 the hiThreshold2 to set
     */
    public void setHiThreshold2(int hiThreshold2) {
        this.hiThreshold2 = hiThreshold2;
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
     * @return the measureParams
     */
    public String getMeasureParams() {
        return measureParams;
    }

    /**
     * @param measureParams the measureParams to set
     */
    public void setMeasureParams(String measureParams) {
        this.measureParams = measureParams;
    }

//</editor-fold>

}
