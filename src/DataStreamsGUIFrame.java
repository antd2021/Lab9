import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.*;
import java.nio.file.Files;


public class DataStreamsGUIFrame extends JFrame {

    private File selectedFile;
    private JTextArea originalText;
    private JTextArea filteredText;
    String fileContent;

    public DataStreamsGUIFrame() {
        setTitle("Invoice Generator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createGUI();
        setVisible(true);
    }

    private void createGUI() {
        // Title
        JLabel titleLabel = new JLabel("Keyword Filter");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);


        JPanel textPanel = new JPanel();
        textPanel.setBorder(BorderFactory.createTitledBorder("Text Information:"));
        textPanel.setLayout(new GridLayout(2, 2));

        originalText = new JTextArea(10, 25);
        originalText.setEditable(false);
        filteredText = new JTextArea(10, 25);
        filteredText.setEditable(false);
        JLabel textAreaLabel = new JLabel("Enter Filter Word-");
        JTextField filterWord = new JTextField();

        textPanel.add(new JScrollPane(originalText));
        textPanel.add(new JScrollPane(filteredText));
        textPanel.add(textAreaLabel);
        textPanel.add(filterWord);
        add(textPanel, BorderLayout.CENTER);


        // Stats Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Invoice Output:"));
        buttonsPanel.setLayout(new GridLayout(1, 3));

        JButton loadFile = new JButton("Load File");
        JButton searchFile = new JButton("Search File");
        JButton quit = new JButton("Quit");

        buttonsPanel.add(loadFile);
        buttonsPanel.add(searchFile);
        buttonsPanel.add(quit);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Action Listeners

        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(DataStreamsGUIFrame.this, "Are you sure you want to quit?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });

        loadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));

                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile();
                    try {
                        // Read the binary content of the selected file
                        FileInputStream inStream = new FileInputStream(selectedFile);
                        DataInputStream input = new DataInputStream(inStream);

                            fileContent = input.readUTF();
                            inStream.close();
                        //this takes the input from the file and then it should read it in plain text


                        originalText.setText(fileContent);
                        filteredText.setText("");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(DataStreamsGUIFrame.this,
                                "Error reading the file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        searchFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    String searchWord = filterWord.getText().trim();
                    if (!searchWord.isEmpty()) {
                        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(selectedFile))) {
                            String originalContent = Files.readString(selectedFile.toPath());

                            StringBuilder filteredContent = new StringBuilder();
                            filteredContent.append("Filtered Content:\n");

                            originalContent.lines()
                                    .filter(line -> line.contains(searchWord))
                                    .forEach(filteredLine -> {
                                        filteredContent.append(filteredLine).append("\n");
                                    });

                            filteredText.setText(filteredContent.toString());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(DataStreamsGUIFrame.this,
                                    "Error reading the file: " + ex.getMessage(),
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(DataStreamsGUIFrame.this,
                                "Please enter a search string.",
                                "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(DataStreamsGUIFrame.this,
                            "Please load a file first.",
                            "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });



    }
}