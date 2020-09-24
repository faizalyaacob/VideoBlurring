package ai.certifai.solution;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.opencv_core.Mat;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.bytedeco.opencv.global.opencv_highgui.destroyAllWindows;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.opencv.highgui.HighGui.imshow;


public class Test {
    public static void main(String[] args) throws Exception {
        String imagePath = "E:\\Java Projects\\output";
        File folder = new File(imagePath);
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                f.delete();
            }
        }
        VideoReader vr = new VideoReader();
        DBScan dbScan = new DBScan();
        System.out.println("Start Encoding...");
        String path = "C:\\Users\\scotg\\Downloads\\Video\\WhatsApp4.mp4";
        HashMap<double[], HashMap<Long,int[]>> out = vr.detectAndEncodeFace(path);
        List<double[]> embs = new ArrayList<>(out.keySet());
        List<DoublePoint> points = new ArrayList<>();
        for( double[] emb: embs){
            points.add( new DoublePoint(emb));
        }
        System.out.println("End Encoding...");
        System.out.println("Start Clustering...");
        // pass video 2 short,video 1 short 2 scot, fail video3 0 output fast moving
        // fail on long vid 3 no faizal, pass long vid 1, video 2 no zufar
        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.08,10, new EuclideanDistance());
        // pass video 2 long 2 peh, pass video 1 long 2 scott,fail video 3 2 peh no faizal no zufar
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.06,10, new CosineSim());
        // pass video 3 long 3 peh, pass video 2 2 zufar 2 sum,pass video 1 2 shamala
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.06,6, new CosineSim());//pass all!!
        // pass video 1, pass video 2 2 zufar 2 sum, pass video 3 2 peh
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.065,6, new CosineSim());// pass all!! better
        // pass video 3 2 peh, , pass video 2 2 zufar 2 sum, pass video1
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.07,6, new CosineSim());
        System.out.println("End Clustering");
        HashMap<DoublePoint,List<DoublePoint>> map = dbScan.getRepresentationMap(clusters);
//        for (DoublePoint pt : map.keySet()){
//            System.out.println(pt.getPoint().length);
//            System.out.println(pt);
//        }
        System.out.println("No.of Face Detected: "+map.keySet().size());
        String saveloc = "E:\\Java Projects\\output\\";

        HashMap<String, double[]> detectedEmb = vr.extractImageDetected(map,out,path,saveloc);

        File imageFolder = new File(saveloc);
        String[] listofFiles = imageFolder.list();

        for(String image: listofFiles){
            String imageName = saveloc + "\\" + image;
            Mat imageMat = imread(imageName);
            opencv_highgui.imshow(imageName,imageMat);
            if (waitKey(0) == 27) {
                destroyAllWindows();
            }
        }





        List<String> facelist = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.println("Please select the face that you need...");
        System.out.println(detectedEmb.keySet());
        String selection = sc.nextLine(); // User inputs either 1/2/3
        facelist.add("face-"+selection+".jpg");
        List<double[]> getSelectedFaces = GetSelectedFaces.getSelectedFaces(detectedEmb,facelist);
        List<double[]> embToBlur = compare.compareImage(map, getSelectedFaces);
        VideoBlurring vb = new VideoBlurring();
        HashMap<Long, List<int[]>> generateTimeLocs = vb.generateTimeLocs(embToBlur,out);
        vb.BlurringAndGenerateVideo(path,generateTimeLocs);

    }
}
