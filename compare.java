package ai.certifai.solution.facial_recognition.identification;

import org.apache.commons.math3.ml.clustering.DoublePoint;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class compare {

    public List<double []> compareImage(HashMap<DoublePoint,List<DoublePoint>> embCluster, List<DoublePoint> notToBlur){
        List<double []> embToBlur = new ArrayList<>();

        for (DoublePoint emb : embCluster.keySet()){
            if(!notToBlur.contains(emb)){
                for(DoublePoint face: embCluster.get(emb)){
                    double[] faceDouble = face.getPoint();
                    embToBlur.add(faceDouble);
                }
            }
        }

        return embToBlur;
    }

    public static void main(String[] args){

        HashMap<DoublePoint,List<DoublePoint>> embCluster = new HashMap<>();
        List<DoublePoint> notToBlur = new ArrayList<>();

        double[] pnt1 = new double[]{1.0,1.0,1.0};
        double[] pnt2 = new double[]{2.0,2.0,2.0};
        DoublePoint d1 = new DoublePoint(pnt1);
        DoublePoint d2 = new DoublePoint(pnt2);

        List<DoublePoint> l1 = new ArrayList<>();
        l1.add(d1);

        List<DoublePoint> l2 = new ArrayList<>();
        l2.add(d2);

        embCluster.put(d1,l1);
        embCluster.put(d2,l2);

//        notToBlur.add(d1);
//        notToBlur.add(d2);

        compare cp = new compare();
        List<double[]> faceToBlur = cp.compareImage(embCluster,notToBlur);
        for(double[] face: faceToBlur){
            System.out.println(Arrays.toString(face));
        }
    }
}
