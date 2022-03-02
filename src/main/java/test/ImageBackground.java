/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author wjrfo
 */
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
public class ImageBackground 
{
  public static void main(String args[]) {
      new ImageBackground();
  }
          
public ImageBackground()           
  {
    JFrame frame = new JFrame("Display an image in the background");
//    final ImageIcon icon = new ImageIcon("images/background.png");

    URL imageURL =  getClass().getClassLoader().getResource("wheat.jpg");
    final ImageIcon icon = new ImageIcon(imageURL);
    
    int width = icon.getIconWidth();
    int height = icon.getIconHeight();

    JTextArea text = new JTextArea() 
    {
      Image img = icon.getImage();
      // instance initializer
      {setOpaque(false);}
      public void paintComponent(Graphics graphics) 
      {
        graphics.drawImage(img, 0, 0, this);
        super.paintComponent(graphics);
      }
    };
    JScrollPane pane = new JScrollPane(text);
    Container content = frame.getContentPane();
    content.add(pane, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(3);
    frame.setSize(width, height);
    frame.setVisible(true);
  }
}