package com.hm.oldiesbutgoodies.common.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PasswordUtil {

    private static final String SPECIAL_CHARS = "!@#$%^&*";


    public static boolean passwordValid(String pwd) {
        if (pwd.length() < 8 || pwd.length() > 20 || pwd == null) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char ch : pwd.toCharArray()) {
            if (Character.isLetter(ch)) {
                hasLetter = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else if (SPECIAL_CHARS.indexOf(ch) >= 0) {
                hasSpecialChar = true;
            }
        }
        return hasLetter && hasDigit && hasSpecialChar;
    }


    public static String passwordRandomCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            int n = random.nextInt(36);
            if (n > 25) {
                list.add(String.valueOf(n - 26));
            } else {
                list.add(String.valueOf((char) (n + 65)));
            }
        }

        char symbol = SPECIAL_CHARS.charAt(random.nextInt(SPECIAL_CHARS.length()));
        list.add(String.valueOf(symbol));

        //순서 섞기
        Collections.shuffle(list);

        for (String item : list) {
            code.append(item);
        }

        return code.toString();
    }

    public static String hashPassword(String pwd) {
        return BCrypt.hashpw(pwd, BCrypt.gensalt());
    }
}
