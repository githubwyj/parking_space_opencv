package com.sucok.parking_space;


import com.sucok.parking_space.core.factory.MatchingFactory;
import com.sucok.parking_space.utils.ImageUtil;
import org.bytedeco.opencv.opencv_core.Mat;

class MatchTemplateDemo {

    public static void main(String[] args) {

        Mat img = ImageUtil.readImgFromClasspath("images/test/2.jpg", null);
        Mat template = ImageUtil.readImgFromClasspath("images/template/black_spot.jpg", null);

        MatchingFactory matchingFactory = new MatchingFactory(img, template);
        System.out.println(matchingFactory.templateMatching(0.98f));
        matchingFactory.showResult();

    }

}