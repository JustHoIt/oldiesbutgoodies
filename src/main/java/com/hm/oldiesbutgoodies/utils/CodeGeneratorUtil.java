package com.hm.oldiesbutgoodies.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodeGeneratorUtil {

    public static String sixLetterRandomCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int n = random.nextInt(36);
            if (n > 25) {
                list.add(String.valueOf(n - 25));
            } else {
                list.add(String.valueOf((char) (n + 65)));
            }
        }

        for (String item : list) {
            code.append(item);
        }

        return code.toString();
    }
}
