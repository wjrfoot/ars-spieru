/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.model;

import ij.ImagePlus;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author wjrfo
 */
final public class DataStore {

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the ip1
     */
    public ImagePlus getIp1() {
        return ip1;
    }

    /**
     * @param ip1 the ip1 to set
     */
    public void setIp1(ImagePlus ip1) {
        this.ip1 = ip1;
    }

    /**
     * @return the ip2
     */
    public ImagePlus getIp2() {
        return ip2;
    }

    /**
     * @param ip2 the ip2 to set
     */
    public void setIp2(ImagePlus ip2) {
        this.ip2 = ip2;
    }

    /**
     * @return the ip3
     */
    public ImagePlus getIp3() {
        return ip3;
    }

    /**
     * @param ip3 the ip3 to set
     */
    public void setIp3(ImagePlus ip3) {
        this.ip3 = ip3;
    }

    /**
     * @return the ip4
     */
    public ImagePlus getIp4() {
        return ip4;
    }

    /**
     * @param ip4 the ip4 to set
     */
    public void setIp4(ImagePlus ip4) {
        this.ip4 = ip4;
    }

    private final static ArrayList<DataStore> dataStore = new ArrayList<>();

    public static DataStore dataStoreFactory() {
        DataStore ds = new DataStore();
        ds.id = dataStore.size();
        dataStore.add(ds);
        return ds;

    }

    public static DataStore getDataStore(int idx) {
        return dataStore.get(idx);
    }

    private DataStore() {

    }
    
    private int id = 0;
    private ImagePlus ip = null; 
    private ImagePlus ip1 = null; 
    private ImagePlus ip2 = null; 
    private ImagePlus ip3 = null; 
    private ImagePlus ip4 = null; 

    /**
     * @return the ip
     */
    public ImagePlus getIp() {
        return ip.duplicate();
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(ImagePlus ip) {
        this.ip = ip;
    }

}
