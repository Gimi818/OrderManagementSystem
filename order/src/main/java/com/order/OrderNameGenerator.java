package com.order;

import java.util.Random;

public class OrderNameGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int NAME_LENGTH = 8;

    public static String generate() {
        Random random = new Random();
        StringBuilder name = new StringBuilder(NAME_LENGTH);
        for (int i = 0; i < NAME_LENGTH; i++) {
            name.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return name.toString();
    }
}
