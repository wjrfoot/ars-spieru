/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.view;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *  verifier for thresholds. requires hi and lo to be numbers between 0 and 255 and hi > lo
 * 
 * @author wjrfo
 */
public class ThresholdInputVerifer extends InputVerifier {

    JTextField lowerBound;
    JTextField upperBound;
   /**
    * verifier requires the two jtextfields because validation requires both
    * 
    * @param lowerBound
    * @param upperBound 
    */ 
    public ThresholdInputVerifer(JTextField lowerBound, JTextField upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }
    
    @Override
    public boolean verify(JComponent textField) {
        int inpVal = 0;
        try {
            JTextField jtf = (JTextField) textField;
            inpVal = Integer.parseInt(jtf.getText());
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(textField, "Input argument not a number");
            return false;
        }
        int lo = Integer.parseInt(lowerBound.getText());
        int hi = Integer.parseInt(upperBound.getText());
        if (lo >= hi) {
            JOptionPane.showMessageDialog(textField, "Lower bound cannot be greater than or equal to upper bound");
            return false;
    }
        if (lo < 0 | lo > 255 | hi < 0 | hi > 255) {
            JOptionPane.showMessageDialog(textField, "Value must be 0 <= val <= 255");
            return false;
        }
             
        return true;
    }
    
}
