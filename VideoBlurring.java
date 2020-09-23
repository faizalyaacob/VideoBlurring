package ai.certifai.solution.facial_recognition.video_reading;

import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.bytedeco.opencv.global.opencv_imgproc.blur;

public class VideoBlurring {

    public VideoBlurring() throws Exception{
    }

    public static void main(String[] args) throws Exception {
        String vidPath = "C:\\Users\\Asus\\Desktop\\CDLE project data\\video1_Trim.mp4";
        VideoReader capture = new VideoReader();
        VideoBlurring blur = new VideoBlurring();
        HashMap<double[],HashMap<Long,int[]>> embTimeLoc = capture.detectAndEncodeFace(vidPath);
        List<double[]> faceToBlur = new ArrayList<>();
        HashMap<Long, List<int[]>> embMap = blur.generateTimeLocs(faceToBlur,embTimeLoc);
        for(long time: embMap.keySet()){
            System.out.println(time);
            System.out.println(embMap.get(time).size());
        }
        blur.BlurringAndGenerateVideo(vidPath,embMap);
    }

    public HashMap<Long, List<int[]>> generateTimeLocs (List<double[]> faceToBlur, HashMap<double[], HashMap<Long, int[]>> embTimeLoc){
        HashMap<Long, List<int[]>> output= new HashMap<>();
        // should use double[] face: faceToBlur
        for(double[] face: embTimeLoc.keySet()){
            if(embTimeLoc.containsKey(face)){
                HashMap<Long,int[]> timeloc = embTimeLoc.get(face);
                for(long time: timeloc.keySet()){
                    if(!output.containsKey(time)){
                        output.put(time,new ArrayList<>());
                    }
                    output.get(time).add(timeloc.get(time));
                }
            }
        }
        return output;
    }

    public void BlurringAndGenerateVideo(String vidPath, HashMap<Long, List<int[]>> timeLocs) throws Exception{

        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(vidPath);
        grabber.setFormat("mp4");
        grabber.start();
        int width = grabber.getImageWidth();
        int height = grabber.getImageHeight();
        System.out.println(grabber.getVideoCodec());

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("output.mp4", width  , height, 0);
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);
        recorder.setVideoBitrate(9000);
        recorder.setFormat("mp4");
        recorder.setVideoQuality(0); // maximum quality
        recorder.setFrameRate(24);
        recorder.start();

//        VideoWriter output = new VideoWriter("edited.mp4",2,30.0,new Size(grabber.getImageWidth(),grabber.getImageHeight()),true);

        OpenCVFrameConverter.ToMat frame2Mat = new OpenCVFrameConverter.ToMat();
        while (grabber.grab() != null) {
            Frame current_frame = grabber.grabImage();
            long currentTime = grabber.getTimestamp();
            if(current_frame != null) {
                Mat image = frame2Mat.convertToMat(current_frame);
                if(timeLocs.containsKey(grabber.getTimestamp())) {
                    List<int[]> listLocs = timeLocs.get(currentTime);

                    for(int[] location : listLocs){
                        Rect roi = new Rect(location[0], location[1], location[2], location[3]);
                        blur(image.apply(roi), image.apply(roi), new Size(50, 50));
                    }
                }

                recorder.record(frame2Mat.convert(image));
            }
        }
        recorder.stop();
    }



}