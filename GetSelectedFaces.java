package ai.certifai.solution;

import org.apache.commons.math3.ml.clustering.DoublePoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class GetSelectedFaces {
    public static List<double[]> getSelectedFaces(HashMap<String,double[]> detectedEmb, List<String>facelist){


        List<double[]> selectedFaceEmb = new ArrayList<>();
        for(String names: detectedEmb.keySet()){
            for(int i=0;i<facelist.size();i++){
                if(names.equals(facelist.get(i))){
                    selectedFaceEmb.add(detectedEmb.get(names));
                }
            }
        }
        return selectedFaceEmb;
    }
}
