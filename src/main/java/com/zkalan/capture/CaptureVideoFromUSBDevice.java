package com.zkalan.capture;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import javax.swing.*;

public class CaptureVideoFromUSBDevice {

    private String pushAddress;
    private Integer frame_rate;
    private int camera_num;

    private OpenCVFrameGrabber grabber;
    private CanvasFrame canvas;
    private OpenCVFrameConverter.ToIplImage converter;
    private FrameRecorder recorder;

    private opencv_core.IplImage grabbed_image;

    public CaptureVideoFromUSBDevice(String pushAddress, Integer frame_rate, int camera_num){
        this.pushAddress = pushAddress;
        this.frame_rate = frame_rate;
        this.camera_num = camera_num;
    }

    /**
     * init resources
     * @throws OpenCVFrameGrabber.Exception
     * @throws FrameRecorder.Exception
     */
    public void init() throws OpenCVFrameGrabber.Exception, FrameRecorder.Exception {
        this.grabber = new OpenCVFrameGrabber(this.camera_num);
        grabber.start();

        this.canvas = new CanvasFrame("摄像头", CanvasFrame.getDefaultGamma() / grabber.getGamma());
        this.canvas.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.canvas.setAlwaysOnTop(false);

        this.converter = new OpenCVFrameConverter.ToIplImage();
        this.grabbed_image = converter.convert(grabber.grab());

        this.recorder = FrameRecorder.createDefault(this.pushAddress, grabbed_image.width(), grabbed_image.height());
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264); // avcodec.AV_CODEC_ID_H264，编码
        recorder.setFormat("flv");//封装格式，如果是推送到rtmp就必须是flv封装格式
        recorder.setFrameRate(frame_rate);

        if (recorder instanceof OpenCVFrameRecorder){
            System.out.println("------OpenCVFrameRecorder------");
        } else if (recorder instanceof  FFmpegFrameRecorder){
            System.out.println("------FFmpegFrameRecorder------");
        }
    }

    /**
     *
     * @throws FrameRecorder.Exception
     * @throws FrameGrabber.Exception
     * @throws InterruptedException
     */
    public void work() throws FrameRecorder.Exception, FrameGrabber.Exception, InterruptedException {
        recorder.start();
        long startTime = 0;
        long timeStamp = 0;
        if (startTime == 0){
            startTime = System.currentTimeMillis();
        }
        Frame frame;
        while (canvas.isDisplayable()){
            frame = grabber.grab();
            canvas.showImage(frame);
            timeStamp = 1000 * (System.currentTimeMillis() - startTime);
            recorder.setTimestamp(timeStamp);
            recorder.record(frame);

            Thread.sleep(45);
        }
    }

    /**
     * release resources
     * @throws FrameGrabber.Exception
     * @throws FrameRecorder.Exception
     */
    public void close() throws FrameGrabber.Exception, FrameRecorder.Exception {
        this.canvas.dispose();
        this.grabber.stop();
        this.recorder.close();
        this.grabbed_image.close();
    }

}

