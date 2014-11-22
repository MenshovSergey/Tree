import java.util.*;
import java.util.List;


public class Tree {
    public List<Tree> childs;
    private Vector currentVertex;
    private static final int RADIUS = 10;
    /* Радиус влияния (выбираем ближайшие) */
    private static final double D_I = 0.4;
    private static final double x = 5.9;
    /* Радиус узла */
    private static final double D = 0.01;
    /* Радиус уничтожения */
    private static final double D_K = 20* D;
    private static final int MAGIC_COUNT = 500;
    private static double[] circle;
    private static class TreeNear {
        Tree node;
        HashSet<Vector> near;
        Vector summary;

        public TreeNear(Tree node) {
            this.node = node;
            near = new HashSet<Vector>();
            summary = new Vector(0, 0, 0);
        }
        public void addPoint(Vector a) {
            near.add(a);
        }
        public void addVector(Vector a) {
            summary = summary.add(a.sub(node.currentVertex).norm());
        }
        public void subVector(Vector a) {
            summary = summary.sub(a.sub(node.currentVertex).norm());
        }
    }
    private static int subdivisionsCircle = 20;
    private ArrayList<Double> points;
    private ArrayList<Integer> indixes;
    private int iLeft = -1;
    private int iRight = -1;
    public Tree() {
        points = new ArrayList<>();
        indixes = new ArrayList<>();
    }
    public Tree createTree(int n) {
        Tree tree = new Tree(0, 0, 0);
        circle = getCircle(subdivisionsCircle);

        int lastDelete = 0;
        int currentDelete = 0;
        TreeNear tn = new TreeNear(tree);
        List<Vector> currentPoint = Generate.generatePoints(RADIUS, n);
        boolean flag = true;
        List<TreeNear> treeNode = new ArrayList<TreeNear>();
        treeNode.add(tn);
        HashMap<Vector, TreeNear> nearestNode = new HashMap<Vector, TreeNear>();
        TreeNear[] nearest = new TreeNear[currentPoint.size()];
        for (int i = 0; i < currentPoint.size(); i++) {
            Vector p = currentPoint.get(i);
            double curMin = p.dist(tn.node.currentVertex);
            if (curMin < D_I * D_I) {
                tn.addPoint(p);
                tn.addVector(p);
                nearest[i] = tn;
            }
        }
        while (flag && currentPoint.size() > 0) {
            System.out.println(currentPoint.size() +" " + treeNode.size());
            flag = false;
            /* Здесь находим для каждой точки ближайший Node и записываем в Node ближайшие к нему точки .... пункт b*/
            //это метод updateNear..
            /*c and d and e*/
            List<TreeNear> newTreeNode = new ArrayList<TreeNear>();
            for (TreeNear i : treeNode) {
                Vector curVector = new Vector(0, 0, 0);
                curVector = i.summary;
                /*
                for (Vector j : i.near) {
                    curVector = curVector.add(j.sub(i.node.currentVertex).norm());
                } */
                //i.near.clear();
                if (curVector.sqLength() == 0) {
                    continue;
                }
                flag = true;
                curVector = curVector.norm();
                Tree t = new Tree(curVector.mul(D).add(i.node.currentVertex));

                i.node.childs.add(t);
                TreeNear q = new TreeNear(t);
                newTreeNode.add(q);
                updateNearNode(nearest, q, i, currentPoint);
            }
            treeNode.addAll(newTreeNode);
            /* f and g */
            Iterator<Vector> it = currentPoint.iterator();
            currentDelete++;
            while (it.hasNext()) {
                Vector p = it.next();
                boolean delete = false;
                for (TreeNear i : newTreeNode/*treeNode*/) {
                    if (D_K * D_K >= p.dist(i.node.currentVertex)) {
                        delete = true;
                        break;
                    }
                }
                if (delete) {
                    it.remove();
                    lastDelete = currentDelete;
                }
            }
            if (currentDelete - lastDelete > MAGIC_COUNT) {
                break;
            }


        }
        return tree;
    }
    private Tree(int x, int y, int z) {
        currentVertex = new Vector(x, y, z);
        childs = new ArrayList<Tree>();
    }

