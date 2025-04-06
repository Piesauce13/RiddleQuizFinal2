import java.io.*;
import java.util.*;

public class UserManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String USERS_FILE = "users.txt";
    public static final Map<String, String> users = new HashMap<>();

    public static void loadUsers() {
        try (Scanner fileScanner = new Scanner(new File(USERS_FILE))) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                if (data.length >= 2) users.put(data[0], data[1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("No previous user data found. Starting fresh.");
        }
    }

    public static void saveUsers() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            users.forEach((user, pass) -> writer.println(user + "," + pass));
        } catch (IOException e) {
            System.out.println("Error saving users.");
        }
    }

    public static void signUp() {
        Console console = System.console();
        if (console == null) System.out.println("No console available. Running in IDE?");

        System.out.print("Enter a username: ");
        String username = scanner.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists. Try another one.");
            return;
        }

        char[] passwordArray = console != null ? console.readPassword("Enter a password: ") : scanner.nextLine().toCharArray();
        String password = new String(passwordArray);

        users.put(username, password);
        Leaderboard.addNewUser(username);
        saveUsers();
        Leaderboard.saveLeaderboard();
        System.out.println("Sign-up successful! You can now log in.");
    }

    public static String login() {
        Console console = System.console();
        if (console == null) System.out.println("No console available. Running in IDE?");

        while (true) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            if (!users.containsKey(username)) {
                System.out.println("Username not found. Try again.");
                continue;
            }

            char[] passwordArray = console != null ? console.readPassword("Enter password: ") : scanner.nextLine().toCharArray();
            String password = new String(passwordArray);

            if (users.get(username).equals(password)) {
                System.out.println("Login successful! Welcome, " + username + "!");
                return username;
            } else {
                System.out.println("Invalid password. Try again.");
            }
        }
    }

    public static void editProfile(String username) {
        System.out.println("\n\n                                Edit Profile                                ");
        System.out.println("1. Change Username");
        System.out.println("2. Change Password");
        System.out.println("3. Back to Main Menu");
        System.out.print("Choose an option: ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    changeUsername(username);
                    break;
                case 2:
                    changePassword(username);
                    break;
                case 3:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number (1-3).");
        }
    }

    private static void changeUsername(String currentUsername) {
        System.out.print("Enter new username: ");
        String newUsername = scanner.nextLine();

        if (users.containsKey(newUsername)) {
            System.out.println("Username already exists. Try again.");
            return;
        }

        String password = users.remove(currentUsername);
        users.put(newUsername, password);
        Leaderboard.updateUsername(currentUsername, newUsername);
        saveUsers();
        Leaderboard.saveLeaderboard();

        System.out.println("Username changed successfully to: " + newUsername);
        System.out.println("Please login again with your new username.");
        Main.showWelcomeScreen();
    }

    private static void changePassword(String username) {
        Console console = System.console();
        if (console == null) System.out.println("No console available. Running in IDE?");

        while (true) {
            System.out.print("Enter current password: ");
            String currentPassword = new String(console != null ? console.readPassword() : scanner.nextLine().toCharArray());

            if (!users.get(username).equals(currentPassword)) {
                System.out.println("Incorrect password. Try again.");
                continue;
            }

            System.out.print("Enter new password: ");
            String newPassword = new String(console != null ? console.readPassword() : scanner.nextLine().toCharArray());

            if (newPassword.equals(currentPassword)) {
                System.out.println("New password cannot be same as current. Try again.");
                continue;
            }

            System.out.print("Confirm new password: ");
            String confirmPassword = new String(console != null ? console.readPassword() : scanner.nextLine().toCharArray());

            if (!newPassword.equals(confirmPassword)) {
                System.out.println("Passwords do not match. Try again.");
                continue;
            }

            users.put(username, newPassword);
            saveUsers();
            System.out.println("Password changed successfully");
            System.out.println("Please log in again with your new password.");
            Main.showWelcomeScreen();
            return;
        }
    }
}
