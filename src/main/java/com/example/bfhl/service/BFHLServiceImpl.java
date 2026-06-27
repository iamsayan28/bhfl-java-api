package com.example.bfhl.service;

import com.example.bfhl.dto.RequestDTO;
import com.example.bfhl.dto.ResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BFHLServiceImpl implements BFHLService {

    // --- Hardcoded user identity fields ---
    private static final String USER_ID     = "sayan_mandal_28042005";
    private static final String EMAIL       = "iamsayan277@gmail.com";
    private static final String ROLL_NUMBER = "2310990883";

    @Override
    public ResponseDTO process(RequestDTO request) {
        List<String> data = request.getData();

        List<String> oddNumbers       = new ArrayList<>();
        List<String> evenNumbers      = new ArrayList<>();
        List<String> alphabets        = new ArrayList<>();
        List<String> specialChars     = new ArrayList<>();
        long         numericSum       = 0;
        // Collect ALL individual alphabetic characters (one per char in each token)
        List<Character> allAlphaChars = new ArrayList<>();

        for (String token : data) {
            if (isNumber(token)) {
                long val = Long.parseLong(token);
                numericSum += val;
                if (val % 2 == 0) {
                    evenNumbers.add(token);
                } else {
                    oddNumbers.add(token);
                }
            } else if (isAlpha(token)) {
                // Add entire token uppercased to alphabets array
                alphabets.add(token.toUpperCase());
                // Collect each character individually for concat_string
                for (char c : token.toCharArray()) {
                    if (Character.isLetter(c)) {
                        allAlphaChars.add(c);
                    }
                }
            } else {
                // Special character token
                specialChars.add(token);
            }
        }

        String concatString = buildConcatString(allAlphaChars);

        return ResponseDTO.builder()
                .isSuccess(true)
                .userId(USER_ID)
                .email(EMAIL)
                .rollNumber(ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialChars)
                .sum(String.valueOf(numericSum))
                .concatString(concatString)
                .build();
    }

    /**
     * Returns true if every character in the token is a digit
     * (handles multi-digit numbers like "334").
     */
    private boolean isNumber(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    /**
     * Returns true if every character in the token is a letter
     * (handles multi-char strings like "ABCD").
     */
    private boolean isAlpha(String token) {
        if (token == null || token.isEmpty()) return false;
        for (char c : token.toCharArray()) {
            if (!Character.isLetter(c)) return false;
        }
        return true;
    }

    /**
     * Builds concat_string:
     * - Take all alphabetic characters from input (preserving order)
     * - Reverse the list
     * - Apply alternating caps: index 0 → uppercase, index 1 → lowercase, ...
     *
     * Example: input chars [a, y, b]  →  reversed [b, y, a]  →  "ByA"
     */
    private String buildConcatString(List<Character> chars) {
        // Reverse
        List<Character> reversed = new ArrayList<>(chars);
        java.util.Collections.reverse(reversed);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < reversed.size(); i++) {
            char c = reversed.get(i);
            if (i % 2 == 0) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}