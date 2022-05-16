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
import java.util.ArrayList;
import java.util.HashMap;
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
                            pixels[pixIdx] = col;
                            pixIdx++;
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

            checkSquares(ip.getProcessor());
        
        Image image = ip.getImage();

            System.out.println("width " + image.getWidth(null) + " height " + image.getHeight(null));
        
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().drawImage(image, 0, 0, null);
        
        String imageFileName = "C:\\Users\\wjrfo\\Documents\\2018 June flour pics\\testPanel.bmp";
        File imageFile = new File(imageFileName);
        System.out.println("Output file name: " + imageFileName);
        try {
            ImageIO.write(bufferedImage, "bmp", imageFile);
        } catch (IOException ex) {
            Logger.getLogger(MakeTestImage.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.close();
        } catch (IOException ex) {
            Logger.getLogger(MakeTestImage.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }
    
    
    private void checkSquares(ImageProcessor imageProcessor) {
        
        ArrayList<HashMap> mapList = new ArrayList<>();

        // assumes that there are 5 different colors leading to an image
        // with 25x5 (w x h) boxes and each box is 100 x 100 pixels
        
        for (int rdx = 0; rdx < 5; rdx++ ) {
            for (int gdx = 0; gdx < 5; gdx++ ) {
                for (int bdx = 0; bdx < 5; bdx++ ) {
                    HashMap<Integer, Integer> hashMap = new HashMap<>();
                    mapList.add(hashMap);
                    int initX = (5 * rdx + gdx) * 100;
                    int initY = bdx * 100;
                    for (int xdx = 0; xdx < 100; xdx++) {
                        for (int ydx = 0; ydx < 100; ydx++) {
                            int pixel = imageProcessor.getPixel(xdx + initX, ydx + initY);
                            Integer total = hashMap.get(pixel);
                            if (total == null) {
                                hashMap.put(pixel, 1);
                            } else {
                                int tot = total.intValue();
                                tot++;
                                hashMap.put(pixel, tot);
                            }
                        }
                    }
                }
            }
        }
        
        System.out.println("debug");
    }


}
