package com.url_service.url_service.utils;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class URLUtils {
    Map<Integer, Character> charMap = new HashMap<>();

    public void createBinaryCharacterMap() {

        int keyCounter = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            this.charMap.put(keyCounter, c);
            keyCounter++;
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            this.charMap.put(keyCounter, c);
            keyCounter++;
        }

        for (int c = 0; c <= 9; c++) {
            // Convert the integer i to its character representation
            char digitChar = (char) (c + '0');

            this.charMap.put(keyCounter, digitChar);
            keyCounter++;

        }

    }

    public String BinaryMultipleOfSix(String binaryString) {
        int length = binaryString.length();
        int diff = 6 - (length % 6);
        if (diff != 6) {
            StringBuilder prefix = new StringBuilder();
            for (int i = 0; i < diff; i++) {
                prefix.append('0');
            }
            return prefix + binaryString;
        }
        return binaryString;
    }

    public String IDToURLGenerator(Integer id) {
        if(charMap.isEmpty()){
            createBinaryCharacterMap();
        }
        String BinanryNumber = Integer.toBinaryString(id);
        BinanryNumber=BinaryMultipleOfSix(BinanryNumber);

        StringBuilder shortUrl = new StringBuilder();

        // Process 6 bits at a time
        for (int i = 0; i < BinanryNumber.length(); i += 6) {
            String chunk = BinanryNumber.substring(i, i + 6);
            int decimalValue = Integer.parseInt(chunk, 2); // Convert binary to int
            char mappedChar = charMap.get(decimalValue);   // Map to character
            shortUrl.append(mappedChar);
        }

        return shortUrl.toString();
    }
}
