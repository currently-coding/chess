import java.util.Objects;

public class Coordinate {
    protected int col;
    protected int row;

    public Coordinate() {
        System.err.println("Coordinate: Creating new Coordinate");
        col = 0;
        row = 0;
    }

    public Coordinate(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public void print() {
        System.out.println("(" + col + "|" + row + ")");
    }

    @Override
    public boolean equals(Object o) {
        // System.err.println("Coordinate: .equals()");
        if (this.getClass() != o.getClass())
            return false;
        if (this == o)
            return true;
        Coordinate that = (Coordinate) o;
        return (this.row == that.row && this.col == that.col);

    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
