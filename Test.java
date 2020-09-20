package ai.certifai.solution.facial_recognition.video_reading;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
        VideoReader vr = new VideoReader();
        DBScan dbScan = new DBScan();
        System.out.println("Start Encoding...");
        HashMap<double[], HashMap<Long,int[]>> out = vr.detectAndEncodeFace("C:\\Users\\Asus\\Desktop\\CDLE project data\\video2.mp4");
        List<double[]> embs = new ArrayList<>(out.keySet());
        List<DoublePoint> points = new ArrayList<>();
        for( double[] emb: embs){
            points.add( new DoublePoint(emb));
        }
        System.out.println("End Encoding...");
        System.out.println("Start Clustering...");
        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.3,5);
        System.out.println("End Clustering");
        HashMap<DoublePoint,List<DoublePoint>> map = dbScan.getRepresentationMap(clusters);
        for (DoublePoint pt : map.keySet()){
            System.out.println(pt.getPoint().length);
            System.out.println(pt);
        }
    }
}
