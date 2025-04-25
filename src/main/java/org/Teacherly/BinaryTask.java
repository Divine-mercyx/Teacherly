package org.Teacherly;

import java.util.Arrays;

public class BinaryTask {


    public String convertToBinary(String number1, String number2) {
        int sum = Integer.parseInt(number1, 2) + Integer.parseInt(number2, 2);
        String binary = "";
        while (sum > 0) {
            int remainder = sum % 2;
            binary = remainder + binary;
            sum = sum / 2;
        }
        return binary;
    }
}
