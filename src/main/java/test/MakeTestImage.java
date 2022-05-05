/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import ij.ImagePlus;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static test.Scan.fileName;

/**
 *
 * @author wjrfo
 */
public class MakeTestImage {

    public static void main(String[] args) {
        MakeTestImage mti = new MakeTestImage(100, 100);
        mti.run();
    }

    private static int colors[] = {0x00, 0x40, 0x80, 0xC0, 0xFF};
    int cellHeight, cellWidth;

    public MakeTestImage(int cellHeight, int cellWidth) {
        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;
    }

    private int makeColor(int red, int green, int blue) {
        return (colors[red] << 16) | (colors[green] << 8) | (colors[blue]);
//        return 0xFF;
    }

    private int[] makeImageArray(PrintWriter pw) {
        int[] pixels = new int[colors.length * colors.length * colors.length * cellHeight * cellWidth];

        int pixIdx = 0;
        for (int blu = 0; blu < colors.length; blu++) {
            for (int wid = 0; wid < cellWidth; wid++) {
                for (int gre = 0; gre < colors.length; gre++) {
                    for (int red = 0; red < colors.length; red++) {
                        for (int hgt = 0; hgt < cellHeight; hgt++) {
                            int col = makeColor(red, gre, blu);
                            pixels[pixIdx++] = col;
                        }
                    }
                }
            }
        }

        for (int blu = 0; blu < colors.length; blu++) {
                for (int gre = 0; gre < colors.length; gre++) {
                    for (int red = 0; red < colors.length; red++) {
                            int col = makeColor(red, gre, blu);
                            pw.printf("%2d %2d %06x\n", blu, gre * colors.length + red, col);
//                            pw.printf("%2d %2d %2d %06x\n", red, gre, blu, col);
                }
            }
        }

        return pixels;
    }

    public void run() {
        String textFileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\testPanel.txt";
        
        try {
            PrintWriter pw = new PrintWriter(textFileName);

        int[] pixels = makeImageArray(pw);
        
        ImageProcessor ipr = new ColorProcessor(cellWidth * colors.length * colors.length, cellHeight * colors.length, pixels);
        ImagePlus ip = new ImagePlus("Test Panel", ipr);
        ip.show();

        Image image = ip.getImage();

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().drawImage(image, 0, 0, null);
        
        String imageFileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\testPanel.png";
        File imageFile = new File(imageFileName);
        System.out.println("Output file name: " + imageFileName);
        try {
            ImageIO.write(bufferedImage, "png", imageFile);
        } catch (IOException ex) {
            Logger.getLogger(MakeTestImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.close();
        } catch (IOException ex) {
            Logger.getLogger(MakeTestImage.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }
}
