//package com.sucok.parking_space.core;
//
//
//import com.sucok.parking_space.common.DistinguishModelEnum;
//import com.sucok.parking_space.core.factory.MatchingFactory;
//import com.sucok.parking_space.utils.ImageUtil;
//import org.bytedeco.javacpp.DoublePointer;
//import org.bytedeco.javacpp.opencv_core;
//import org.bytedeco.javacpp.opencv_core.*;
//
//import static org.bytedeco.javacpp.opencv_core.minMaxLoc;
//import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
//import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
//import static org.bytedeco.javacpp.opencv_imgproc.*;
//
//public class ParkingSpaceOpenCV {
//
//    /**
//     * 匹配
//     *
//     * @param modelEnum
//     * @param screen
//     * @return
//     */
//    public void matching(DistinguishModelEnum modelEnum, Mat screen) {
//        Mat grayImg = new Mat();
//        //压缩
//        pyrDown(screen, grayImg);
//        pyrDown(screen, grayImg);
//
//        MatchingFactory matchingFactory = new MatchingFactory();
//         //cvtColor(screen, grayImg, COLOR_BGR2GRAY);
//        Rect character = matchingFactory.matchingMethod(modelEnum, grayImg);
//        Point target = matchingFactory.findTarget2(grayImg, character);
//        circle(screen, target, 5, new Scalar(0, 0, 0, 0));
//        rectangle(screen, character, new Scalar(0, 255, 255, 0));
//        imwrite("E:\\Work\\Project\\self\\parking_space_opencv\\" + "1.jpg", screen);
//    }
//
//}
