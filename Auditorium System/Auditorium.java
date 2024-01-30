import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

class Auditorium {
    private Node first;

    public Auditorium(String filename) {
        set_Auditorium(filename);
    }

    private void set_Auditorium(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));

            Node lastRowFirstNode = null; // Keeps track of the first Node of the last row processed
            Node previousRowNode = null; // Keeps track of the nodes in the previous row as we link down

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Node currentRowFirstNode = null;
                Node currentNode = null;

                for (int i = 0; i < line.length(); i++) {
                    if (currentNode == null) { // This means we are at the beginning of a new row
                        currentNode = new Node(new Seat(line.charAt(i)));
                        if (first == null) {
                            first = currentNode; // Set the first node of the auditorium if it's not set
                        }
                        currentRowFirstNode = currentNode;// the start of this row

                        if (previousRowNode != null) {
                            previousRowNode.setDown(currentNode); // Set down link for the first node
                        }
                    } else { // Not the first seat in the row
                        currentNode.setNext(new Node(new Seat(line.charAt(i))));
                        currentNode = currentNode.getNext();

                        if (previousRowNode != null) {
                            previousRowNode = previousRowNode.getNext(); // Move to the next node in the previous row
                            if (previousRowNode != null) {
                                previousRowNode.setDown(currentNode); // Set down link for current node
                            }
                        }
                    }
                }

                // Move the trackers to the next row
                if (lastRowFirstNode == null) {
                    lastRowFirstNode = currentRowFirstNode;
                } else {
                    lastRowFirstNode = lastRowFirstNode.getDown();
                }
                previousRowNode = currentRowFirstNode;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            e.printStackTrace();
        }
    }

    public void print_Auditorium() {
        Node current_row = first;
        int row_number = 0;
        System.out.println("\n\nAuditorium Seating");
        // Assuming no_of_seats is the width of the auditorium
        int no_of_seats = getAuditoriumWidth();
        System.out.print(" ");
        for (int i = 65; i < 65 + no_of_seats; i++) {
            System.out.print((char)i);
        }
        System.out.println();
        while (current_row != null) {
            row_number++;
            System.out.print(row_number);
            Node current = current_row;
            while (current != null) {
                Seat s = current.getPayload();
                if (s.getTicketType() != '.') {
                    System.out.print('#');
                } else {
                    System.out.print(s.getTicketType());
                }
                current = current.getNext();
            }
            System.out.println();
            current_row = current_row.getDown();
        }
    }

    private int getAuditoriumWidth() {
        Node temp = first;
        int width = 0;
        while (temp != null) {
            width++;
            temp = temp.getNext();
        }
        return width;
    }

    private double calculateScore(int currentRow, int currentSeatIndex, int total_seats_required, int width) {
        int height = getAuditoriumHeight();
        double middleRowVal = (height - 1) / 2.0 + 1;
        double middleSeatVal = (width - 1) / 2.0;
        double midBlockSeat = currentSeatIndex + (total_seats_required - 1) / 2.0;
    
        double rowDistance = Math.abs(middleRowVal - currentRow);
        double seatDistance = Math.abs(middleSeatVal - midBlockSeat);
    
        return Math.sqrt(rowDistance * rowDistance + seatDistance * seatDistance);
    }

    public int getAuditoriumHeight() {
        Node tempRow = first;
        int height = 0;
        while (tempRow != null) {
            height++;
            tempRow = tempRow.getDown();
        }
        return height;
    }    

    public boolean check_Auditorium(int tt, int row, int seat) {
        Node current = first;
        for (int i = 1; i < row; i++) {
            current = current.getDown();
        }
        for (int i = 0; i < seat; i++) {
            current = current.getNext();
        }
        for (int i = 0; i < tt; i++) {
            if (current.getPayload().getTicketType() != '.') {
                return false;
            }
            current = current.getNext();
        }
        return true;
    }

    public void book_Auditorium(int row, int seat, int a, int c, int s) {
        Node current = first;
        for (int i = 1; i < row; i++) {
            current = current.getDown();
        }
        for (int i = 0; i < seat; i++) {
            current = current.getNext();
        }
        for (int i = 0; i < a; i++) {
            current.getPayload().setTicketType('A');
            current = current.getNext();
        }
        for (int i = 0; i < c; i++) {
            current.getPayload().setTicketType('C');
            current = current.getNext();
        }
        for (int i = 0; i < s; i++) {
            current.getPayload().setTicketType('S');
            current = current.getNext();
        }
    }

    public boolean best_Available(int adultQty, int childQty, int seniorQty, Scanner scanner) {
        int total_seats_required = adultQty + childQty + seniorQty;
        int width = getAuditoriumWidth();
        int height = getAuditoriumHeight();
    
        int bestRow = -1;
        int bestSeat = -1;
        double bestScore = Double.MAX_VALUE;
        int middleRowVal = (height - 1) / 2 + 1;
    
        for (int row = 1; row <= height; row++) {
            for (int seatChar = 0; seatChar <= width - total_seats_required; seatChar++) {
                if (check_Auditorium(total_seats_required, row, seatChar)) {
                    double score = calculateScore(row, seatChar, total_seats_required, width);
                    if (score < bestScore || (score == bestScore && Math.abs(row - middleRowVal) < Math.abs(bestRow - middleRowVal))
                        || (score == bestScore && Math.abs(row - middleRowVal) == Math.abs(bestRow - middleRowVal) && row < bestRow)) {
                        bestScore = score;
                        bestRow = row;
                        bestSeat = seatChar;
                    }
                }
            }
        }
    
        if (bestSeat >= 0 && bestRow > 0) {
            char startSeatChar = (char) (bestSeat + 'A');
            char endSeatChar = (char) (bestSeat + 'A' + total_seats_required - 1);
            System.out.println("Best available seats: " + bestRow + startSeatChar + " - " + bestRow + endSeatChar);
            System.out.print("Reserve? (Y/N) ");
            scanner.nextLine();
            String userInput = scanner.nextLine();
    
            if (userInput.equalsIgnoreCase("Y")) {
                book_Auditorium(bestRow, bestSeat, adultQty, childQty, seniorQty);
                return true;
            } else {
                System.out.println("Reservation cancelled.");
                return false;
            }
        } else {
            System.out.println("No sufficient consecutive seats available.");
            return false;
        }
    }

    public void write_File() {
        try (PrintWriter pw = new PrintWriter(new File("A1.txt"))) {
            Node current_row = first;
            while (current_row != null) {
                Node current = current_row;
                while (current != null) {
                    pw.print(current.getPayload().getTicketType());
                    current = current.getNext();
                }
                pw.println();
                current_row = current_row.getDown();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void calculate_Output() {
        Node current_row = first;
        int sold_adult = 0, sold_child = 0, sold_senior = 0;
        int totalSeats = 0; // To count the total number of seats
    
        while (current_row != null) {
            Node current = current_row;
            while (current != null) {
                totalSeats++; // Increment for each seat
                char seatType = current.getPayload().getTicketType();
                if (seatType == 'A') {
                    sold_adult++;
                } else if (seatType == 'C') {
                    sold_child++;
                } else if (seatType == 'S') {
                    sold_senior++;
                }
                current = current.getNext();
            }
            current_row = current_row.getDown();
        }
        
        int totalTickets = sold_adult + sold_child + sold_senior; // Total tickets sold
    
        System.out.println("Total Seats: " + totalSeats);
        System.out.println("Total Tickets: " + totalTickets);
        System.out.println("Adult Tickets: " + sold_adult);
        System.out.println("Child Tickets: " + sold_child);
        System.out.println("Senior Tickets: " + sold_senior);
        System.out.println("Total Sales: $" + String.format("%.2f", (sold_adult * 10 + sold_child * 5 + sold_senior * 7.5)));
    }
    
}