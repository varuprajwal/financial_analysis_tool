/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Prajwal S
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class FinancialApp extends JFrame {
    private ArrayList<DataPoint> dataPoints;
    private JTextArea dataInputArea;
    private JTextArea resultArea;
    private JButton analyzeButton;
    private JPanel chartPanel;

    public FinancialApp() {
        setTitle("Financial Analysis Tool");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        dataPoints = new ArrayList<>();

        // Input Area
        dataInputArea = new JTextArea(10, 40);
        dataInputArea.setBorder(BorderFactory.createTitledBorder("Enter daily profit/loss data (format: date,value)"));
        JScrollPane inputScrollPane = new JScrollPane(dataInputArea);

        // Result Area
        resultArea = new JTextArea(5, 40);
        resultArea.setEditable(false);
        resultArea.setBorder(BorderFactory.createTitledBorder("Result"));
        JScrollPane resultScrollPane = new JScrollPane(resultArea);

        // Analyze Button
        analyzeButton = new JButton("Analyze");
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                analyzeData();
            }
        });

        // Chart Panel
        chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g);
            }
        };
        chartPanel.setPreferredSize(new Dimension(800, 300));

        // Layout
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);
        inputPanel.add(analyzeButton, BorderLayout.SOUTH);

        add(inputPanel, BorderLayout.NORTH);
        add(resultScrollPane, BorderLayout.CENTER);
        add(chartPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void analyzeData() {
        dataPoints.clear();
        String[] lines = dataInputArea.getText().split("\n");

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                String date = parts[0].trim();
                int value;
                try {
                    value = Integer.parseInt(parts[1].trim());
                    dataPoints.add(new DataPoint(date, value));
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid data: " + line);
                }
            }
        }

        int[] values = dataPoints.stream().mapToInt(DataPoint::getValue).toArray();
        int[] result = KadaneAlgorithm.findMaxSubarray(values);

        StringBuilder resultText = new StringBuilder();
        resultText.append("Maximum Profit Period: ").append(dataPoints.get(result[0]).getDate())
                .append(" to ").append(dataPoints.get(result[1]).getDate()).append("\n");
        resultText.append("Maximum Profit: ").append(result[2]);

        resultArea.setText(resultText.toString());
        chartPanel.repaint();
    }

    private void drawChart(Graphics g) {
        if (dataPoints.isEmpty()) return;

        int width = chartPanel.getWidth();
        int height = chartPanel.getHeight();
        int maxVal = dataPoints.stream().mapToInt(DataPoint::getValue).max().orElse(1);
        int minVal = dataPoints.stream().mapToInt(DataPoint::getValue).min().orElse(0);

        int yScale = height / (maxVal - minVal);
        int xScale = width / dataPoints.size();

        int[] values = dataPoints.stream().mapToInt(DataPoint::getValue).toArray();
        int[] result = KadaneAlgorithm.findMaxSubarray(values);

        // Draw bars
        for (int i = 0; i < dataPoints.size(); i++) {
            int x = i * xScale;
            int y = height - (dataPoints.get(i).getValue() - minVal) * yScale;
            int barHeight = (dataPoints.get(i).getValue() - minVal) * yScale;

            if (i >= result[0] && i <= result[1]) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.LIGHT_GRAY);
            }

            g.fillRect(x, y, xScale, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, xScale, barHeight);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FinancialApp::new);
    }
}
