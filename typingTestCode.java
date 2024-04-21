import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

public class TypingTest {
    static String[] easy = {
            "The quick brown fox jumps over the lazy dog in the park.",
            "Mary had a little lamb, its fleece was white as snow.",
            "The sun sets in the west, painting the sky with hues of orange and pink."
    };

    static String[] medium = {
            "The world is full of wonders waiting to be explored and discovered and you shocked when you discovered it.",
            "Life is like a journey, filled with twists and turns along the way so make sure to enjoy its every moment.",
            "Time flies by quickly, so cherish every moment, make it count, be responsible and adopt the right path."
    };

    static String[] hard = {
            "In the depths of despair, where shadows linger and hope seems faint, summon the resilience within to ascend, radiating like the sun, illuminating paths of possibility and casting out darkness.",
            "Life's greatest lessons, woven into the fabric of existence, emerge from the crucible of pain and adversity, shaping our souls, guiding us through the labyrinth of experience, and illuminating the path to resilience and growth.",
            "The journey of a thousand miles, a tapestry woven with dreams and aspiration, unfurls with the courage of that single step forward, igniting the path of discovery, and transformation across boundless horizons."
    };

    static List<TestResult> results = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=====================================");
        System.out.println("|        Welcome to Typing Test      |");
        System.out.println("=====================================");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String userName = scanner.nextLine();

        while (true) {
            displayMainMenu();
            int choice = getIntegerInput(scanner);

            switch (choice) {
                case 1:
                    startTypingTest(scanner, userName);
                    break;
                case 2:
                    viewResults();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close(); // Closing the scanner
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    public static void displayMainMenu() {
        System.out.println("\n================ Main Menu ================");
        System.out.println("| 1. Start Typing Test                   |");
        System.out.println("| 2. View Results                        |");
        System.out.println("| 3. Exit                                |");
        System.out.println("==========================================");
        System.out.print("Enter your choice: ");
    }

    public static int getIntegerInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }

    public static void startTypingTest(Scanner scanner, String userName) throws InterruptedException {
        while (true) {
            displayDifficultyMenu();
            int choice = getIntegerInput(scanner);
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    attemptTypingTest(scanner, userName, easy);
                    break;
                case 2:
                    attemptTypingTest(scanner, userName, medium);
                    break;
                case 3:
                    attemptTypingTest(scanner, userName, hard);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    public static void displayDifficultyMenu() {
        System.out.println("\n=============== Difficulty ===============");
        System.out.println("| 1. Easy                                 |");
        System.out.println("| 2. Medium                               |");
        System.out.println("| 3. Hard                                 |");
        System.out.println("| Enter your choice                       |");
        System.out.println("===========================================");
        System.out.print("|OR  Press  4 To Go Goback to Main Menu \n");
    }

    public static void attemptTypingTest(Scanner scanner, String userName, String[] sentences) throws InterruptedException {
        int attempts = 3;
        while (attempts > 0) {
            System.out.println("\n============= Attempt " + (4 - attempts) + " =============");
            printCountdown();
            String targetSentence = printRandomSentence(sentences);
            double startTime = LocalTime.now().toNanoOfDay();

            String typedSentence = scanner.nextLine();

            double endTime = LocalTime.now().toNanoOfDay();
            double elapsedTime = (endTime - startTime) / 1_000_000_000.0;
            int numChars = typedSentence.length();

            int wpm = calculateWPM(numChars, elapsedTime);
            int accuracy = calculateAccuracy(typedSentence, targetSentence);
            int errors = targetSentence.length() - numChars;

            TestResult result = new TestResult(userName, wpm, accuracy, errors);
            results.add(result);

            System.out.println("\n===== Test Result =====");
            System.out.println("Your typing speed is: " + wpm + " wpm!");
            System.out.println("Accuracy: " + accuracy + "%");
            System.out.println("Errors: " + errors);

            if (accuracy < 90) {
                System.out.println("You need to improve your accuracy. Try again!");
                attempts--;
            } else {
                System.out.println("Congratulations! You have good accuracy.");
                break;
            }
        }
    }

    public static void printCountdown() throws InterruptedException {
        System.out.println("Starting in:");
        for (int i = 3; i > 0; i--) {
            System.out.println(i);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public static String printRandomSentence(String[] sentences) {
        Random rand = new Random();
        String sentence = sentences[rand.nextInt(sentences.length)];
        System.out.println("Type the following sentence:");
        System.out.println(sentence);
        return sentence;
    }

    public static int calculateWPM(int numChars, double elapsedTime) {
        return (int) ((((double) numChars / 5) / elapsedTime) * 60);
    }

    public static int calculateAccuracy(String typedSentence, String targetSentence) {
        int commonChars = 0;
        for (int i = 0; i < Math.min(typedSentence.length(), targetSentence.length()); i++) {
            if (typedSentence.charAt(i) == targetSentence.charAt(i)) {
                commonChars++;
            }
        }
        return (int) ((double) commonChars / targetSentence.length() * 100);
    }

    public static void viewResults() {
        System.out.println("\n============== Test Results ==============");
        if (results.isEmpty()) {
            System.out.println("No results available.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println("Attempt " + (i + 1) + ": " + results.get(i));
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press any key to go back to the main menu.");
        scanner.nextLine();
    }

    static class TestResult {
        private String userName;
        private int wpm;
        private int accuracy;
        private int errors;

        public TestResult(String userName, int wpm, int accuracy, int errors) {
            this.userName = userName;
            this.wpm = wpm;
            this.accuracy = accuracy;
            this.errors = errors;
        }

        @Override
        public String toString() {
            return "User: " + userName + ", WPM: " + wpm + ", Accuracy: " + accuracy + "%, Errors: " + errors;
        }
    }
}
