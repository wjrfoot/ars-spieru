package gov.usda.ars.spieru.chalk.view;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author wjrfo
 */

import test.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ShowImage extends Panel {

    BufferedImage image;

    public ShowImage(String imageName) {
        try {
            System.out.println("current directory : " + new File(".").getCanonicalPath());
            File input = new File(imageName);
            image = ImageIO.read(input);
        } catch (IOException ie) {
            System.out.println("Error:" + ie.getMessage());
        }
    }

    public ShowImage() {
        try {
            System.out.println("current directory : " + new File(".").getCanonicalPath());
            System.out.println("Enter image name\n");
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            String imageName = bf.readLine();
            File input = new File(imageName);
            image = ImageIO.read(input);
        } catch (IOException ie) {
            System.out.println("Error:" + ie.getMessage());
        }
    }

    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    static public void main(String args[]) throws
            Exception {
        JFrame frame = new JFrame("Display image");
        Panel panel = new ShowImage();
        frame.getContentPane().add(panel);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
