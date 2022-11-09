package org.max.uva.solved;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 11172 - Relational Operator
 */
public class Uva_11172 {

    private static String getRelation(int a, int b) {
        if (a > b) {
            return ">";
        }
        if (b > a) {
            return "<";
        }
        return "=";
    }

    private Uva_11172(boolean debugMode) throws IOException {

        InputStream in = System.in;

        if (debugMode) {
            in = Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }

        try (Scanner sc = new Scanner(in)) {

            int testsCount = sc.nextInt();

            for (int i = 0; i < testsCount; ++i) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                System.out.println(getRelation(a, b));
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Uva_11172(args.length == 1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
