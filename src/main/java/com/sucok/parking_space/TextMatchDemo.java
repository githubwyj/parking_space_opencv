package com.sucok.parking_space;


import com.sucok.parking_space.core.factory.MatchingFactory;
import com.sucok.parking_space.core.tesseract.TextDistinguish;
import com.sucok.parking_space.utils.ImageUtil;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.opencv_core.Mat;

/**
 * 文字识别demo
 */
class TextMatchDemo {

    public static void main(String[] args) {

        Mat img = ImageUtil.readImgFromClasspath("images/text/2.png", null);
        opencv_highgui.imshow("result", img);
        System.out.println(TextDistinguish.image2Text(img));
        opencv_highgui.waitKey(0);
        opencv_highgui.destroyAllWindows();
    }

}