import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Scientific Calculator ===");
            System.out.println("1. Basic Arithmetic");
            System.out.println("2. Unit Conversion");
            System.out.println("3. Scientific Calculations");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    basicArithmetic(scanner);
                    break;
                case 2:
                    unitConversion(scanner);
                    break;
                case 3:
                    scientificCalculations(scanner);
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private static void basicArithmetic(Scanner scanner) {
        System.out.print("Enter first number: ");
        double num1 = scanner.nextDouble();
        System.out.print("Enter second number: ");
        double num2 = scanner.nextDouble();

        System.out.println("Select operation: ");
        System.out.println("1. Addition (+)");
        System.out.println("2. Subtraction (-)");
        System.out.println("3. Multiplication (*)");
        System.out.println("4. Division (/)");

        int operation = scanner.nextInt();
        double result;

        switch (operation) {
            case 1:
                result = num1 + num2;
                break;
            case 2:
                result = num1 - num2;
                break;
            case 3:
                result = num1 * num2;
                break;
            case 4:
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    System.out.println("Error: Division by zero!");
                    return;
                }
                break;
            default:
                System.out.println("Invalid operation.");
                return;
        }
        System.out.println("Result: " + result);
    }

    private static void unitConversion(Scanner scanner) {
        System.out.println("Unit Conversion");
        System.out.println("1. Celsius to Fahrenheit");
        System.out.println("2. Fahrenheit to Celsius");
        System.out.println("3. Meters to Kilometers");
        System.out.println("4. Kilometers to Meters");

        int choice = scanner.nextInt();
        double value;

        switch (choice) {
            case 1:
                System.out.print("Enter temperature in Celsius: ");
                value = scanner.nextDouble();
                System.out.println("Fahrenheit: " + (value * 9/5 + 32));
                break;
            case 2:
                System.out.print("Enter temperature in Fahrenheit: ");
                value = scanner.nextDouble();
                System.out.println("Celsius: " + ((value - 32) * 5/9));
                break;
            case 3:
                System.out.print("Enter distance in Meters: ");
                value = scanner.nextDouble();
                System.out.println("Kilometers: " + (value / 1000));
                break;
            case 4:
                System.out.print("Enter distance in Kilometers: ");
                value = scanner.nextDouble();
                System.out.println("Meters: " + (value * 1000));
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private static void scientificCalculations(Scanner scanner) {
        System.out.println("Scientific Calculations");
        System.out.println("1. Sine");
        System.out.println("2. Cosine");
        System.out.println("3. Tangent");
        System.out.println("4. Exponentiation (x^y)");

        int choice = scanner.nextInt();
        double result;

        switch (choice) {
            case 1:
                System.out.print("Enter angle in degrees: ");
                double angle = scanner.nextDouble();
                result = Math.sin(Math.toRadians(angle));
                System.out.println("Sine: " + result);
                break;
            case 2:
                System.out.print("Enter angle in degrees: ");
                angle = scanner.nextDouble();
                result = Math.cos(Math.toRadians(angle));
                System.out.println("Cosine: " + result);
                break;
            case 3:
                System.out.print("Enter angle in degrees: ");
                angle = scanner.nextDouble();
                result = Math.tan(Math.toRadians(angle));
                System.out.println("Tangent: " + result);
                break;
            case 4:
                System.out.print("Enter base (x): ");
                double base = scanner.nextDouble();
                System.out.print("Enter exponent (y): ");
                double exponent = scanner.nextDouble();
                result = Math.pow(base, exponent);
                System.out.println("Result: " + result);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
}
