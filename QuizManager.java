import java.io.*;
import java.util.*;

public class QuizManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String QUESTIONS_FILE = "questions.txt";
    private static final List<Question> questions = new ArrayList<>();

    public static void loadQuestions() {
        try (Scanner fileScanner = new Scanner(new File(QUESTIONS_FILE))) {
            int qNum = 1;
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                for (int i = 0; i < parts.length; i++)
                    parts[i] = parts[i].trim().replaceAll("^\"|\"$", "");

                if (parts.length >= 6) {
                    String difficulty = parts[0];
                    String text = parts[1];
                    String[] options = Arrays.copyOfRange(parts, 2, parts.length - 1);
                    char correctAnswer = parts[parts.length - 1].charAt(0);
                    questions.add(new Question(qNum++, difficulty, text, options, correctAnswer));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No questions found. Please add them to " + QUESTIONS_FILE);
        }
    }

    public static void playQuiz(String username) {
        if (questions.isEmpty()) {
            System.out.println("No questions loaded.");
            return;
        }

        Map<String, List<Question>> byDifficulty = new HashMap<>();
        byDifficulty.put("Easy", new ArrayList<>());
        byDifficulty.put("Medium", new ArrayList<>());
        byDifficulty.put("Hard", new ArrayList<>());

        for (Question q : questions)
            byDifficulty.getOrDefault(q.getDifficulty(), new ArrayList<>()).add(q);

        long start = System.currentTimeMillis();
        int totalScore = 0, totalQs = 0;

        for (String diff : new String[]{"Easy", "Medium", "Hard"}) {
            List<Question> qList = byDifficulty.get(diff);
            if (qList == null || qList.isEmpty()) continue;

            System.out.println("\nStarting " + diff + " questions:");
            Collections.shuffle(qList);
            List<Question> selected = qList.subList(0, Math.min(3, qList.size()));

            int qNum = 1;
            int diffScore = 0;
            for (Question q : selected) {
                System.out.println("\n" + qNum++ + ". (" + q.getDifficulty() + ") " + q.getText());
                char optionChar = 'A';
                for (String opt : q.getOptions()) {
                    System.out.println(optionChar + ". " + opt);
                    optionChar++;
                }

                char answer;
                while (true) {
                    System.out.print("Your answer (A, B, C) or Q to quit: ");
                    String input = scanner.nextLine().trim().toUpperCase();

                    if (input.equals("Q")) {
                        System.out.println("Exiting quiz. Progress lost.");
                        return;
                    }

                    if (input.matches("[ABC]")) {
                        answer = input.charAt(0);
                        break;
                    }

                    System.out.println("Invalid input. Try A, B, or C.");
                }

                if (answer == q.getCorrectAnswer()) {
                    System.out.println("Correct!");
                    diffScore++;
                } else {
                    System.out.println("Wrong! Correct answer: " + q.getCorrectAnswer());
                }
            }

            totalScore += diffScore;
            totalQs += selected.size();

            System.out.println("Completed " + diff + ". Score: " + diffScore + "/" + selected.size());
        }

        long end = System.currentTimeMillis();
        long timeTaken = (end - start) / 1000;

        System.out.println("\nQuiz complete! Total score: " + totalScore + "/" + totalQs);
        System.out.println("Time taken: " + timeTaken + " seconds");

        Leaderboard.updateScore(username, totalScore, timeTaken);
    }
}
