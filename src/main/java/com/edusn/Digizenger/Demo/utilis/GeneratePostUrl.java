package com.edusn.Digizenger.Demo.utilis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class GeneratePostUrl {

    @Value("${app.baseUrl}")
    private String baseUrl;



    public String generateSecurePostLinkUrl(int postIdOrShareId) {
        Set<Integer> randomUniqueNumbers = generateUniqueRandomNumbers(8, 0, 9);
        String uniqueNumberString = randomUniqueNumbers.toString().replaceAll("[\\[\\], ]", ""); // Convert set to string and remove brackets, commas, and spaces
        String token = UUID.randomUUID().toString(); // Generate a unique token
        return baseUrl + "/view/share/" + postIdOrShareId + "?token=" + token + "&uniqueCode=" + uniqueNumberString;
    }

    public static Set<Integer> generateUniqueRandomNumbers(int count, int min, int max) {
        Set<Integer> uniqueNumbers = new HashSet<>();

        while (uniqueNumbers.size() < count) {
            int randomNumber = (int)(Math.random() * (max - min + 1)) + min;
            uniqueNumbers.add(randomNumber);
        }

        return uniqueNumbers;
    }
}
