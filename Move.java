public class Move {
    Piece piece;
    Coordinate start;
    Coordinate end;

    public Move(Board board, Piece piece, Coordinate start, Coordinate end) {
        System.err.println("Move: Creating new Move");
        this.piece = piece;
        this.start = start;
        this.end = end;
    }

    public void print() {
        piece.print();
        System.out.print(": ");
        start.print();
        System.out.print(" -> ");
        end.print();
        System.out.println();
    }

}
