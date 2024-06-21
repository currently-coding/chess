
import java.net.CookieHandler;
import java.util.ArrayList;

public enum Side {
    BLACK,
    WHITE,
    UNDEFINED;

    public ArrayList<ArrayList<Coordinate>> is_checked(Board board) {
        Coordinate king = board.king(this);
        ArrayList<ArrayList<Coordinate>> allPaths = new ArrayList<>();

        for (Coordinate coordinate : board.pieces.keySet()) { // check for each piece
            Piece piece = board.pieces.get(coordinate);
            if (piece == null) {
                continue;
            }
            if (piece.getColor() != this) { // only opponent's pieces can check
                ArrayList<Coordinate> path = piece.getType().valid_move(board, piece, king, king);
                if (!path.isEmpty()) { // check if current piece could move to king's position
                    allPaths.add(path); // add path to list of all paths
                }
            }
        }

        return allPaths;
    }

    public boolean checkmate(Board board) {
        System.err.println("Side: checkmate(): checking all possible moves");
        // Add performance later
        // Check every single move -> if not checked anymore => Not checkmate
        Coordinate king = board.king(this);
        ArrayList<ArrayList<Coordinate>> attackPath = is_checked(board);
        if (attackPath.isEmpty())
            return false;
        for (Coordinate key : board.pieces.keySet()) {
            Piece piece = board.pieces.get(key);
            if (piece == null)
                continue;
            for (ArrayList<Coordinate> path : attackPath) {
                for (Coordinate square : path) {
                    if (piece.move(key, square)) {
                        
                    }
                }
            }
        }
    }
}
