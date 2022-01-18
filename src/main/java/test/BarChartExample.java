/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author wjrfo
 */
import gov.usda.ars.spieru.chalk.controller.SubImagePlus;
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
import java.util.List;

public class BarChartExample extends JFrame {

    List<SubImagePlus> subImagePlusList;

    public BarChartExample(List<SubImagePlus> subImagePlusList) {

        this.subImagePlusList = subImagePlusList;
        initUI();
    }

    public BarChartExample() {

        this(null);
    }

    private void initUI() {

        CategoryDataset dataset = createDataset();

        JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);

        pack();
        setTitle("Bar chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private CategoryDataset createDataset() {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (subImagePlusList == null) {
            dataset.setValue(46, "Gold medals", "USA");
            dataset.setValue(38, "Gold medals", "China");
            dataset.setValue(29, "Gold medals", "UK");
            dataset.setValue(22, "Gold medals", "Russia");
            dataset.setValue(13, "Gold medals", "South Korea");
            dataset.setValue(11, "Gold medals", "Germany");
        } else {
            double[] bounds = {0.1, 0.2, 0.5, 1.1};
            int[] count = new int[bounds.length];

            for (SubImagePlus sip : subImagePlusList) {
                double kernel = Double.parseDouble(sip.getKernelResults().split("\\t")[1]);
                double chalk = Double.parseDouble(sip.getChalkResults().split("\\t")[1]);
                double ratio = chalk / kernel;
                for (int idx = 0; idx < bounds.length; idx++) {
                    if (ratio < bounds[idx]) {
                        count[idx]++;
                        break;
                    }
                }
            }
            for (int idx = 0; idx < bounds.length; idx++) {
                dataset.setValue(count[idx], "Gold medals", ""+ bounds[idx]);
            }
        }
        return dataset;
    }

    private JFreeChart createChart(CategoryDataset dataset) {

        JFreeChart barChart = ChartFactory.createBarChart(
                "Chalk Kernel Analysis (need to add name here)",
                "",
                "Chalk count by category",
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
}
