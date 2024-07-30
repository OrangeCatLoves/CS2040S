import static org.junit.Assert.*;

import org.junit.Test;

public class OptimizationTest {

    /* Tests for Problem 1 */

    @Test
    public void testOptimization1() {
        int[] dataArray = {1, 3, 5, 7, 9, 11, 10, 8, 6, 4};
        assertEquals(11, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization10() {
        int[] dataArray = {11,0,10};
        assertEquals(11, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization11() {
        int[] dataArray = {};
        assertEquals(0, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization2() {
        int[] dataArray = {67, 65, 43, 42, 23, 17, 9, 100};
        assertEquals(100, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization3() {
        int[] dataArray = {4, -100, -80, 15, 20, 25, 30};
        assertEquals(30, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization111() {
        int[] dataArray = {1,2,3,4,5};
        assertEquals(5, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization12() {
        int[] dataArray = {5,4,3,2,1};
        assertEquals(5, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization13() {
        int[] dataArray = {5,4,3,2,1,6};
        assertEquals(6, Optimization.searchMax(dataArray));
    }

    @Test
    public void testOptimization4() {
        int[] dataArray = {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84,
                83};
        assertEquals(100, Optimization.searchMax(dataArray));
    }

}
