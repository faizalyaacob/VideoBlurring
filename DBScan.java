package ai.certifai.solution.facial_recognition.video_reading;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBScan {

    public static void main(String[] args) {
    }

    // Get perform clustering
    public List<Cluster<DoublePoint>> getClusters(List<DoublePoint> points, double eps, int minPts, DistanceMeasure method){
        final DBSCANClusterer<DoublePoint> transformer =
                new DBSCANClusterer<>(eps, minPts, method);
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
    public HashMap<DoublePoint,List<DoublePoint>> getRepresentationMap(List<Cluster<DoublePoint>> clusters){
        HashMap<DoublePoint,List<DoublePoint>> map = new HashMap<>();
        for(Cluster<DoublePoint> cluster: clusters){
            map.put(cluster.getPoints().get(0),cluster.getPoints());
        }
        return map;
    }
}
