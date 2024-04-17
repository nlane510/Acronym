package com.acronym_finder.maven_acronym_finder;

import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class IntegratedClassTest {

    private IntegratedClass testClass;
    private final String testTextFileName = "testDoc.txt";

    @Before
    public void setUp() {
        testClass = new IntegratedClass();
        // Ensure that the testTextFileName exists in the correct directory for these tests
    }

    @Test
    public void testTokenizeFile() {
        Map<String, Integer> result = IntegratedClass.tokenizeFile(testTextFileName);
        assertNotNull("Checking that the result is not null", result);
        // More detailed tests can follow here, depending on the expected output
    }

    @Test
    public void testHasConsecutiveCapitalLetters() {
        assertTrue("Checking an acronym", IntegratedClass.hasConsecutiveCapitalLetters("TEST"));
        assertFalse("Checking a non-acronym", IntegratedClass.hasConsecutiveCapitalLetters("test"));
    }

    @Test
    public void testExecuteDbSearch() throws Exception {
        // Note, this test will require you to mock/stub out database access as in the executeDbSearch method,
        // or it will have to connect to a test database.
        // Here is a basic test outline:
        Map<String, Integer> testInput = new HashMap<>();
        testInput.put("TEST", 1);
        IntegratedClass.executeDbSearch(testInput);

        // Check that the file "Results.txt" exists and the content is as expected.

        File resultsFile = new File("Results.txt");
        assertTrue("Check if results file exists", resultsFile.exists());

        String resultsContent = new String(Files.readAllBytes(resultsFile.toPath()));
        // Check that the resultsContent is as expected here
    }
}