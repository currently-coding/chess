import java.util.Scanner;

public class Game {
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println(game.winner + "has won!");
    }

    private final Board board;
    private final Side winner;
    Scanner scanner;

    public Game() {
        board = new Board();
        scanner = new Scanner(System.in);
        winner = play();
    }

    private Coordinate[] get_move(Scanner scanner) {
        System.out.print("New Move:\n\tStart:\t");
        String source = scanner.next();
        System.out.print("\tEnd:\t");
        String destination = scanner.next();
        System.out.println("Move: " + source + " -> " + destination);

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

    public void introduction() {
        System.out.println("Welcome to Command Line Chess!");
        System.out.print("If you wish for an explanation type 1(and press enter), else just press enter: ");
        String input = scanner.next();
        if (input.isBlank()) {
            return;
        }
        System.out.println(
                "How to move:\n\t1. Type a letter specifying the row you want to start your move from.\n\t2.Type a number specifying the column you want your move to start from.\n\t3. Do the same to specify where you want to move to.\nYour moves will be checked for correctness.\nHave fun!");
    }

    public Side switch_sides(Side active_player) {
        return active_player == Side.BLACK ? Side.WHITE : Side.BLACK;
    }

    public final Side play() {
        introduction();
        Side active_player = Side.WHITE;
        do {
            System.err.println("Game: main game loop");
            Coordinate[] move; // declare first to avoid scoping issues in do-while
            do { // repeat until valid move
                System.err.println("Game: getting move");
                board.display();
                System.err.println("Game: Side: " + active_player);
                System.out.println(active_player + ":");
                move = get_move(scanner);
            } while (!board.move(move[0], move[1]));
            active_player = switch_sides(active_player);

        } while (!board.game_over());
        if (Side.BLACK.checkmate(board)) {
            return Side.WHITE;
        } else { // we know that it's checkmate so lazy comparison
            return Side.BLACK;
        }
    }
}