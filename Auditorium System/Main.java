import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter the filename for the auditorium configuration:");
        String filename = scanner.nextLine();
        // Assuming Auditorium constructor takes a filename to set up the auditorium.
        Auditorium aud_obj = new Auditorium(filename);

        boolean exit = false;
        do {
            printMainMenu();
            int choice = getUserChoice();
            
            if (choice == 1) { 
                aud_obj.print_Auditorium();

                int row = getValidRowNumber(aud_obj);
                
                System.out.println("Enter the row number:");

                System.out.println("Enter the starting seat letter:");
                char seatChar = scanner.next().charAt(0);
                int seat = seatChar - 'A'; // Convert to integer index

                System.out.println("Enter the number of adult tickets:");
                int adultQty = getUserInput();

                System.out.println("Enter the number of child tickets:");
                int childQty = getUserInput();

                System.out.println("Enter the number of senior tickets:");
                int seniorQty = getUserInput();

                int totalSeats = adultQty + childQty + seniorQty;
                
                if (aud_obj.check_Auditorium(totalSeats, row, seat)) {
                    aud_obj.book_Auditorium(row, seat, adultQty, childQty, seniorQty);
                } else {
                    System.out.println("Not enough seats available.");
                    // Call the modified best_Available method
                    if (!aud_obj.best_Available(adultQty, childQty, seniorQty, scanner)) {
                        System.out.println("No suitable seats available or reservation cancelled.");
                    }
                }

            } else if(choice == 2) {
                exit = true;
                aud_obj.calculate_Output();

            }

        } while (!exit);

        aud_obj.write_File();
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Reserve Seats");
        System.out.println("2. Exit");
    }

    private static int getUserInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid number:");
            scanner.next(); // Consume the invalid input
        }
        return scanner.nextInt();
    }

    private static int getUserChoice() {
        int input;
        do {
            System.out.println("Enter your choice:");
            while (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                scanner.next(); // Consume the invalid input
            }
            input = scanner.nextInt();
        } while (input < 1 || input > 2);
        return input;
    }  

    private static int getValidRowNumber(Auditorium auditorium) {
        int rowNumber;
        do {
            System.out.println("Enter the row number:");
            rowNumber = getUserInput();
            if (rowNumber < 1 || rowNumber > auditorium.getAuditoriumHeight()) {
                System.out.println("Invalid row number. Please enter a number between 1 and " + auditorium.getAuditoriumHeight() + ".");
            }
        } while (rowNumber < 1 || rowNumber > auditorium.getAuditoriumHeight());
        return rowNumber;
    }
}
