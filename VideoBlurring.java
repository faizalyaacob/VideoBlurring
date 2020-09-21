package ai.certifai.solution.classification;

import javafx.util.Pair;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_videoio.VideoWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgproc.blur;

public class VideoBlurring {

    public VideoBlurring() throws Exception{
    }

    public static void main(String[] args) throws Exception {
        String vidPath = "C:\\Users\\scotg\\Downloads\\Video\\SampleVideo1.mp4";
        ai.certifai.solution.facial_recognition.video_reading.VideoReader capture = new ai.certifai.solution.facial_recognition.video_reading.VideoReader();
        HashMap<double[], HashMap<Long,int[]>> out = capture.detectAndEncodeFace(vidPath);
        VideoBlurring blur = new VideoBlurring();
        blur.BlurringAndGenerateVideo(vidPath,out);

    }

    public void BlurringAndGenerateVideo(String vidPath, HashMap<double[],HashMap<Long,int[]>> out) throws Exception{

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(vidPath);
        grabber.setFormat("mp4");
        grabber.start();
        int width = grabber.getImageWidth();
        int height = grabber.getImageHeight();
        System.out.println(grabber.getVideoCodec());

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("output.mp4", 1920  , 1080, 0);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
        recorder.setVideoBitrate(9000);
        recorder.setFormat("mp4");
        recorder.setVideoQuality(0); // maximum quality
        recorder.setFrameRate(15);
        recorder.start();

//        VideoWriter output = new VideoWriter("edited.mp4",2,30.0,new Size(grabber.getImageWidth(),grabber.getImageHeight()),true);

        HashMap<Long,List<int[]>> time_loc = new HashMap<>();

        List<int[]> locations= new ArrayList<>();
        OpenCVFrameConverter.ToMat frame2Mat = new OpenCVFrameConverter.ToMat();
        for(double[] emb: out.keySet()){
            for(long time_stamp: time_loc.keySet()){
                if (!time_loc.containsKey(time_stamp)){
                    time_loc.put(time_stamp,new ArrayList<>());
                }
                time_loc.get(time_stamp).add(out.get(emb).get(time_stamp));
            }
        }
        int i = 0;
        Mat image = new Mat();
        while (grabber.grab() != null) {
            System.out.println(i);
            Frame current_frame = grabber.grabImage();
            image = frame2Mat.convertToMat(current_frame);
            long current_time  = grabber.getTimestamp();

            if (time_loc.containsKey(current_time)){            //Check if the timestamp contains regions needs to be blurred
                locations = time_loc.get(current_time);
                System.out.println(locations);
                for(int[] location : locations) {

                    Rect roi = new Rect(location[0], location[1], location[2], location[3]);
                    blur(image.apply(roi), image.apply(roi), new Size(50, 50));
                }
            }
            recorder.record(frame2Mat.convert(image));
            if (i == 50){

                recorder.stop();
            }
            i++;
        }

    }



    }