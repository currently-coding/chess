public class Piece {
    private final PieceType type;
    private final Side color;
    Board board;

    public Piece(Board board, PieceType type, Side color) {
        this.type = type;
        this.color = color;
        this.board = board;
    }

    public String get_string_type() {
        return type.toString();
    }

    @Override
    public String toString() {
        return ("Piece: " + type + "(" + color.toString() + ")" + type.toString());
    }

    public PieceType getType() {
        return this.type;
    }

    public Side getColor() {
        return this.color;
    }

    public boolean move(Coordinate start, Coordinate end) {
        // TODO: add checked parameter to avoid rapid checking when checkmate
        return this.type.possible_move(board, this, start, end);
    }

}
