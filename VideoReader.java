package ai.certifai.solution.facial_recognition.video_reading;

import ai.certifai.solution.facial_recognition.detection.FaceLocalization;
import ai.certifai.solution.facial_recognition.detection.OpenCV_DeepLearningFaceDetector;
import ai.certifai.solution.facial_recognition.identification.feature.VGG16FeatureProvider;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.*;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;
import static org.bytedeco.opencv.global.opencv_imgproc.resize;

public class VideoReader {


    public VideoReader() throws IOException, ClassNotFoundException {
    }

//    public static void main(String[] args) throws Exception {
//        VideoReader vr = new VideoReader();
////        vr.detect("C:\\Users\\Asus\\Desktop\\CDLE project data\\video1.mp4");
//        HashMap<double[], HashMap<Long,int[]>> out = vr.detectAndEncodeFace("C:\\Users\\Asus\\Desktop\\CDLE project data\\video1.mp4");
//        for(double[] emb : out.keySet()){
//            System.out.println(Arrays.toString(emb));
//        }
//
//    }

    // for video testing
    public void detect(String videoPath) throws Exception{
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.setFormat("mp4");
        grabber.start();
        int width = grabber.getImageWidth();
        int height = grabber.getImageHeight();

        OpenCV_DeepLearningFaceDetector faceDetector = new OpenCV_DeepLearningFaceDetector(width,height,0.8);
        VGG16FeatureProvider faceIdentifier = new VGG16FeatureProvider();

        OpenCVFrameConverter.ToMat frame2Mat = new OpenCVFrameConverter.ToMat();

        while (grabber.grab() != null) {
            Frame frame = grabber.grabImage();
            if (frame != null) {
                Mat opencvMat = frame2Mat.convert(frame);

                long time = grabber.getTimestamp();
                System.out.println(time);
                System.out.println(grabber.getLengthInTime());

                faceDetector.detectFaces(opencvMat);
                List<FaceLocalization> faceLocalizations = faceDetector.getFaceLocalization();
                for (FaceLocalization loc: faceLocalizations){
                    INDArray emb = faceIdentifier.getEmbeddingFromPic(opencvMat,loc);
                }
            }
        }

    }

    // functional part
    public HashMap<double[], HashMap<Long,int[]>> detectAndEncodeFace(String videoPath) throws Exception {
        HashMap<double[], HashMap<Long,int[]>> out = new HashMap<>();
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.setFormat("mp4");
        grabber.start();
        int width = grabber.getImageWidth();
        int height = grabber.getImageHeight();

        OpenCV_DeepLearningFaceDetector faceDetector = new OpenCV_DeepLearningFaceDetector(width,height,0.8);
        VGG16FeatureProvider faceIdentifier = new VGG16FeatureProvider();

        OpenCVFrameConverter.ToMat frame2Mat = new OpenCVFrameConverter.ToMat();

        while (grabber.grab() != null) {
            Frame frame = grabber.grabImage();
            if (frame != null) {
                Mat opencvMat = frame2Mat.convert(frame);

                long time = grabber.getTimestamp();

                faceDetector.detectFaces(opencvMat);
                List<FaceLocalization> faceLocalizations = faceDetector.getFaceLocalization();
                for (FaceLocalization loc: faceLocalizations){
                    System.out.println(time);
                    int X = (int) loc.getLeft_x();
                    int Y = (int) loc.getLeft_y();
                    int Width = loc.getValidWidth(opencvMat.size().width());
                    int Height = loc.getValidHeight(opencvMat.size().height());
                    System.out.println(X + "," + Y + "," + Width + "," + Height + "," );
                    if(X < 0 || Y <0 || Width <=0 || Height <= 0){continue;}
                    INDArray emb = faceIdentifier.getEmbeddingFromPic(opencvMat,loc);
                    double[] embDouble = emb.toDoubleVector();
                    int[] loci = new int[]{X,Y,Width,Height};


                    if (!out.containsKey(embDouble)){
                        out.put( embDouble, new HashMap<>());
                    }
                    out.get(embDouble).put(time,loci);
                }
            }
        }
        return out;
    }

    private static void annotateFaces(List<FaceLocalization> faceLocalizations, Mat image) {
        for (FaceLocalization i : faceLocalizations){
            rectangle(image,new Rect(new Point((int) i.getLeft_x(),(int) i.getLeft_y()), new Point((int) i.getRight_x(),(int) i.getRight_y())), new Scalar(0, 255, 0, 0),2,8,0);
        }
    }

    public HashMap<String,double[]> extractImageDetected(HashMap<DoublePoint,List<DoublePoint>> cluster, HashMap<double[], HashMap<Long,int[]>> timeLocs, String videoPath) throws Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        // HashMap<String name,>
        grabber.setFormat("mp4");
        grabber.start();
        int width = grabber.getImageWidth();
        int height = grabber.getImageHeight();
        OpenCVFrameConverter.ToMat frame2Mat = new OpenCVFrameConverter.ToMat();
        int nameIdx = 0;
        for(DoublePoint emb: cluster.keySet()){
            double[] pnt = emb.getPoint();
            System.out.println(Arrays.toString(pnt));
            if(timeLocs.get(pnt) == null){
                for(double[] pnts : timeLocs.keySet()){
                    System.out.println("The Points inside:");
                    System.out.println(Arrays.toString(pnts));
                }
                System.out.println("null");
            }
            HashMap<Long, int[]> timeLoc = timeLocs.get(pnt);

            int i = 0;
            Long time = 0L;
            for (Long timeStamp : timeLoc.keySet()){
                if (i == 1){break;}
                System.out.println(timeStamp);
                time = timeStamp;
                i++;
            }
            grabber.setTimestamp(time);
            Frame frame = grabber.grabImage();
            Mat image = frame2Mat.convert(frame);

            int [] loc = timeLoc.get(time);
            int X = loc[0];
            int Y = loc[1];
            int Width = loc[2];
            int Height = loc[3];

            // Crop face, Resize and convert into INDArray
            Mat crop_image = new Mat(image, new Rect(X, Y, Width, Height));

            //resize and save and name
            Mat resizeImage = new Mat();
            resize(crop_image, resizeImage, new Size(50,50));

            String imagePath = "";
            String imageName = "face-"+nameIdx;

            imagePath = "C:\\Users\\Asus\\Desktop\\CDLE project data\\output\\"+imageName+".jpg";

            imwrite(imagePath,resizeImage);

            nameIdx++;
        }

       /* List<Long> timeToGrab = new ArrayList<>();
        for(double[] i :timeLoc.keySet()){
            for(Long s: timeLoc.get(i).keySet()){
                timeToGrab.add(s);
            }
        }
        int i = 0;

        while(i<timeToGrab.size()){
            grabber.setTimestamp(timeToGrab.get(i));
            Frame frame = grabber.grabImage();
        } */
        return null;
    }

}
