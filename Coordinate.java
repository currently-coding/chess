import java.util.Objects;

public class Coordinate {
    protected int col;
    protected int row;

    public Coordinate() {
        col = 0;
        row = 0;
    }

    public Coordinate(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public void print() {
        System.out.print("(" + col + "|" + row + ")");
    }

    @Override
    public boolean equals(Object o) {
        // check if from the same class
        if (this.getClass() != o.getClass())
            return false;
        // check if the same in memory
        if (this == o)
            return true;
        // convert to Coordinate since it has to be of that type
        Coordinate that = (Coordinate) o;
        return (this.row == that.row && this.col == that.col);

    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + this.col + "|" + this.row + ")";
    }
}
