
import java.util.ArrayList;

public enum Side {
    BLACK,
    WHITE,
    UNDEFINED;

    public ArrayList<Coordinate> is_checked(Board board) {
        Coordinate king = board.king(this);
        for (Coordinate coordinate : board.pieces.keySet()) { // check for each piece
            Piece piece = board.pieces.get(coordinate);
            if (piece == null) {
                continue;
            }
            if (piece.getColor() != this) { // only opponent's pieces can check
                ArrayList<Coordinate> path = piece.getType().valid_move(board, piece, king, king);
                if (!path.isEmpty()) { // check if current piece could
                    // move to king's position
                    return path;
                }
            }
        }
        return new ArrayList<>();
    }

    public boolean checkmate(Board board) {
        System.err.println("Side: checkmate(): checking all possible moves");
        // Add performance later
        // Check every single move -> if not checked anymore => Not checkmate
        Coordinate king = board.king(/* Side */);
    }
}
