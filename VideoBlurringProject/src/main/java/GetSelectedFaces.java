import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetSelectedFaces {
    public static List<double[]> getSelectedFaces(HashMap<String,double[]> detectedEmb, List<String>facelist){


        List<double[]> selectedFaceEmb = new ArrayList<>();
        for(String names: facelist){
            System.out.println(names);
            for(String nameMap: detectedEmb.keySet()){
                System.out.println(nameMap);
                System.out.println(nameMap.equals(names));
                if (nameMap.equals(names)){
                    selectedFaceEmb.add(detectedEmb.get(nameMap));
                }
            }
        }
        System.out.println(selectedFaceEmb.size());
        return selectedFaceEmb;
    }
}
