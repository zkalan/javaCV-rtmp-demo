package com.zkalan.demo;

import com.zkalan.capture.CaptureVideoFromUSBDevice;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;

public class Demo {

    public static void main(String[] args) throws FrameGrabber.Exception, FrameRecorder.Exception, InterruptedException {
        CaptureVideoFromUSBDevice cvusb = new CaptureVideoFromUSBDevice("rtmp://114.212.84.130:1935/mylive/room", 25, 0);
        cvusb.init();
        cvusb.work();
        cvusb.close();
    }
}
