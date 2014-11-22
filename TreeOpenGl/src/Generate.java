import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Generate {
    public static List<Vector> generatePoints(int radius, int n) {
        List<Vector> result = new LinkedList<Vector>();
        double curX, curY, curZ;
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            //curY = random.nextDouble();
            //curX = (random.nextDouble() - 0.5) * Math.sin(curY * curY * Math.PI);
            //curX = (random.nextDouble() - 0.5) * (Math.pow(2 +curY, curY)) * Math.sin(curY);
            //curX = Math.sqrt(1 - curY) *(random.nextDouble() *2 - 1);
            curY = random.nextDouble();
            curX = Math.sqrt(1 - curY) *(random.nextDouble() *2 - 1);
            curZ = 0;
            Vector z = new Vector(curX, curY, curZ);
            z.number = i;
            result.add(z);

        }
        return result;
    }
}