    public Tree(Vector a) {
        currentVertex = a;
        childs = new ArrayList<Tree>();
    }
    public Vector getCurrentVertex() {
        return currentVertex;
    }

    private static void updateNearNode(TreeNear[] nearest, TreeNear t,TreeNear parent, List<Vector> currentPoint) {
        /*
        HashSet<Vector> e = new HashSet<Vector>(parent.near);
        for (Vector p : e) {            ;
            double curMin = p.dist(t.node.currentVertex);
            double hasMin = 0;
            if (nearest[p.number] != null) {
                hasMin = p.dist(nearest[p.number].node.currentVertex);
            }
            else {
                hasMin = D_K * 2;
            }
            if (curMin < hasMin && curMin < D_I * D_I) {
                t.addPoint(p);
                t.addVector(p);
                if (nearest[p.number] != null) {
                    nearest[p.number].near.remove(p);
                    nearest[p.number].subVector(p);
                }
                nearest[p.number] = t;
            }


        }*/
        for (int i = 0; i < currentPoint.size(); i++) {
            Vector p = currentPoint.get(i);
            double curMin = p.dist(t.node.currentVertex);
            double hasMin = 0;
            if (nearest[p.number] != null) {
                hasMin = p.dist(nearest[p.number].node.currentVertex);
            }
            else {
                hasMin = D_K * 2;
            }
            if (curMin < hasMin && curMin < D_I * D_I) {
                t.addPoint(p);
                t.addVector(p);
                if (nearest[p.number] != null) {
                    nearest[p.number].near.remove(p);
                    nearest[p.number].subVector(p);
                }
                nearest[p.number] = t;
            }


        }
    }

    public ArrayList<Double> getPoints() {
       ArrayList<Double> result = new ArrayList<>();

       return result;
    }
    private double getRadiusCylinder(Tree node, Tree parent) {
        double sum = 0.0f, self = 1.0f;
        for (Tree i : node.childs) {
            sum += Math.pow(getRadiusCylinder(i, node), x);
        }

        if (sum > 0.0f) {
            self =  Math.pow(sum, 1.0 / x);
        }
        if (node.iLeft == -1) {
            node.iLeft = points.size();
            node.iRight = points.size() + subdivisionsCircle - 1;
        }
        addPoints(node, self);
        if (parent.iLeft == -1) {
            parent.iLeft = points.size();
            parent.iRight = points.size() + subdivisionsCircle - 1;
        }
        addPoints(parent, self);
        connectNodes(node, parent);
        return self;


    }

    private void addPoints(Tree node, double r) {
        for (int i = 0; i < subdivisionsCircle; i++) {
            points.add(circle[3 * i] * r + node.currentVertex.x);
            points.add(circle[3 * i + 1] * r + node.currentVertex.y);
            points.add(circle[3 * i + 2] * r + node.currentVertex.z);
        }
    }

    private void connectNodes(Tree node, Tree parent) {
        int diff = parent.iLeft;
        for (int i = parent.iLeft; i < parent.iRight; i++) {
            indixes.add(i);
            indixes.add(i - diff + node.iLeft);
            indixes.add(i - diff + node.iLeft + 1);

            indixes.add(i);
            indixes.add(i - diff + node.iLeft + 1);
            indixes.add(i - diff + 1);

        }
    }

    private static double[] getCircle(int subdivisions) {
        double[] result = new double [3 * (subdivisions + 1)];
        double step = 2 * Math.PI / (subdivisions);
        for (int i = 0; i <= subdivisions; i++) {
            if (i == subdivisions) {
                result[i * 3] = 0;
                result[i * 3 + 1] = 0;
                result[i * 3 + 2] = 0;
            } else {
                result[i * 3] = Math.cos(step * i);
                result[i * 3 + 1] = Math.sin(step * i);
                result[i * 3 + 2] = 0;
            }
        }
        return result;
    }


}
