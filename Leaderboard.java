import java.io.*;
import java.util.*;

public class Leaderboard {
    private static final String FILE = "leaderboard.txt";
    private static final Map<String, Integer> scores = new HashMap<>();
    private static final Map<String, Long> times = new HashMap<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void loadLeaderboard() {
        try (Scanner fileScanner = new Scanner(new File(FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length >= 2) {
                    scores.put(data[0], Integer.parseInt(data[1]));
                    long time = (data.length >= 3) ? Long.parseLong(data[2]) : Long.MAX_VALUE;
                    times.put(data[0], time);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No leaderboard found; starting fresh.");
        }
    }

    public static void saveLeaderboard() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE))) {
            for (String user : scores.keySet()) {
                writer.println(user + "," + scores.get(user) + "," + (times.getOrDefault(user, Long.MAX_VALUE)));
            }
        } catch (IOException e) {
            System.out.println("Error saving leaderboard.");
        }
    }

    public static void addNewUser(String username) {
        scores.put(username, 0);
        times.put(username, Long.MAX_VALUE);
    }

    public static void updateScore(String username, int score, long time) {
        int prev = scores.getOrDefault(username, 0);
        long prevTime = times.getOrDefault(username, Long.MAX_VALUE);
        if (score > prev || (score == prev && time < prevTime)) {
            scores.put(username, score);
            times.put(username, time);
            System.out.println("New high score!");
            saveLeaderboard();
        } else {
            System.out.println("No new record. Keep trying!");
        }
    }

    public static void updateUsername(String oldUser, String newUser) {
        int score = scores.remove(oldUser);
        long time = times.remove(oldUser);
        scores.put(newUser, score);
        times.put(newUser, time);
    }

    public static void showLeaderboard() {
        System.out.println("\n\n                                Leaderboard                              ");
        scores.entrySet().stream()
                .sorted((a, b) -> {
                    int cmp = b.getValue().compareTo(a.getValue());
                    if (cmp != 0) return cmp;
                    return Long.compare(times.getOrDefault(a.getKey(), Long.MAX_VALUE), times.getOrDefault(b.getKey(), Long.MAX_VALUE));
                })
                .forEach(entry -> {
                    String user = entry.getKey();
                    System.out.println(user + " - " + scores.get(user) + " points - Time: " + (times.get(user) == Long.MAX_VALUE ? "N/A" : times.get(user) + " sec"));
                });

        System.out.print("\n1. Back to Main Menu: ");
        scanner.nextLine();
    }
}
