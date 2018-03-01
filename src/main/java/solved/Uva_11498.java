package solved;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 11498 - Division of Nlogonia
 */
public class Uva_11498 {


    private Uva_11498(boolean debugMode) throws IOException {

        InputStream in = System.in;

        if (debugMode) {
            in = Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }

        try (Scanner sc = new Scanner(in)) {


            while (true) {

                final int queriesCount = sc.nextInt();

                if (queriesCount == 0) {
                    break;
                }

                final int baseX = sc.nextInt();
                final int baseY = sc.nextInt();

                for (int i = 0; i < queriesCount; ++i) {
                    int x = sc.nextInt();
                    int y = sc.nextInt();

                    String name = divisionName(baseX, baseY, x, y);
                    System.out.println(name);
                }
            }
        }
    }

    private static String divisionName(int baseX, int baseY, int x, int y) {

        if (x == baseX || y == baseY) {
            return "divisa";
        }

        StringBuilder res = new StringBuilder();

        if (y > baseY) {
            res.append("N");
        }
        else {
            res.append("S");
        }

        if (x > baseX) {
            res.append("E");
        }
        else {
            res.append("O");
        }

        return res.toString();
    }


    public static void main(String[] args) {
        try {
            new Uva_11498(args.length == 1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
