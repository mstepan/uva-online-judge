package solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * 230 - Borrowers
 */
public class Uva_230 {


    private static final String TITLE_AUTHOR_SEPARATOR = "\" by ";

    private Uva_230() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            List<Book> allBooks = readAllBooks(rd);
            allBooks.sort(Book.AUTHOR_TITLE_ASC);
            setIndexes(allBooks);

            Map<Integer, Book> indexToBookMap = createIndexToBookMap(allBooks);

            Map<String, Book> titleBookMap = createTitleMap(allBooks);

            BitSet shelveState = new BitSet(allBooks.size());
            shelveState.set(0, allBooks.size(), true);

            List<Book> returnedBooks = new ArrayList<>();

            while (true) {

                String line = rd.readLine().trim();

                if ("END".equals(line)) {
                    break;
                }

                // BORROW title
                // RETURN title
                // SHELVE

                if ("SHELVE".equals(line)) {
                    handleShelveCommand(returnedBooks, shelveState, indexToBookMap, out);
                }
                else {

                    String[] data = splitCommandAndTitle(line);

                    Book singleBook = titleBookMap.get(data[1]);

                    if ("BORROW".equals(data[0])) {
                        shelveState.clear(singleBook.index);
                    }
                    else {
                        returnedBooks.add(singleBook);
                    }
                }

            }

            diff();
        }
    }

    private static void setIndexes(List<Book> allBooks) {

        int i = 0;

        for (Book singleBook : allBooks) {
            singleBook.index = i;
            ++i;
        }
    }

    private static Map<Integer, Book> createIndexToBookMap(List<Book> allBooks) {
        Map<Integer, Book> indexToBookMap = new HashMap<>();
        for (Book book : allBooks) {
            indexToBookMap.put(book.index, book);
        }
        return indexToBookMap;
    }

    private static Map<String, Book> createTitleMap(List<Book> allBooks) {
        Map<String, Book> titleBookMap = new HashMap<>();

        for (Book singleBook : allBooks) {
            titleBookMap.put(singleBook.title, singleBook);
        }
        return titleBookMap;
    }

    private static String[] splitCommandAndTitle(String line) {
        int index = line.indexOf(' ');

        String command = line.substring(0, index);
        String title = line.substring(index + 2, line.length() - 1);
        return new String[]{command, title};
    }

    private List<Book> readAllBooks(BufferedReader rd) throws IOException {


        List<Book> allBooks = new ArrayList<>();

        while (true) {

            String line = rd.readLine().trim();

            if ("END".equals(line)) {
                return allBooks;
            }
            int sepIndex = line.indexOf(TITLE_AUTHOR_SEPARATOR);

            String title = line.substring(1, sepIndex);
            String author = line.substring(sepIndex + TITLE_AUTHOR_SEPARATOR.length());

            Book book = new Book(title, author);

            allBooks.add(book);
        }
    }

    private static void handleShelveCommand(List<Book> returnedBooks, BitSet shelveState,
                                            Map<Integer, Book> indexToBookMap, PrintStream out) {
        returnedBooks.sort(Book.AUTHOR_TITLE_ASC);

        for (Book bookToPutBack : returnedBooks) {

            int prevIndex = shelveState.previousSetBit(bookToPutBack.index - 1);

            if (prevIndex == -1) {
                out.printf("Put \"%s\" first%n", bookToPutBack.title);
            }
            else {
                Book previousBook = indexToBookMap.get(prevIndex);
                out.printf("Put \"%s\" after \"%s\"%n", bookToPutBack.title, previousBook.title);
            }

            shelveState.set(bookToPutBack.index);
        }

        returnedBooks.clear();

        out.println("END");
    }

    private static class Book {

        static final Comparator<Book> AUTHOR_TITLE_ASC = (b1, b2) -> {

            int cmp = b1.author.compareTo(b2.author);

            if (cmp != 0) {
                return cmp;
            }

            return b1.title.compareTo(b2.title);
        };

        final String title;
        final String author;
        int index;

        Book(String title, String author) {
            this.title = title;
            this.author = author;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Book book = (Book) o;
            return Objects.equals(title, book.title) &&
                    Objects.equals(author, book.author);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, author);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // DEBUG part
    //------------------------------------------------------------------------------------------------------------------

    private static boolean DEBUG;

    private static InputStream createInput() throws IOException {
        if (DEBUG) {
            return Files.newInputStream(Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/in.txt"));
        }
        return System.in;
    }

    private static PrintStream createOutput() throws IOException {
        if (DEBUG) {
            return new PrintStream(Files.newOutputStream(
                    Paths.get("/Users/mstepan/repo/uva-online-judge/src/main/java/out-actual.txt")));
        }
        return System.out;
    }

    private static void diff() throws IOException, InterruptedException {

        if (!DEBUG) {
            return;
        }

        Process process = Runtime.getRuntime()
                .exec(String.format("/usr/bin/diff %s %s",
                        "/Users/mstepan/repo/uva-online-judge/src/main/java/out.txt",
                        "/Users/mstepan/repo/uva-online-judge/src/main/java/out-actual.txt"));

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);

        Thread th = new Thread(streamGobbler);
        th.start();
        th.join();

        process.waitFor();
    }

    public static void main(String[] args) {
        try {
            DEBUG = (args.length == 1);
            new Uva_230();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static class StreamGobbler implements Runnable {

        private InputStream inputStream;

        private Consumer<String> consumer;


        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }

}
