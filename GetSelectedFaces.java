package ai.certifai.solution.facial_recognition.identification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetSelectedFaces {
    public List<double[]> getSelectedFaces(HashMap<String,double[]> detectedEmb, List<String>facelist){
        List<double[]> selectedFaceEmb = new ArrayList<>();
        for(String names: detectedEmb.keySet()){
            for(int i=0;i<facelist.size();i++){
                if(names==facelist.get(i)){
                    selectedFaceEmb.add(detectedEmb.get(names));
                }
            }
        }
        return selectedFaceEmb;
    }
}
