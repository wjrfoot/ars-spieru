/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.model;

import java.awt.Image;

/**
 *
 * @author wjrfo
 */
public class ImageRecord {

    /**
     * @return the filNam
     */
    public String getFilNam() {
        return filNam;
    }

    /**
     * @param filNam the filNam to set
     */
    public void setFilNam(String filNam) {
        this.filNam = filNam;
    }

    /**
     * @return the image
     */
    public Image getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Image image) {
        this.image = image;
    }

    private  String filNam = "";
    private Image image = null;
    
    
    
}
