import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserManager.loadUsers();
        Leaderboard.loadLeaderboard();
        QuizManager.loadQuestions();

        showWelcomeScreen();
    }

    public static void showWelcomeScreen() {
        System.out.println("---------------------- Welcome to Riddle Quiz Game! ----------------------");
        System.out.println("\"Test your wits with challenging riddles across different difficulty levels.\"");

        while (true) {
            System.out.print("\nDo you have an account yet?\nEnter (Yes/No): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes") || response.equals("y")) {
                String username = UserManager.login();
                if (username != null) showMainMenu(username);
            } else if (response.equals("no") || response.equals("n")) {
                UserManager.signUp();
            } else {
                System.out.println("Invalid input. Please enter 'Yes' or 'No'.");
            }
        }
    }

    private static void showMainMenu(String username) {
        while (true) {
            System.out.println("\n\n                                Main Menu                                ");
            System.out.println("1. Play Quiz");
            System.out.println("2. View Leaderboard");
            System.out.println("3. Edit Profile");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        QuizManager.playQuiz(username);
                        break;
                    case 2:
                        Leaderboard.showLeaderboard();
                        break;
                    case 3:
                        UserManager.editProfile(username);
                        break;
                    case 4:
                        System.out.println("Logging out... Goodbye, " + username + "!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1-4).");
            }
        }
    }
}
