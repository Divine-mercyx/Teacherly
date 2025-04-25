package org.Teacherly;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class BinaryTaskTest {

    @Test
    public void convertStringNumberToBinary_binaryTest() {
        BinaryTask binaryTask = new BinaryTask();
        String binary = binaryTask.convertToBinary("11", "10");
        assertEquals("101", binary);
    }
}