package DBScan;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBScan {

    public static void main(String[] args) {
        INDArray a = Nd4j.create(new double[] {1,1,1,1});
        INDArray b = Nd4j.create(new double[] {10,10,10,10});
        INDArray c = Nd4j.create(new double[] {20,20,20,20});
        List<INDArray> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++){
            list.add(a.add(Math.random()));
            list.add(b.add(Math.random()));
            list.add(c.add(Math.random()));
        }
//        System.out.println(list);
        DBScan scan = new DBScan();
        List<Cluster<DoublePoint>> clusters = scan.getClusters(list,1,3);
//        for(Cluster<DoublePoint> cluster: clusters){
//            System.out.println(Arrays.toString(cluster.getPoints().get(0).getPoint()));
//        }
        HashMap<DoublePoint,List<DoublePoint>> result = scan.getRepresentationMap(clusters);
        for(DoublePoint key: result.keySet()){
            System.out.println("key");
            System.out.println(key);
            System.out.println("value");
            System.out.println(result.get(key));
            System.out.println(result.get(key).size());
        }

//        DBScan scan = new DBScan();
//        scan.testCluster();
    }

    // Get perform clustering
    private List<Cluster<DoublePoint>> getClusters(List<INDArray> arrays, double eps, int minPts){
        final List<DoublePoint> points = arraysToPoints(arrays);
        final DBSCANClusterer<DoublePoint> transformer =
                new DBSCANClusterer<>(eps, minPts);
        return transformer.cluster(points);
    }

    //INDArray to Point
    public List<DoublePoint> arraysToPoints(List<INDArray> array){
        final List<DoublePoint> points = new ArrayList<>();
        for (INDArray arr : array){
            points.add(new DoublePoint(arr.toDoubleVector()));
        }
        return points;
    }

    //Point to INDArray
    public List<INDArray> pointsToArray(List<DoublePoint> points){
        final List<INDArray> arr = new ArrayList<>();
        for (DoublePoint point : points){
            double[] pnt = point.getPoint();
            arr.add(Nd4j.create(pnt));
        }
        return arr;
    }

    // return map (representation of face, list of faces)
    private HashMap<DoublePoint,List<DoublePoint>> getRepresentationMap(List<Cluster<DoublePoint>> clusters){
        HashMap<DoublePoint,List<DoublePoint>> map = new HashMap<>();
        for(Cluster<DoublePoint> cluster: clusters){
            map.put(cluster.getPoints().get(0),cluster.getPoints());
        }
        return map;
    }
}
