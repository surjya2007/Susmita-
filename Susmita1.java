import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TaskManager {

    private static DefaultListModel<String> taskModel = new DefaultListModel<>();
    private static JList<String> taskList = new JList<>(taskModel);
    private static final String FILE_NAME = "tasks.txt";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Student Task Manager");
        frame.setSize(450, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JTextField taskField = new JTextField();

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton doneButton = new JButton("Mark Done");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(taskField, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(doneButton);
        bottomPanel.add(deleteButton);

        JScrollPane scrollPane = new JScrollPane(taskList);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        loadTasks();

        addButton.addActionListener(e -> {
            String task = taskField.getText().trim();
            if (!task.isEmpty()) {
                taskModel.addElement(task);
                taskField.setText("");
                saveTasks();
            }
        });

        deleteButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                taskModel.remove(index);
                saveTasks();
            }
        });

        doneButton.addActionListener(e -> {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                String task = taskModel.get(index);
                if (!task.startsWith("✔ ")) {
                    taskModel.set(index, "✔ " + task);
                    saveTasks();
                }
            }
        });

        frame.setVisible(true);
    }

    private static void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskModel.size(); i++) {
                writer.write(taskModel.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskModel.addElement(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}