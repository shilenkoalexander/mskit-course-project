import org.donntu.knt.mskit.course.myfilters.Filter;
import org.donntu.knt.mskit.course.myfilters.kernel.GaussianKernelValue;
import org.junit.Test;

/**
 * @author Shilenko Alexander
 */
public class HomeTest {
    @Test
    public void tesdt() {
        int[][] pixels = new int[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10},
                {11, 12, 13, 14, 15},
                {16, 17, 18, 19, 20},
                {21, 22, 23, 24, 25},
        };

        Filter filter = new Filter();
        filter.process(pixels, 2, new GaussianKernelValue(1));
    }
}
