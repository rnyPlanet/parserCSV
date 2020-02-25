import db.DB;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.generateJTable();
    }

    private File selectedFile;

    private void chooseFile() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma-separated values", "csv", "text");
        jfc.setFileFilter(filter);
        jfc.showOpenDialog(null);

        File selectedFile1 = jfc.getSelectedFile();

        if (selectedFile1 != null) {
            selectedFile = selectedFile1;
        }
    }

    private void showInTable(DefaultTableModel model) {

        if (selectedFile != null) {
            try {
                String sourceFile = selectedFile.getAbsolutePath();

                Spliter spliter = new Spliter();

                List<List<String>> rowsFromFile = spliter.parse(sourceFile);

                Vector<String> columnNames = new Vector<>(rowsFromFile.get(0));
                model.setColumnIdentifiers(columnNames);

                String fileName = new File(sourceFile).getName();
                fileName = fileName.replaceAll("\\.|-|_|\\s+|\\(|\\)", "");

                DB db = new DB(fileName.substring(0, Math.min(fileName.length(), 16)),
                        rowsFromFile);

                db.write();

                db.read().stream().skip(1)
                        .forEach(i -> {
                            Vector<String> row = new Vector<>(i);
                            model.addRow(row);
                        });

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public void generateJTable() {

        JFrame frame = new JFrame();
        final DefaultTableModel model = new DefaultTableModel();

        final JTable table = new JTable();
        table.setModel(model);
        table.setForeground(Color.black);
        table.setRowHeight(30);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JButton btnCooseFile = new JButton("Choose a file");

        btnCooseFile.setBounds(0, 0, 200, 25);

        JScrollPane pane = new JScrollPane(table);
        pane.setBounds(0, 25, 880, 335);

        frame.setLayout(null);
        frame.add(pane);
        frame.add(btnCooseFile);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setPreferredSize(new Dimension(screenSize.width / 2, screenSize.height / 2));

        btnCooseFile.addActionListener(e -> {
            chooseFile();

            int rowCount = model.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                model.removeRow(i);
            }

            showInTable(model);
        });

        frame.setSize(900, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}