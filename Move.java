public class Move {
    Piece piece;
    Coordinate start;
    Coordinate end;

    public Move(Board board, Piece piece, Coordinate start, Coordinate end) {

        this.piece = piece;
        this.start = start;
        this.end = end;
    }

    public void print() {
        piece.print();

        start.print();

        end.print();

    }

}
