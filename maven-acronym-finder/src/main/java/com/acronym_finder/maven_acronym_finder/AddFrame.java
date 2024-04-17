package com.acronym_finder.maven_acronym_finder;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AddFrame extends JFrame {
    private JPanel addPanel;
    private JButton btInsert;
    private JButton btCancel;
    private JTextField tfAcronym;
    private JTextField tfDefinition;
    private JTextField tfNote;

    public AddFrame() {
        this("");
    }

    public AddFrame(String initialAcronym) {
        setContentPane(addPanel);
        setTitle("Acronym Finder Plus");
        setSize(450, 300);
        setVisible(true);

        tfAcronym.setText(initialAcronym);

        btCancel.addActionListener(e -> dispose());

        btInsert.addActionListener(e -> {
            String acronym = tfAcronym.getText();
            String definition = tfDefinition.getText();
            String note = tfNote.getText();  // Retrieve text from tfNote

            if (acronym.length() < 2 || !acronym.equals(acronym.toUpperCase()) || definition.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please make sure the acronym is valid (Use capital letters), and there is a definition given.");
                return;
            }

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/database",
                        "root",
                        "mysql"
                );

                // Add placeholder for the field Notes into SQL command.
                String sql = "INSERT INTO acronym (IdNum, Abbreviation, Term, Notes, `Rank`) VALUES (NULL, ?, ?, ?, 1)";

                PreparedStatement statement = con.prepareStatement(sql);
                statement.setString(1, acronym);
                statement.setString(2, definition);
                statement.setString(3, note.isEmpty()? "": note);  // Pass the note or "", if note is empty.

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(null, "The Acronym " + acronym + " has been successfully added!");
                    tfAcronym.setText("");
                    tfDefinition.setText("");
                    tfNote.setText("");  // Clear tfNote
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}