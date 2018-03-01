package solved;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 11727 - Cost Cutting
 */
public class Uva_11727 {


    private Uva_11727(boolean debugMode) throws IOException {

        InputStream in = System.in;

        if (debugMode) {
            in = Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }

        try (Scanner sc = new Scanner(in)) {

            final int testsCount = sc.nextInt();

            for (int i = 0; i < testsCount; ++i) {
                int[] arr = new int[3];
                for (int k = 0; k < arr.length; ++k) {
                    arr[k] = sc.nextInt();
                }

                Arrays.sort(arr);

                System.out.printf("Case %d: %d%n", i + 1, arr[1]);
            }

        }
    }


    public static void main(String[] args) {
        try {
            new Uva_11727(args.length == 1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
