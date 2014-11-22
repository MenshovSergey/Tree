import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;

/**
 * Created by sergej on 18.11.14.
 */
public class Main {
    int n = 500;
    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(800,600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        Tree tree = new Tree();
        tree = tree.createTree(n);
        ArrayList<Double> pointsTree = tree.getPoints();
        while (!Display.isCloseRequested()) {

            // render OpenGL here

            Display.update();
        }
        Display.destroy();
    }

    public static void main(String[] args) {
        Main displayExample = new Main();
        displayExample.start();

    }
}
