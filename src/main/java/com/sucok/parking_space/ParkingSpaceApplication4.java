package com.sucok.parking_space;


import cn.hutool.core.io.FileUtil;
import com.sucok.parking_space.utils.ImageUtil;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.opencv_core.*;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_core.normalize;
import static org.bytedeco.opencv.global.opencv_highgui.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


public class ParkingSpaceApplication4 {


    public static void main(String[] args) {
        FileUtil.clean("E:\\Work\\Project\\self\\parking_space_opencv\\log\\");
        FileUtil.mkdir("E:\\Work\\Project\\self\\parking_space_opencv\\log\\result");
        FileUtil.mkdir("E:\\Work\\Project\\self\\parking_space_opencv\\log\\images");
        String[] images = {"images/test/3.jpg", "images/template/black_spot.jpg"};
        //read in image default colors
        Mat img = ImageUtil.readImgFromClasspath(images[0], null);
        //load in template in grey
        Mat templ = ImageUtil.readImgFromClasspath(images[1], null);//int = 0


        int result_cols = img.cols() - templ.cols() + 1;
        int result_rows = img.rows() - templ.rows() + 1;
        //Size for the result image
//        for (int i = 0; i < 1; i++) {
//            pyrDown(img, img, new Size(img.cols() / 2, img.rows() / 2), BORDER_DEFAULT);
//            pyrDown(templ, templ, new Size(templ.cols() / 2, templ.rows() / 2), BORDER_DEFAULT);
//        }
//        TM_SQDIFF = 0,
//                TM_SQDIFF_NORMED = 1,
//                TM_CCORR = 2,
//                TM_CCORR_NORMED = 3,
//                TM_CCOEFF = 4,
//                TM_CCOEFF_NORMED = 5;
        int method = Imgproc.TM_CCOEFF_NORMED;
        Mat result = new Mat();
        result.create(result_rows, result_cols, CvType.CV_32FC1);
        Mat img_display = new Mat();
        img.copyTo(img_display);


        double angleRange = 360;
        double angleStep = 10;
        double angleStart = 345.5;

        //for (int i = 0; i <= (int) angleRange / angleStep; i++) {
        for (int i = 0; i <= 0; i++) {
            DoublePointer minValT = new DoublePointer();
            DoublePointer maxValT = new DoublePointer();
            Point minLocT = new Point();
            Point maxLocT = new Point();
            double angleT = angleStart + angleStep * i;
            Mat newImg = imageRotate(img, angleT);
            matchTemplate(newImg, templ, result, method);
            imwrite("E:\\Work\\Project\\self\\parking_space_opencv\\log\\" + angleT + ".png", newImg);
            minMaxLoc(result, minValT, maxValT, minLocT, maxLocT, null);
            normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
            getPointsFromMatAboveThreshold(result, 0.97f).stream().forEach((point) -> {
                System.out.println(+point.x() + "_" + point.y() + "_" + angleT);
                double yds = newImg.cols() - img.cols() ; //183
                double xds = newImg.rows() - img.rows();
                rectangle(newImg, new Rect(point.x(), point.y(), templ.cols(), templ.rows()), randomColor(), 2, 0, 0);
                imwrite("E:\\Work\\Project\\self\\parking_space_opencv\\log\\images\\" + point.x() + "_" + point.y() + "_" + angleT + ".png", newImg);
                Point2d oldPoint = getPointsBeforeImageRotate(new Point2d((double) point.x(), (double) point.y()), angleT);
                RotatedRect rRect = new RotatedRect(new Point2f((float) (oldPoint.x() + (templ.cols() / 2)), (float) (oldPoint.y() + (templ.rows() / 2))), new Size2f(templ.cols(), templ.rows()), (float) ( angleT)); //定义一个旋转矩形

                Point2f vertices = new Point2f(8);
                rRect.points(vertices);
                FloatBuffer floats = vertices.asBuffer();
                Point[] points = new Point[4];
                for (int j = 0; j < points.length; j++) {
                    Point2d p = getPointsBeforeImageRotate(new Point2d(floats.get(2 * j), floats.get(2 * j + 1)), angleT );
                    points[j] = new Point((int) p.x(), (int) p.y());

                    // System.out.println(j + "+" + (int) floats.get(2 * j) + "_" + (int) floats.get(2 * j + 1));
                }
                //逐条边绘制
                for (int k = 0; k < 4; k++) {
                    line(img_display, points[k], points[(k + 1) % 4], randomColor());//四个角点连成线，最终形成旋转的矩形。
                }


                Rect rect = rRect.boundingRect();

               // rectangle(img_display, rect, randomColor());
                try {
                    Mat image_roi = img.apply(rect);
                    if (image_roi.rows() < templ.rows() + 15 && image_roi.cols() < templ.cols() + 15) {
                        BytePointer bytePointer = new BytePointer("E:\\Work\\Project\\self\\parking_space_opencv\\log\\result\\" + System.currentTimeMillis() + ".png");
                        imwrite(bytePointer, image_roi);

                    } else {
                        // BytePointer bytePointer = new BytePointer("E:\\Work\\Project\\self\\parking_space_opencv\\log\\"+System.currentTimeMillis()+".png");
                        // imwrite(bytePointer, image_roi);
                    }

                    // TessBaseAPI api = new TessBaseAPI();
                } catch (Exception e) {

                }
//                imwrite("E:\\Work\\Project\\self\\parking_space_opencv\\log\\1.png", image_roi);
//                TessBaseAPI api = new TessBaseAPI();
//                PIX image = pixRead("E:\\Work\\Project\\self\\parking_space_opencv\\log\\1.png");
//                api.SetImage(image);
//
//                // Lookup all component images
//                int[] blockIds = {};
//                BOXA boxes = api.GetComponentImages(RIL_TEXTLINE, true, null, blockIds);
//                if(null != boxes){
//                    BytePointer outText;
//                    for (int m = 0; m < boxes.n(); m++) {
//                        // For each image box, OCR within its area
//                        BOX box = boxes.box(m);
//                        api.SetRectangle(box.x(), box.y(), box.w(), box.h());
//                        outText = api.GetUTF8Text();
//                        String ocrResult = outText.getString();
//                        int conf = api.MeanTextConf();
//
//                        String boxInformation = String.format("Box[%d]: x=%d, y=%d, w=%d, h=%d, confidence: %d, text: %s", m, box.x(), box.y(), box.w(), box.h(), conf, ocrResult);
//                        System.out.println(boxInformation);
//
//                        outText.deallocate();
//                    }
//
//
//                }
//                // Destroy used object and release memory
//                api.End();
//                pixDestroy(image);

            });

        }


        imshow("Original marked", img_display);

        imwrite("E:\\Work\\Project\\self\\parking_space_opencv\\log\\result.png", img_display);
        waitKey(0);


        destroyAllWindows();

    }

