package org.sdu.sem4.g7.main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TestAssert {

    @Test
    public void testExample() {
        // Arrange
        int expected = 42;

        // Act
        int actual = 40 + 2;

        // Assert
        assertEquals(expected, actual, "The actual value did not match the expected value.");
    }
}