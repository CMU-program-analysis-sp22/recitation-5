package inputs;

public class DefsTest {
    public static int test() {
        int x = 3;
        int y = x + 4;
        int z = (y - x) * 2;
        int w = 0;

        for (int i = 0; i < 10; i++) {
            w = w + 1;
            x = x - 1;
        }

        if (w > 4) {
            y = 4;
            x = x + 3;
        } else {
            x = x + 4;
        }

        return x + y + w + z;
    }
}
