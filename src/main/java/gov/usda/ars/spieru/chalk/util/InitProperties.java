/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wjrfo
 */
public class InitProperties {
    
    private static final String APPS = "chalk";

    /**
     * @return the prop
     */
    public static Properties getProp() {
        if (prop == null) {
            prop = new Properties();
        }
        return prop;
    }
    
    private static Properties prop = null;
    
    public static void main(String[] args) throws IOException {
        init();
    }
    
    public InitProperties() {
        
    }
    
    static void init() throws IOException {
        
        System.out.println("Starting init properties");
        
        File userHomeF = new File(System.getProperty ( "user.home" ));
        
        File appHomeF = new File(userHomeF, "AppData/Local/SPIERU");
        
        System.out.println(appHomeF.getAbsolutePath() + " " + appHomeF.exists());
        
        if (!appHomeF.exists()) {
            appHomeF.mkdir();
        }

        System.out.println(appHomeF.getAbsolutePath() + " " + appHomeF.exists());
        
        File propF = new File(appHomeF, APPS);
        
        Properties tempProp = getProp();
        
        try {
            tempProp.storeToXML(new FileOutputStream(propF), APPS);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InitProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