    public static Point2d getPointsBeforeImageRotate(Point2d newPoint, double angle) {
        double x = newPoint.x() * Math.cos(angle) + newPoint.y() * Math.sin(angle);
        double y = -newPoint.x() * Math.sin(angle) + newPoint.y() * Math.cos(angle);
        return new Point2d(x, y);
    }

    public static Scalar randomColor() {
        Random rng = new Random();
        int r = rng.nextInt(256);
        int g = rng.nextInt(256);
        int b = rng.nextInt(256);
        return new Scalar(r, g, b, 1);
    }

    public static List<Point> getPointsFromMatAboveThreshold(Mat m, float t) {

        List<Point> matches = new ArrayList<Point>();
        FloatIndexer indexer = m.createIndexer();
        for (int y = 0; y < m.rows(); y++) {
            for (int x = 0; x < m.cols(); x++) {
                if (indexer.get(y, x) > t) {
                    //System.out.println("(" + x + "," + y +") = "+ indexer.get(y,x));
                    matches.add(new Point(x, y));

                }
            }
        }
        return matches;
    }

    public static Mat imageRotate(Mat src, double angle) {
        Mat newImg = new Mat();
        Point2f pt = new Point2f((float) src.cols() / 2, (float) src.rows() / 2);
        Mat r = getRotationMatrix2D(pt, angle, 1.0);
        warpAffine(src, newImg, r, src.size());

        return newImg;


//        int SCALE = 1;
//        int length;//输出图像的宽度或高度
//        //为了保证无论如何旋转都能放下，输出图像的长和宽都设为输入图像对角线长度乘以SCALE
//        //但如果是缩小(SCALE<=1)，这样会导致临时图像中放不下原图，所以对于所有缩小的情况，输出图像和临时图像的长宽都设为原图的对角线长度
//        if (SCALE <= 1) {
//            length = (int) Math.ceil(Math.sqrt(src.cols() * src.cols() + src.rows() * src.rows()));
//        } else {
//            length = (int) Math.ceil(Math.sqrt(src.cols() * src.cols() + src.rows() * src.rows()) * SCALE);
//        }
//        //建立临时图像，长宽都是源图像的对角线长度，将源图像复制到临时图像的中心后再变换
//        Mat tempImg = new Mat(length, length, src.type());//临时图像，大小和输出图像一样大
//        int ROI_x = length / 2 - src.cols() / 2;//ROI矩形左上角的x坐标
//        int ROI_y = length / 2 - src.rows() / 2;//ROI矩形左上角的y坐标
//        Rect ROIRect = new Rect(ROI_x, ROI_y, src.cols(), src.rows());//ROI矩形
//        Mat tempImgROI2 = new Mat(tempImg, ROIRect);//tempImg的中间部分
//        src.copyTo(tempImgROI2);//将原图复制到tempImg的中心
//
//        Point2f center = new Point2f(length / 2, length / 2);//旋转中心
//        Mat M = getRotationMatrix2D(center, angle, SCALE);//计算旋转的仿射变换矩阵
//
//        //输出看看算出的矩阵是什么
//        Mat dst = new Mat();//输出图像
//
//        warpAffine(tempImg, dst, M, new Size(length, length));//仿射变换
//
//        return dst;
    }


}