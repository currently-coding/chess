import java.awt.Choice;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.util.Scanner;
// TODO: implement save option to restore game state

public class Game {
    public static void main(String[] args) {
        Game _ = new Game();
    }

    private final Board board;
    Scanner scanner;

    public Game() {
        board = new Board();
        scanner = new Scanner(System.in);
        String winner = play().toString();
        System.out.println("The Game has ended.");
        System.out.println("==> " + winner + " has won <==");
    }

    public final Side play() {
        /*
         * returns all winners of the full game
         */
        // introduction();
        Side active_player = Side.WHITE;
        boolean black_checkmate, white_checkmate;
        do {
            Coordinate[] move = get_input(active_player, board, scanner); // declare first to avoid scoping issues in
                                                                          // do-while
            active_player = (active_player == Side.WHITE) ? Side.BLACK : Side.WHITE;
            if (!active_player.is_checked(board).isEmpty()) {
                System.out.println(active_player.toString() + " is in a check.");
            }

            // check for checkmate after every move
            black_checkmate = Side.BLACK.checkmate(board);
            white_checkmate = Side.WHITE.checkmate(board);

        } while (!(black_checkmate || white_checkmate));
        // ---- GAME OVER ----
        board.display();
        if (white_checkmate) {
            return Side.BLACK;
        } else if (black_checkmate) {
            return Side.WHITE;
        } else {
            return Side.DRAW;
        }
    }

    private Coordinate parseCoordinate(String input) {
        int row = input.charAt(1) - 48; // idk why 48...
        int col = input.charAt(0) - 'a' + 1; // Convert 'a'-'h' to 1-8
        return new Coordinate(row, col);

    }

    private boolean isValidCoordinatePair(String input) {
        // regex fun
        return input.matches("[a-h][1-8][a-h][1-8]");
    }

    private Coordinate[] get_input(Side active, Board board, Scanner scanner) {
        Coordinate[] move = new Coordinate[2];

        while (true) { // Repeat until a valid move is entered
            board.display();
            System.out.print(active + ": ");
            String command = scanner.nextLine().trim(); // e.g., "show a4" or "move a4b5"

            // Validate input format
            if (!command.startsWith("show ") && !command.startsWith("move ")) {
                System.out.println("Invalid command. Use 'show <coordinate>' or 'move <coordinate-pair>'.");
                continue;
            }

            String[] parts = command.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid format. Use 'show <coordinate>' or 'move <coordinate-pair>'.");
                continue;
            }

            String mode = parts[0]; // "show" or "move"
            String input = parts[1];

            // Handle 'show' command
            if (mode.equals("show")) {
                if (isValidCoordinatePair(input)) {
                    Coordinate start = parseCoordinate(input.substring(0, 2));
                    Coordinate end = parseCoordinate(input.substring(2, 4));
                    System.out.println("Coordinates given: " + start + end);
                    Piece piece = board.pieces.get(start);
                    if (piece != null) {
                        System.out.println("Piece at " + input + ": " + piece);
                    } else {
                        System.out.println("No piece at " + input);
                    }
                } else {
                    System.out.println("Invalid coordinate format. Use a letter (a-h) followed by a number (1-8).");
                }
                continue; // Loop back for the next input
            }

            // Handle 'move' command
            if (mode.equals("move")) {
                while (true) {
                    try {
                        move = validate_move_input(input, move, active);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println("\tError: " + e.getMessage());
                        while (true) {
                            System.out.print("New Coordinates: ");
                            input = scanner.nextLine().trim();
                            if (!isValidCoordinatePair(input)) {
                                System.out.println("Try again: invalid format");
                            } else {
                                break;
                            }
                        }
                    }
                }
                if (move.length == 2) {
                    return move;
                } else {
                    continue;
                }
            }
        }

    }

    private Coordinate[] validate_move_input(String input, Coordinate[] move, Side active) {
        if (input.length() != 4) {
            throw new IllegalArgumentException("Invalid format");
        }

        String startStr = input.substring(0, 2);
        String endStr = input.substring(2, 4);

        if (isValidCoordinatePair(input)) {
            Coordinate start = parseCoordinate(startStr);
            Coordinate end = parseCoordinate(endStr);
            move = new Coordinate[] { start, end };

            // Basic validity checks
            if (board.pieces.get(start) == null) {
                System.out.println("Coordinates given: " + start + end);
                throw new IllegalArgumentException("No piece at starting position");
            }
            if (board.pieces.get(start).getColor() != active) {
                throw new IllegalArgumentException("Moving opponent's pieces");
            }
            if (board.move(start, end)) {
                return new Coordinate[] { start, end };

            } else {
                throw new IllegalArgumentException("Illegal Move");
            }
        } else {
            throw new IllegalArgumentException("Invalid Coordinates");
        }
    }

    public String save(String filename) {
        // TODO: implement
        return new String();
    }
}
