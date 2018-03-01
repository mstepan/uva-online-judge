import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * 
 */
public class Main {


    private Main(boolean debugMode) throws IOException {

        InputStream in = System.in;

        if (debugMode) {
            in = Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }

        try (Scanner sc = new Scanner(in)) {


        }
    }


    public static void main(String[] args) {
        try {
            new Main(args.length == 1);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
