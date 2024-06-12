public class Piece {
    private final PieceType type;
    private final Side color;
    Board board;

    public Piece(Board board, PieceType type, Side color) {
        System.err.println("Piece: Creating new Piece");
        this.type = type;
        this.color = color;
        this.board = board;
    }

    public PieceType getSymbol() {
        return type;
    }

    public void print_type() {
        type.print_short();
    }

    public void print() {
        System.out.print(type + " - " + color);
    }

    public PieceType getType() {
        return this.type;
    }

    public Side getColor() {
        return this.color;
    }

    public boolean move(Coordinate start, Coordinate end) {
        System.err.println("Piece: moving");
        return this.type.possible_move(board, this, start, end);
    }

}
