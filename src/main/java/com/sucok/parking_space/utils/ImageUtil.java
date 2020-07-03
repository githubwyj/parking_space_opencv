package com.sucok.parking_space.utils;


import cn.hutool.core.lang.Assert;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point2f;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Size;


import java.io.File;
import java.net.URL;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.getRotationMatrix2D;
import static org.bytedeco.opencv.global.opencv_imgproc.warpAffine;


/**
 * 图片工具类
 */
public class ImageUtil {

    /**
     * 读取classpath图片
     *
     * @param path
     * @param readType
     * @return
     */
    public static Mat readImgFromClasspath(String path, Integer readType) {
        URL fileUrl = ImageUtil.class.getClassLoader().getResource(path);
        String filePath = new File(fileUrl.getFile()).getAbsolutePath();
        if (readType != null) {
            return imread(filePath, readType);
        }
        return imread(filePath);
    }

    /**
     * 图片内部旋转
     *
     * @param src   原图片
     * @param angle 旋转角度
     * @return
     */
    public static Mat imageRotateOfIn(Mat src, double angle) {
        Mat newImg = new Mat();
        Point2f pt = new Point2f((float) src.cols() / 2, (float) src.rows() / 2);
        Mat r = getRotationMatrix2D(pt, angle, 1.0);
        warpAffine(src, newImg, r, src.size());
        return newImg;


    }

    /**
     * 图片整体旋转
     *
     * @param src   原图片
     * @param angle 旋转角度
     * @param scale 缩放比例
     * @return
     */
    public static Mat imageRotate(Mat src, double angle, int scale) {

        //输出图像的宽度或高度
        int length;
        //为了保证无论如何旋转都能放下，输出图像的长和宽都设为输入图像对角线长度乘以SCALE
        //但如果是缩小(scale<=1)，这样会导致临时图像中放不下原图，所以对于所有缩小的情况，输出图像和临时图像的长宽都设为原图的对角线长度
        if (scale <= 1) {
            length = (int) Math.sqrt(src.cols() * src.cols() + src.rows() * src.rows());
        } else {
            length = (int) Math.sqrt(src.cols() * src.cols() + src.rows() * src.rows()) * scale;
        }
        //建立临时图像，长宽都是源图像的对角线长度，将源图像复制到临时图像的中心后再变换
        //临时图像，大小和输出图像一样大
        Mat tempImg = new Mat(length, length, src.type());
        //ROI矩形左上角的x坐标
        int ROI_x = length / 2 - src.cols() / 2;
        //ROI矩形左上角的y坐标
        int ROI_y = length / 2 - src.rows() / 2;
        //ROI矩形
        Rect ROIRect = new Rect(ROI_x, ROI_y, src.cols(), src.rows());
        Mat tempImgROI2 = new Mat(tempImg, ROIRect);//tempImg的中间部分
        src.copyTo(tempImgROI2);//将原图复制到tempImg的中心

        Point2f center = new Point2f((float) length / 2, (float) length / 2);//旋转中心
        Mat M = getRotationMatrix2D(center, angle, scale);//计算旋转的仿射变换矩阵

        Mat dst = new Mat();
        warpAffine(tempImg, dst, M, new Size(length, length));//仿射变换

        return dst;
    }

}
