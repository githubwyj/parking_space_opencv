package com.sucok.parking_space.core.tesseract;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.LeptonicaFrameConverter;
import org.bytedeco.leptonica.BOX;
import org.bytedeco.leptonica.BOXA;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.tesseract.TessBaseAPI;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.bytedeco.leptonica.global.lept.*;
import static org.bytedeco.opencv.global.opencv_imgproc.CV_BGR2RGBA;
import static org.bytedeco.opencv.global.opencv_imgproc.cvtColor;
import static org.bytedeco.tesseract.global.tesseract.RIL_TEXTLINE;

/**
 * @author： githubwyj
 * @date： 2020/7/2 11:17
 * @description： 文字识别
 * @modifiedBy：
 * @version: 1.0
 */
public class TextDistinguish {

    /**
     * 图片识别文字
     *
     * @param img
     * @return
     */
    public static String image2Text(Mat img) {
        //TODO 实现停车位编号识别
        TessBaseAPI api = new TessBaseAPI();
//        Mat gray = new Mat();
//        img.copyTo(gray);
//        cvtColor(gray,gray,CV_BGR2RGBA);
//        api.TesseractRect(gray.data(), 1, gray.channels() * gray.size().width(), 0, 0, gray.cols(), gray.rows());
        try {
            URL url = new URL("https://upload.wikimedia.org/wikipedia/commons/thumb/7/74/Computer_modern_sample.svg/1920px-Computer_modern_sample.svg.png");
            File file = null;

            file = Loader.cacheResource(url);
            PIX image = pixRead(file.getAbsolutePath());
            api.SetImage(image);
            BytePointer outText = api.GetUNLVText();
            if (null != outText) {
                String captcha = outText.getString();
                // Destroy used object and release memory
                api.End();
                outText.deallocate();
                //pixDestroy(image);
                api.close();
                return captcha.trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";

    }


}
