package com;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * InputHelper.java
 * Handles all user input, validation, and formatting for the Library Management System.
 * Role: Input & Validation Engineer
 */
public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    // ─────────────────────────────────────────────
    //  REGEX PATTERNS
    // ─────────────────────────────────────────────
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Z\\s'\\-]{2,100}$");

    private static final Pattern ISBN_PATTERN =
            Pattern.compile("^(97[89])?\\d{9}[\\dX]$");

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^(09|\\+639)\\d{9}$");

    private static final Pattern DATE_PATTERN =
            Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ─────────────────────────────────────────────
    //  PRIMITIVE INPUT READERS
    // ─────────────────────────────────────────────

    /**
     * Reads a non-empty trimmed string from the user.
     * Keeps prompting until the user types something.
     */
    public static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            printError("Input cannot be empty. Please try again.");
        }
    }

    /**
     * Reads a string but allows an empty/blank response (returns "").
     */
    public static String readOptionalString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Reads a positive integer. Rejects anything that is not a whole number > 0.
     */
    public static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(raw);
                if (value > 0) return value;
                printError("Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                printError("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads any integer (including 0 and negatives — useful for menu choices).
     */
    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                printError("Invalid input. Please enter a whole number.");
            }
        }
    }

    /**
     * Reads a double / decimal value greater than 0 (e.g. fine amount, price).
     */
    public static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(raw);
                if (value > 0) return value;
                printError("Please enter a value greater than 0.");
            } catch (NumberFormatException e) {
                printError("Invalid input. Please enter a valid number (e.g. 5.00).");
            }
        }
    }

    /**
     * Reads a yes/no confirmation. Accepts: y, yes, n, no (case-insensitive).
     * Returns true for yes, false for no.
     */
    public static boolean readConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) return true;
            if (input.equals("n") || input.equals("no"))  return false;
            printError("Please type 'y' for yes or 'n' for no.");
        }
    }

    // ─────────────────────────────────────────────
    //  VALIDATED FIELD READERS
    // ─────────────────────────────────────────────

    /**
     * Reads a person's full name (letters, spaces, hyphens, apostrophes only).
     */
    public static String readName(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (isValidName(input)) return formatName(input);
            printError("Name must be 2–100 characters and contain only letters, spaces, hyphens, or apostrophes.");
        }
    }

    /**
     * Reads and validates an ISBN-10 or ISBN-13 code.
     */
    public static String readISBN(String prompt) {
        while (true) {
            String input = readString(prompt).replaceAll("[\\s\\-]", ""); // strip spaces/dashes
            if (isValidISBN(input)) return input;
            printError("Invalid ISBN. Please enter a valid 10 or 13 digit ISBN (e.g. 9780134685991).");
        }
    }

    /**
     * Reads and validates an email address.
     */
    public static String readEmail(String prompt) {
        while (true) {
            String input = readString(prompt).toLowerCase();
            if (isValidEmail(input)) return input;
            printError("Invalid email address. Please enter a valid email (e.g. student@gmail.com).");
        }
    }

    /**
     * Reads a Philippine phone number (09XXXXXXXXX or +639XXXXXXXXX).
     */
    public static String readPhone(String prompt) {
        while (true) {
            String input = readString(prompt).replaceAll("\\s", "");
            if (isValidPhone(input)) return input;
            printError("Invalid phone number. Use format: 09XXXXXXXXX or +639XXXXXXXXX.");
        }
    }

    /**
     * Reads a date in yyyy-MM-dd format and validates it is a real calendar date.
     */
    public static String readDate(String prompt) {
        while (true) {
            String input = readString(prompt);
            if (isValidDate(input)) return input;
            printError("Invalid date. Please use format YYYY-MM-DD (e.g. 2025-06-15).");
        }
    }

    /**
     * Reads a future date (today or later) in yyyy-MM-dd format.
     */
    public static String readFutureDate(String prompt) {
        while (true) {
            String input = readDate(prompt);
            LocalDate entered = LocalDate.parse(input, DATE_FORMATTER);
            if (!entered.isBefore(LocalDate.now())) return input;
            printError("Date must be today or in the future.");
        }
    }

    /**
     * Reads an integer menu choice within a given range [min, max].
     */
    public static int readMenuChoice(String prompt, int min, int max) {
        while (true) {
            int choice = readInt(prompt);
            if (choice >= min && choice <= max) return choice;
            printError("Please enter a number between " + min + " and " + max + ".");
        }
    }

    /**
     * Reads a book quantity (1–9999 copies).
     */
    public static int readQuantity(String prompt) {
        while (true) {
            int qty = readPositiveInt(prompt);
            if (qty <= 9999) return qty;
            printError("Quantity must be between 1 and 9999.");
        }
    }

    /**
     * Reads a year (e.g. publication year). Must be between 1000 and current year.
     */
    public static int readYear(String prompt) {
        int currentYear = LocalDate.now().getYear();
        while (true) {
            int year = readPositiveInt(prompt);
            if (year >= 1000 && year <= currentYear) return year;
            printError("Please enter a valid year between 1000 and " + currentYear + ".");
        }
    }

    /**
     * Reads a Member/Book ID (positive integer).
     */
    public static int readID(String prompt) {
        return readPositiveInt(prompt);
    }

    // ─────────────────────────────────────────────
    //  VALIDATION HELPERS (reusable booleans)
    // ─────────────────────────────────────────────

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidISBN(String isbn) {
        if (isbn == null) return false;
        String clean = isbn.replaceAll("[\\s\\-]", "");
        return ISBN_PATTERN.matcher(clean).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        String clean = phone.replaceAll("\\s", "");
        return PHONE_PATTERN.matcher(clean).matches();
    }

    public static boolean isValidDate(String date) {
        if (date == null || !DATE_PATTERN.matcher(date).matches()) return false;
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isNonEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isPositive(int value) {
        return value > 0;
    }

    public static boolean isPositive(double value) {
        return value > 0;
    }

    // ─────────────────────────────────────────────
    //  FORMATTING UTILITIES
    // ─────────────────────────────────────────────

    /**
     * Capitalises the first letter of each word in a name.
     * e.g. "john dela cruz" → "John Dela Cruz"
     */
    public static String formatName(String name) {
        if (name == null || name.isEmpty()) return name;
        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }

    /**
     * Formats a double as Philippine Peso currency string.
     * e.g. 15.0 → "₱15.00"
     */
    public static String formatCurrency(double amount) {
        return String.format("₱%.2f", amount);
    }

    /**
     * Formats an ISBN with dashes for display (13-digit only).
     * e.g. 9780134685991 → 978-0-13-468599-1
     * Returns the raw value if not 13 digits.
     */
    public static String formatISBN(String isbn) {
        String clean = isbn.replaceAll("[\\s\\-]", "");
        if (clean.length() == 13) {
            return clean.substring(0, 3) + "-" +
                   clean.substring(3, 4) + "-" +
                   clean.substring(4, 6) + "-" +
                   clean.substring(6, 12) + "-" +
                   clean.substring(12);
        }
        return clean;
    }

    /**
     * Truncates a string to maxLength characters and adds "…" if it was cut.
     * Useful for table display.
     */
    public static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 1) + "…";
    }

    /**
     * Pads a string to a fixed width for aligned console table columns.
     */
    public static String padRight(String text, int width) {
        if (text == null) text = "";
        return String.format("%-" + width + "s", truncate(text, width));
    }

    public static String padLeft(String text, int width) {
        if (text == null) text = "";
        return String.format("%" + width + "s", truncate(text, width));
    }

    // ─────────────────────────────────────────────
    //  UI HELPERS
    // ─────────────────────────────────────────────

    /** Prints a red-style error message to the console. */
    public static void printError(String message) {
        System.out.println("  [!] " + message);
    }

    /** Prints a success message. */
    public static void printSuccess(String message) {
        System.out.println("  [✓] " + message);
    }

    /** Prints a divider line for menus. */
    public static void printDivider() {
        System.out.println("─".repeat(55));
    }

    /** Pauses and waits for the user to press Enter before continuing. */
    public static void pressEnterToContinue() {
        System.out.print("\n  Press ENTER to continue...");
        scanner.nextLine();
    }

    /** Clears the console (works on most terminals). */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
