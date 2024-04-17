package com.acronym_finder.maven_acronym_finder;

//import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class HomeFrame extends JFrame {
    //private static XWPFDocument doc;
    private JTextArea taUpload;
    private JButton btSearch;
    private JButton btAdd;
    private JPanel homePanel;
    private String currentFilePath;


    public HomeFrame() {
        setContentPane(homePanel);
        setTitle("Acronym Finder Plus");
        setSize(450, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        taUpload.setDropTarget(new DropTarget() {
            public synchronized void drop(DropTargetDropEvent evt) {
                try {
                    evt.acceptDrop(DnDConstants.ACTION_COPY);
                    if (evt.getTransferable().isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        List<File> droppedFiles = (List<File>)
                                evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                        StringBuilder fileNames = new StringBuilder();
                        boolean isFileFormatCorrect = true;

                        for (File file : droppedFiles) {
                            // Check if file extension is .docx
                            if(!file.getName().toLowerCase().endsWith(".docx")) {
                                // If file isn't a .docx, show error message
                                isFileFormatCorrect = false;
                                JOptionPane.showMessageDialog(HomeFrame.this,
                                        "Incompatible file type, please submit a .docx file.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                            fileNames.append(file.getAbsolutePath());
                        }
                        if(isFileFormatCorrect) {
                            currentFilePath = fileNames.toString();
                            System.out.println(fileNames);
                            taUpload.setText(currentFilePath);

                            JOptionPane.showMessageDialog(HomeFrame.this,
                                    "File has been successfully dropped.",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IntegratedClass.executeSearch(currentFilePath); // Perform the search

                    // Instantiate and show ResultsFrame
                    ResultsFrame resultsFrame = new ResultsFrame();
                    resultsFrame.setVisible(true);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Instantiate and show AddFrame
                AddFrame addFrame = new AddFrame();
                addFrame.setVisible(true);
            }
        });

    }

    public static void main(String[] args) {
        HomeFrame myFrame = new HomeFrame();
        myFrame.setVisible(true);
    }
}