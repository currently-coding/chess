public class Move {
    private Piece piece;
    private Coordinate start;
    private Coordinate end;

    public Move(Board board, Piece piece, Coordinate start, Coordinate end) {

        this.piece = piece;
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return piece.toString() + start.toString() + end.toString();
    }

    public Piece getPiece() {
        return this.piece;
    }

    public Coordinate getEnd() {
        return this.end;
    }

    public Coordinate getStart() {
        return this.start;
    }

}
