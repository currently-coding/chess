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

    private Coordinate[] get_move(Scanner scanner, Side active) {
        String input;
        do {
            System.out.print(active.toString() + ": ");
            input = scanner.next();
            if (input.length() == 4) {
                System.out.println();
                break;
            }
            System.out.println();
            System.out.println("Error: Incorrect format. Try again.\n");
        } while (true);

        String source, destination;
        source = input.substring(0, 2);
        destination = input.substring(2, 4);

        // Convert the source coordinate
        int sourceCol = source.charAt(0) - 'a' + 1; // Convert letter to number (a -> 1, b -> 2, etc.)
        int sourceRow = Character.getNumericValue(source.charAt(1)); // Convert character to integer

        // Convert the destination coordinate
        int destinationCol = destination.charAt(0) - 'a' + 1; // Convert letter to number (a -> 1, b -> 2, etc.)
        int destinationRow = Character.getNumericValue(destination.charAt(1)); // Convert character to integer

        // Create Coordinate objects
        Coordinate sourceCoordinate = new Coordinate(sourceRow, sourceCol);
        Coordinate destinationCoordinate = new Coordinate(destinationRow, destinationCol);
        return new Coordinate[] { sourceCoordinate, destinationCoordinate };
    }

    public final Side play() {
        /*
         * returns all winners of the full game
         */
        // introduction();
        Side active_player = Side.WHITE;
        boolean black_checkmate, white_checkmate;
        do {
            Coordinate[] move; // declare first to avoid scoping issues in do-while
            boolean wrong_side = false;
            do { // repeat until valid move
                wrong_side = false;
                board.display();
                move = get_move(scanner, active_player);
                // Validity checks
                // check there is a piece
                if (this.board.pieces.get(move[0]) == null) {
                    System.out.println("Error: There is no piece to move at the start position given.");
                    continue;
                }
                // check Side
                if (this.board.pieces.get(move[0]).getColor() != active_player) {
                    System.out.println("Error: Cannot move the opponents pieces.");
                    continue;
                }
                if (board.move(move[0], move[1])) {
                    break;
                } else {
                    System.out.println("Illegal Move. Try again");
                }

            } while (true);
            active_player = (active_player == Side.WHITE) ? Side.BLACK : Side.WHITE;
            if (!active_player.is_checked(board).isEmpty()) {
                System.out.println(active_player.toString() + " is in a check.");
            }

            // check for checkmate after every move
            black_checkmate = Side.BLACK.checkmate(board);
            white_checkmate = Side.WHITE.checkmate(board);

        } while (!(black_checkmate || white_checkmate));
        board.display();
        if (white_checkmate) {
            return Side.BLACK;
        } else if (black_checkmate) {
            return Side.WHITE;
        } else {
            return Side.DRAW;
        }
    }

    public String save(String filename) {
        // TODO: implement
        return new String();
    }
}
