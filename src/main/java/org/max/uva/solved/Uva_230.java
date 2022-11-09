package org.max.uva.solved;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Borrowers.
 * <p>
 * https://vjudge.net/problem/UVA-230
 * <p>
 * Profile usage:
 * <p>
 * -agentpath:/Users/mstepan/repo/async-profiler/build/libasyncProfiler.so=start,svg,
 * file=/Users/mstepan/repo/uva-online-judge/src/main/java/uva-profile.svg,event=cpu
 */
public class Uva_230 {

    private Uva_230() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            List<Book> allBooks = new ArrayList<>();

            Map<String, Book> booksToAuthor = new HashMap<>();

            String line = rd.readLine().trim();

            while (!"END".equals(line)) {

                String[] data = line.split("by");

                String title = data[0].replace('"', ' ').trim();
                String author = data[1].trim();

                Book book = new Book(author, title);

                booksToAuthor.put(title, book);
                allBooks.add(book);
                line = rd.readLine().trim();
            }

            allBooks.sort(Book.AUTHOR_TITLE_ASC);

            line = rd.readLine().trim();

            List<Book> returnedBooks = new ArrayList<>();

            while (!"END".equals(line)) {

                if ("SHELVE".equals(line)) {
                    handleShelve(out, allBooks, returnedBooks);
                    returnedBooks.clear();
                }
                else {

                    int firstSpace = line.indexOf(' ');

                    String command = line.substring(0, firstSpace);
                    String title = line.substring(firstSpace + 1).replace('"', ' ').trim();

                    Book book = booksToAuthor.get(title);

                    if ("RETURN".equals(command)) {
                        returnedBooks.add(book);
                    }
                    else if ("BORROW".equals(command)) {
                        book.borrow();
                    }
                    else {
                        throw new IllegalArgumentException("Can't properly parse command");
                    }
                }

                line = rd.readLine().trim();
            }

            diff();
        }
    }

    private void handleShelve(PrintStream out, List<Book> allBooks, List<Book> returnedBooks) {

        assert allBooks.size() >= returnedBooks.size();

        if (returnedBooks.isEmpty()) {
            out.println("END");
            return;
        }

        returnedBooks.sort(Book.AUTHOR_TITLE_ASC);

        Iterator<Book> mainIt = allBooks.iterator();
        Iterator<Book> returnedIt = returnedBooks.iterator();

        Book returnedBook = returnedIt.next();

        Book prevOnShelve = null;

        while (returnedBook != null && mainIt.hasNext()) {

            Book mainBook = mainIt.next();

            if (mainBook.equals(returnedBook)) {

                if (prevOnShelve == null) {
                    out.println("Put \"" + returnedBook.title + "\" first");
                }
                else {
                    out.println("Put \"" + returnedBook.title + "\" after \"" + prevOnShelve.title + "\"");
                }

                returnedBook.returnToShelve();
                prevOnShelve = returnedBook;

                returnedBook = returnedIt.hasNext() ? returnedIt.next() : null;
            }
            else {
                if (mainBook.onShelve) {
                    prevOnShelve = mainBook;
                }
            }
        }

        out.println("END");
    }

    private static final class Book {

        private static final Comparator<Book> AUTHOR_TITLE_ASC =
                Comparator.comparing(Book::getAuthor).
                        thenComparing(Book::getTitle);

        final String author;
        final String title;
        boolean onShelve;

        Book(String author, String title) {
            this.author = author;
            this.title = title;
            this.onShelve = true;
        }

        String getAuthor() {
            return author;
        }

        String getTitle() {
            return title;
        }

        void borrow() {
            this.onShelve = false;
        }

        void returnToShelve() {
            this.onShelve = true;
        }

        @Override
        public String toString() {
            return author + ", " + title;
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


        StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
        }
    }

}
