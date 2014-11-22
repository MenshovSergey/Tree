/**
 * Created by sergej on 14.11.14.
 */
public class Vector {
    double x, y, z;
    int number;
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double dist(Vector v) {
        return (x - v.x) * (x - v.x) + (y - v.y) * (y - v.y);
    }

    public Vector norm() {
        double len = Math.sqrt(x * x + y * y + z * z);
        return new Vector(x / len, y / len, z / len);
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }
    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }
    public Vector mul(double scale) {
        return new Vector(x * scale, y * scale, z * scale);
    }
    public double sqLength() {
        return x * x + y * y + z * z;
    }
}
