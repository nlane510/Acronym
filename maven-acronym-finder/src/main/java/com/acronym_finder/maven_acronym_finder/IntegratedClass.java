package com.acronym_finder.maven_acronym_finder;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.regex.*;
import org.apache.poi.xwpf.usermodel.*;

public class IntegratedClass {
    private static XWPFDocument doc;
    private static String url="jdbc:mysql://localhost:3306/database";
    private static String username="root";
    private static String password="mysql";



    // This function will be called from MainFrame.java
    public static void executeSearch(String docxFilePath) throws Exception{
        String fileName = "testDoc.txt";

        try (FileInputStream fis = new FileInputStream(docxFilePath);
             FileOutputStream fos = new FileOutputStream(fileName)) {
            doc = new XWPFDocument(fis);

            for (XWPFParagraph para : doc.getParagraphs()) {
                String text = para.getText();
                fos.write(text.getBytes()); //adding to text file
                fos.write(System.lineSeparator().getBytes()); // Add newline
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, Integer> words = tokenizeFile(fileName);
        if (words != null && !words.isEmpty()) {
            executeDbSearch(words);
        } else {
            System.out.println("No words were tokenized from the file");
        }
    }

    public static Map<String, Integer> tokenizeFile(String fileName) {
        Map<String, Integer> tokens = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s");
                for (String token : words) {
                    String cleanedToken = token.replaceAll("[^\\w\\s/]", "");

                    if (hasConsecutiveCapitalLetters(cleanedToken)) {
                        tokens.put(cleanedToken, tokens.getOrDefault(cleanedToken, 0) + 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file " + fileName + " could not be found.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    public static boolean hasConsecutiveCapitalLetters(String token) {
        Pattern pattern = Pattern.compile("(?<!\\w)(?:[A-Z]{1,}(?:\\/[A-Z]{1,4})?|[A-Z] [A-Z])(?!\\w)");
        Matcher matcher = pattern.matcher(token);
        return matcher.find();
    }

    public static void executeDbSearch(Map<String, Integer> words) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(url,username,password);
        Statement statement = connection.createStatement();
        FileWriter fileWriter = new FileWriter("Results.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        for (String word : words.keySet()) {
            try {
                ResultSet resultSet = statement.executeQuery("SELECT * FROM acronym WHERE Abbreviation= '" + word + "'");
                if (!resultSet.next()) {
                    bufferedWriter.write(word + " : undefined\n");
                } else {
                    do {
                        bufferedWriter.write(word + " : " + resultSet.getString(3) + " (" +resultSet.getString(4)+ ")"+"\n");
                    } while(resultSet.next());
                }
            } catch (SQLException e) {
                bufferedWriter.write(word + " : undefined\n");
            }
        }
        bufferedWriter.close();
        statement.close();
        connection.close();
    }
}