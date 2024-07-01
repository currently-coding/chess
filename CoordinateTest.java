import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinateTest {

    @Test
    void testEquals() {
        var coord1 = new Coordinate(2, 4);
        var coord2 = new Coordinate(4, 2);
        var coord3 = new Coordinate(2, 2);
        var coord4 = new Coordinate(4, 4);
        var coord5 = new Coordinate(2, 4);
        var different_class = new int[]{2, 4};
        assertNotEquals(coord1, coord2);
        assertNotEquals(coord1, coord2);
        assertNotEquals(coord1, coord4);
        assertEquals(coord1, coord1);
        assertEquals(coord1, coord5);
        assertNotEquals(coord1, different_class);
    }
}