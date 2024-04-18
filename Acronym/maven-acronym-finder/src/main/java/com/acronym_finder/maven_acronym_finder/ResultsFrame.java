package com.acronym_finder.maven_acronym_finder;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultsFrame extends JFrame {
    private JList<String> list1;
    private JPanel resultsPanel;

    public ResultsFrame() {
        setTitle("Acronym Finder Plus");
        setSize(650, 600);

        // Create JList with specific size
        list1 = new JList<>();
        list1.setVisibleRowCount(32);

        JScrollPane scrollableList = new JScrollPane(list1);
        // Set the preferred size for scrollableList
        scrollableList.setPreferredSize(new Dimension(500, 500));    // Increased height of the box
        scrollableList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        resultsPanel = new JPanel();

        // Set the background color of the panel.
        resultsPanel.setBackground(Color.decode("#BABBC1"));

        // Set the margins of the panel.
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        resultsPanel.add(scrollableList);    // Add scrollable list to your panel

        setContentPane(resultsPanel);     // Set the panel as the content pane of the JFrame

        loadContentsToList();

        // To recognize clicks on list items, add a ListSelectionListener
        list1.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedItem = list1.getSelectedValue();
                if (selectedItem.contains("undefined")) {
                    String acronym = selectedItem.split(" : ")[0];
                    int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to add a definition for " + acronym + "?",
                            "Add definition", JOptionPane.YES_NO_OPTION);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        new AddFrame(acronym);
                    }
                }
            }
        });

        setVisible(true);
    }

    // Method to handle file reading and setting JList content
    private void loadContentsToList() {
        try (BufferedReader reader = new BufferedReader(new FileReader("results.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            // set the model of the JList as the data extracted from the file
            list1.setModel(new DefaultListModel<String>() {{
                lines.forEach(this::addElement);
            }});
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}