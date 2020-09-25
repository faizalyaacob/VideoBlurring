
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.nd4j.linalg.io.ClassPathResource;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.bytedeco.opencv.global.opencv_highgui.waitKey;


public class Test {
    public static void main(String[] args) throws Exception {
        FileChooser fc = new FileChooser();
        String inputVideoPath = fc.getSourceFolder();
        final String saveloc = new ClassPathResource("tempImage").getFile().getAbsolutePath();
        System.out.println("------------------------------------");
        System.out.println("----------BlurMe Beta v1.0----------");
        System.out.println("\033[3m          We anonymize you          \033[0m");
        System.out.println("------------------------------------");
        //Deletes all previous images inside folder
        File folder = new File(saveloc);
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                f.delete();
            }
        }

        //Input: Vid directory Output: HashMap with key: Face Embeddings, Value: Hashmap of timestamp and bound box coordinates
        VideoReader vr = new VideoReader();
        DBScan dbScan = new DBScan();
        System.out.println("Start Encoding...");
        HashMap<double[], HashMap<Long,int[]>> out = vr.detectAndEncodeFace(inputVideoPath);
        List<double[]> embs = new ArrayList<>(out.keySet());
        List<DoublePoint> points = new ArrayList<>();
        for( double[] emb: embs){
            points.add( new DoublePoint(emb));
        }
        System.out.println("End Encoding...");
        System.out.println("\n\nStarted Clustering...");
        // pass video 2 short,video 1 short 2 scot, fail video3 0 output fast moving
        // fail on long vid 3 no faizal, pass long vid 1, video 2 no zufar
        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.1,1, new CosineSim());// pass all!! better
        // pass video 2 long 2 peh, pass video 1 long 2 scott,fail video 3 2 peh no faizal no zufar
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.06,10, new CosineSim());
        // pass video 3 long 3 peh, pass video 2 2 zufar 2 sum,pass video 1 2 shamala
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.06,6, new CosineSim());//pass all!!
        // pass video 1, pass video 2 2 zufar 2 sum, pass video 3 2 peh
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.065,6, new CosineSim());// pass all!! better
        // pass video 3 2 peh, , pass video 2 2 zufar 2 sum, pass video1
//        List<Cluster<DoublePoint>> clusters = dbScan.getClusters(points,0.07,6, new CosineSim());
        System.out.println("End Clustering\n");
        HashMap<DoublePoint,List<DoublePoint>> map = dbScan.getRepresentationMap(clusters);
//        for (DoublePoint pt : map.keySet()){
//            System.out.println(pt.getPoint().length);
//            System.out.println(pt);
//        }
//
//        System.out.println("No.of Faces Detected: "+map.keySet().size());
//        System.out.println("The system will show the faces detected in a separate window.");
//        System.out.println("[IMPORTANT] Notice the face number. Eg: face-0");

        HashMap<String, double[]> detectedEmb = vr.extractImageDetected(map,out, inputVideoPath,saveloc);

        GUI.askForFace(saveloc);
//        Object obj = new Object();
        while(GUI.getFrame().isVisible()){
            TimeUnit.SECONDS.sleep(1);
        }
//        System.out.println("ppppp");
//
        File imageFolder = new File(saveloc);
        String[] listofFiles = imageFolder.list();

        List<String> facelist =new ArrayList<>();
        for (String f : listofFiles){
            facelist.add(f.substring(0,f.length()-4));
        }

        //Extract
        List<double[]> getSelectedFaces = GetSelectedFaces.getSelectedFaces(detectedEmb,facelist);

        //Get embeddings, time stamp and bounding box coordinates to blur unselected faces
        List<double[]> embToBlur = compare.compareImage(map, getSelectedFaces);

        //Video blurring and video write (Output video: "/output.mp4")
        VideoBlurring vb = new VideoBlurring();
        HashMap<Long, List<int[]>> generateTimeLocs = vb.generateTimeLocs(embToBlur,out);
        vb.BlurringAndGenerateVideo(inputVideoPath,generateTimeLocs);

        System.out.println("\n\n Blurring completed! Programme exiting...");

        //Deletes all previous images inside folder
        folder = new File(saveloc);
        files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                f.delete();
            }
        }

    }
}
