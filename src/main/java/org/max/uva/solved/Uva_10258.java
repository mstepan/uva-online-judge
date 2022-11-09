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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 10258 - Contest Scoreboard
 */
public class Uva_10258 {


    private Uva_10258() throws IOException, InterruptedException {

        InputStream in = createInput();
        PrintStream out = createOutput();

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(in))) {

            final int testCases = Integer.parseInt(rd.readLine().trim());
            rd.readLine();

            for (int testIndex = 0; testIndex < testCases; ++testIndex) {

                BoardStat board = new BoardStat();

                while (true) {
                    String line = rd.readLine();

                    if (line == null || "".equals(line)) {
                        break;
                    }

                    line = line.trim();

                    String[] testData = line.split("\\s+");

                    // contestant problem time status
                    // 1 2 10 I
                    final int userId = Integer.parseInt(testData[0].trim());
                    final int problemId = Integer.parseInt(testData[1].trim());
                    final int time = Integer.parseInt(testData[2].trim());

                    // C, I
                    final String status = testData[3].trim();

                    board.update(userId, problemId, time, status);
                }

                List<UserStat> usersStats = board.generateStats();

                if (testIndex > 0) {
                    out.println();
                }

                for (UserStat stat : usersStats) {
                    out.printf("%d %d %d%n", stat.userId, stat.solvedTasks, stat.penalty);
                }
            }

            diff();
        }
    }

    private static class BoardStat {

        final Map<Integer, Set<Integer>> solvedProblemsPerUser = new HashMap<>();
        final Map<UserAndProblem, Integer> incorrectSubmits = new HashMap<>();
        final Map<UserAndProblem, Integer> penaltyValue = new HashMap<>();

        final Set<Integer> usersWhoSubmitted = new HashSet<>();

        void update(int userId, int problemId, int time, String status) {

            usersWhoSubmitted.add(userId);

            if ("C".equals(status)) {
                solvedProblemsPerUser.putIfAbsent(userId, new HashSet<>());

                Set<Integer> solvedSet = solvedProblemsPerUser.get(userId);

                if (!solvedSet.contains(problemId)) {
                    solvedSet.add(problemId);

                    UserAndProblem userProblem = new UserAndProblem(userId, problemId);

                    int penalty = time + 20 * incorrectSubmits.getOrDefault(userProblem, 0);

                    penaltyValue.put(userProblem, penalty);
                }
            }
            else if ("I".equals(status)) {
                UserAndProblem key = new UserAndProblem(userId, problemId);
                incorrectSubmits.compute(key, (userAndProblem, cnt) -> cnt == null ? 1 : cnt + 1);
            }
        }

        List<UserStat> generateStats() {

            Set<Integer> processedUsers = new HashSet<>();

            List<UserStat> usersStats = new ArrayList<>();

            for (Map.Entry<Integer, Set<Integer>> singleUserStat : solvedProblemsPerUser.entrySet()) {

                final int userId = singleUserStat.getKey();

                usersStats.add(
                        new UserStat(
                                userId,
                                singleUserStat.getValue().size(),
                                combinePenalty(userId, singleUserStat.getValue())));

                processedUsers.add(userId);
            }

            for (int userId : usersWhoSubmitted) {
                if (!processedUsers.contains(userId)) {
                    usersStats.add(new UserStat(userId, 0, 0));
                    processedUsers.add(userId);
                }
            }

            usersStats.sort(UserStat.SOLVED_DESC_PENALTY_ASC_CMP);
            return usersStats;
        }

        private int combinePenalty(int userId, Set<Integer> solvedProblems) {

            int totalPenalty = 0;

            for (int problemId : solvedProblems) {
                totalPenalty += penaltyValue.get(new UserAndProblem(userId, problemId));
            }

            return totalPenalty;
        }
    }


    private static class UserAndProblem {
        final int userId;
        final int problemId;

        UserAndProblem(int userId, int problemId) {
            this.userId = userId;
            this.problemId = problemId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserAndProblem that = (UserAndProblem) o;
            return userId == that.userId &&
                    problemId == that.problemId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, problemId);
        }
    }

    private static class UserStat {

        static final Comparator<UserStat> SOLVED_DESC_PENALTY_ASC_CMP = (first, second) -> {

            int cmp = -Integer.compare(first.solvedTasks, second.solvedTasks);

            if (cmp != 0) {
                return cmp;
            }

            cmp = Integer.compare(first.penalty, second.penalty);

            if (cmp != 0) {
                return cmp;
            }

            return Integer.compare(first.userId, second.userId);
        };

        final int userId;
        final int solvedTasks;
        final int penalty;

        UserStat(int userId, int solvedTasks, int penalty) {
            this.userId = userId;
            this.solvedTasks = solvedTasks;
            this.penalty = penalty;
        }


        @Override
        public String toString() {
            return userId + " " + solvedTasks + " " + penalty;
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
            new Uva_10258();
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
