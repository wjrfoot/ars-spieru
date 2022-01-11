/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gov.usda.ars.spieru.chalk.view;

/**
 *
 * @author wjrfo
 */

import test.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;

public class BarChartExample extends JFrame {

    private ChartPanel chartPanel;
    
    public BarChartExample() {

        initUI();
    }

    private void initUI() {

        CategoryDataset dataset = createDataset();

        JFreeChart chart = createChart(dataset);
         setChartPanel(new ChartPanel(chart));
  //      ChartPanel chartPanel = new ChartPanel(chart);
        getChartPanel().setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        getChartPanel().setBackground(Color.white);
        
//        add(chartPanel);
//
//        pack();
//        setTitle("Bar chart");
//        setLocationRelativeTo(null);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private CategoryDataset createDataset() {

    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.setValue(46, "Gold medals", "USA");
        dataset.setValue(38, "Gold medals", "China");
        dataset.setValue(29, "Gold medals", "UK");
        dataset.setValue(22, "Gold medals", "Russia");
        dataset.setValue(13, "Gold medals", "South Korea");
        dataset.setValue(11, "Gold medals", "Germany");

        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart barChart = ChartFactory.createBarChart(
                "Olympic gold medals in London",
                "",
                "Gold medals",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false);

        return barChart;
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

        	BarChartExample ex = new BarChartExample();
            ex.setVisible(true);
        });
    }

    /**
     * @return the chartPanel
     */
    public ChartPanel getChartPanel() {
        return chartPanel;
    }

    /**
     * @param chartPanel the chartPanel to set
     */
    public void setChartPanel(ChartPanel chartPanel) {
        this.chartPanel = chartPanel;
    }
}