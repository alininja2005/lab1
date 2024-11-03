
package Processscheduling;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Processscheduling.Process;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    double weight;
    int startTime;
    int completeTime;
    int waitingTime;
    int turnaroundTime;

    public Process(int pid, int arrivalTime, int burstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.weight = 1.0 / burstTime;
    }
}

public class ProcessSchedulingGUI {
    private JFrame frame;
    private JTextField processField;
    private JTextField arrivalField;
    private JTextField burstField;
    private JTextArea outputArea;
    private List<Process> processes;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ProcessSchedulingGUI window = new ProcessSchedulingGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ProcessSchedulingGUI() {
        processes = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("SJF (NON-PREMPTIVE)");
        frame.setBounds(100, 100, 700, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.WHITE); // Set background color of the frame

        // Using GridBagLayout for more flexible component placement
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding around components

        JLabel lblProcess = new JLabel("Enter process name(if you enter anyother N.O it will always start with one):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        frame.add(lblProcess, gbc);

        processField = new JTextField();
        processField.setBackground(new Color(230, 230, 230)); 
        processField.setPreferredSize(new java.awt.Dimension(150, 25)); 
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(processField, gbc);

        JLabel lblArrival = new JLabel("Enter arrival time:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(lblArrival, gbc);

        arrivalField = new JTextField();
        arrivalField.setBackground(new Color(230, 230, 230)); // Light gray background
        arrivalField.setPreferredSize(new java.awt.Dimension(150, 25)); // Setting preferred size
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(arrivalField, gbc);

        JLabel lblBurst = new JLabel("Enter burst time:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(lblBurst, gbc);

        burstField = new JTextField();
        burstField.setBackground(new Color(230, 230, 230)); // Light gray background
        burstField.setPreferredSize(new java.awt.Dimension(150, 25)); // Setting preferred size
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(burstField, gbc);

        JButton btnAdd = new JButton("Add Process");
        btnAdd.setBackground(Color.RED);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(120, 30)); 
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProcess();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; 
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(btnAdd, gbc);

        JButton btnCalculate = new JButton("Calculate");
        btnCalculate.setBackground(Color.GREEN); 
        btnCalculate.setForeground(Color.WHITE); 
        btnCalculate.setPreferredSize(new Dimension(120, 30)); 
        btnCalculate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                calculate();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Spanning across two columns
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        frame.add(btnCalculate, gbc);

        outputArea = new JTextArea();
        outputArea.setEditable(false); 
        outputArea.setBackground(Color.WHITE); 
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; 
        gbc.gridheight = 2; 
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(outputArea, gbc);
    }

    private void addProcess() {
        try {
            int pid = processes.size() + 1;
            int arrivalTime = Integer.parseInt(arrivalField.getText());
            int burstTime = Integer.parseInt(burstField.getText());
            processes.add(new Process(pid, arrivalTime, burstTime));
            arrivalField.setText("");
            burstField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers.");
        }
    }
    private void calculate() {
        processes.sort((p1, p2) -> Double.compare(p2.weight, p1.weight));
        int currentTime = 0;
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;
        StringBuilder ganttChart = new StringBuilder("Gantt Chart:\n");

    
         ganttChart.append("+").append("-".repeat(8 * processes.size())).append("+\n");
         ganttChart.append("|");

        for (Process p : processes) {
            p.startTime = currentTime;
            currentTime += p.burstTime;
            p.completeTime = currentTime;
            p.waitingTime = p.completeTime - p.startTime;
            p.turnaroundTime = p.waitingTime + p.burstTime;
            totalWaitingTime += p.waitingTime;
            totalTurnaroundTime += p.turnaroundTime;
        }
    
    ganttChart.append("|");
    for (Process p : processes) {
        ganttChart.append(" P").append(p.pid).append(" |");
    }
    ganttChart.append("\n+").append("-".repeat(8 * processes.size())).append("+\n");

    for (Process p : processes) {
        ganttChart.append(String.format("%-8s", p.startTime));
    }
    ganttChart.append(String.format("%-8s", currentTime)); // End time of the last process

        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();
    
        outputArea.setText(""); // Clear previous content
        outputArea.setAlignmentX(JTextArea.CENTER_ALIGNMENT); // Center alignment for JTextArea
    
        outputArea.append(ganttChart.toString() + "\n\nProcess Details:\n");
        outputArea.append("Process\tArrival\tBurst\tStart\tComplete\tWaiting\tTurnaround\n");
    
        for (Process p : processes) {
            outputArea.append("P" + p.pid + "\t" + p.arrivalTime + "\t" + p.burstTime + "\t" +
                    p.startTime + "\t" + p.completeTime + "\t" + p.waitingTime + "\t" + p.turnaroundTime + "\n");
        }
    
        outputArea.append("\nAverage Waiting Time: " + avgWaitingTime);
        outputArea.append("\nAverage Turnaround Time: " + avgTurnaroundTime);
    }
    
}




















