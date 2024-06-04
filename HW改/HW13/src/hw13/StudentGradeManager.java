package hw13;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StudentGradeManager extends JFrame {
    private JButton addButton, saveButton, findButton, deleteButton, listButton;
    private JTextField nameField, heightField, weightField, bmiField;
    private JTextArea textArea;
    private JFileChooser fileChooser;
    private Map<String, Student> students;

    public StudentGradeManager() {
        super("Student BMI Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new FlowLayout());

        students = new HashMap<>();
        fileChooser = new JFileChooser();
        
        nameField = new JTextField(10);
        heightField = new JTextField(10);
        weightField = new JTextField(10);
        bmiField = new JTextField(10);
        bmiField.setEditable(false);  // BMI字段設為不可編輯
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Height (cm):"));
        add(heightField);
        add(new JLabel("Weight (kg):"));
        add(weightField);
        add(new JLabel("BMI:"));
        add(bmiField);

        addButton = new JButton("Add/Update BMI");
        addButton.addActionListener(this::addOrUpdateBMI);
        add(addButton);

        findButton = new JButton("Find Student");
        findButton.addActionListener(this::findStudent);
        add(findButton);

        deleteButton = new JButton("Delete Student");
        deleteButton.addActionListener(this::deleteStudent);
        add(deleteButton);

        listButton = new JButton("List All Students");
        listButton.addActionListener(this::listAllStudents);
        add(listButton);

        saveButton = new JButton("Save to CSV");
        saveButton.addActionListener(this::saveToCSV);
        add(saveButton);

        textArea = new JTextArea(15, 50);
        add(new JScrollPane(textArea));

        // 在構造函數中調用 loadStudentsFromCSV 方法加載資料
        loadStudentsFromCSV();
        // 在構造函數的最後調用 listAllStudents 方法
        listAllStudents(null);

        setVisible(true);
    }

    private void addOrUpdateBMI(ActionEvent e) {
        String name = nameField.getText();
        double height = Double.parseDouble(heightField.getText());
        double weight = Double.parseDouble(weightField.getText());
        Student student = new Student(name, height, weight);
        students.put(name, student);
        bmiField.setText(String.format("%.2f", student.getBmi())); // 顯示計算的 BMI
        textArea.setText("BMI added/updated for " + name);
    }

    private void findStudent(ActionEvent e) {
        String name = nameField.getText();
        Student student = students.get(name);
        if (student != null) {
            textArea.setText(String.format("Found: %s\nHeight: %.0f cm\nWeight: %.0f kg\nBMI: %.2f",
                                            student.getName(), student.getHeight(), student.getWeight(), student.getBmi()));
        } else {
            textArea.setText("Student not found: " + name);
        }
    }

    private void deleteStudent(ActionEvent e) {
        String name = nameField.getText();
        Student removed = students.remove(name);
        if (removed != null) {
            textArea.setText("Student deleted: " + name);
        } else {
            textArea.setText("Student not found: " + name);
        }
    }

    private void listAllStudents(ActionEvent e) {
        StringBuilder builder = new StringBuilder("All Students:\n");
        for (Student student : students.values()) {
            builder.append(String.format("%s\nHeight: %.0f cm, Weight: %.0f kg, BMI: %.2f\n",
                                         student.getName(), student.getHeight(), student.getWeight(), student.getBmi()));
        }
        textArea.setText(builder.toString());
    }

    private void saveToCSV(ActionEvent e) {
        try (FileWriter writer = new FileWriter("BMI_records.csv")) {
            writer.write("Name,Height,Weight,BMI\n");
            for (Student student : students.values()) {
                writer.write(String.format("%s,%.0f,%.0f,%.2f\n", student.getName(), student.getHeight(), student.getWeight(), student.getBmi()));
            }
            textArea.setText("CSV file saved successfully.");
        } catch (IOException ex) {
            textArea.setText("Error saving CSV file: " + ex.getMessage());
        }
    }

    private void loadStudentsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("BMI_records.csv"))) {
            String line;
            // 跳過標題行
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    double height = Double.parseDouble(parts[1]);
                    double weight = Double.parseDouble(parts[2]);
                    double bmi = Double.parseDouble(parts[3]);
                    Student student = new Student(name, height, weight);
                    students.put(name, student);
                }
            }
        } catch (IOException e) {
            textArea.setText("Error loading students from CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new StudentGradeManager();
    }
}
