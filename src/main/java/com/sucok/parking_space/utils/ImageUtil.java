package com.sucok.parking_space.utils;


import cn.hutool.core.lang.Assert;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.*;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;


import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.*;
import java.net.URL;

import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgproc.*;


/**
 * <pre>
 * 图片工具类
 * Created by githubwyj on 2021/5/9 14:33.
 * </pre>
 *
 * @author <a href="https://github.com/githubwyj">githubwyj</a>
 **/
public class ImageUtil {

    public static OpenCVFrameConverter.ToMat converter1 = new OpenCVFrameConverter.ToMat();
    public static OpenCVFrameConverter.ToOrgOpenCvCoreMat converter2 = new OpenCVFrameConverter.ToOrgOpenCvCoreMat();

    /**
     * 读取classpath图片
     *
     * @param path
     * @param readType
     * @return
     */
    public static Mat readImgFromClasspath(String path, Integer readType) {
        String filePath = readFileFromClasspath(path).getAbsolutePath();
        if (readType != null) {
            return imread(filePath, readType);
        }
        return imread(filePath);
    }

    /**
     * 读取classpath图片
     *
     * @param path
     * @return
     */
    public static File readFileFromClasspath(String path) {
        URL fileUrl = ImageUtil.class.getClassLoader().getResource(path);
        assert fileUrl != null;
        return new File(fileUrl.getFile());
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
     * @param mat   原图片
     * @param angle 旋转角度
     * @param scale 缩放比例
     * @return
     */
    public static org.opencv.core.Mat imageRotate(org.opencv.core.Mat mat, double angle, int scale) {
        Mat src = matCovert(mat);

        //输出图像的宽度或高度
        int length = 0;
        //为了保证无论如何旋转都能放下，输出图像的长和宽都设为输入图像对角线长度乘以SCALE
        //但如果是缩小(SCALE<=1)，这样会导致临时图像中放不下原图，所以对于所有缩小的情况，输出图像和临时图像的长宽都设为原图的对角线长度
        if (scale <= 1) {
            length = (int) Math.sqrt(src.cols() * src.cols() + src.rows() * src.rows());
        } else {
            length = (int) Math.sqrt(src.cols() * src.cols() + src.rows() * src.rows()) * scale;
        }
        //建立临时图像，长宽都是源图像的对角线长度，将源图像复制到临时图像的中心后再变换
        Mat tempImg = new Mat(length, length, src.type());
        int roiX = length / 2 - src.cols() / 2;
        int roiY = length / 2 - src.rows() / 2;
        Rect roiRect = new Rect(roiX, roiY, src.cols(), src.rows());
        Mat tempImgroi2 = new Mat(tempImg, roiRect);
        src.copyTo(tempImgroi2);

        Point2f center = new Point2f(length / 2, length / 2);
        Mat m = getRotationMatrix2D(center, angle, scale);
        //输出图像
        Mat dst = new Mat();
        //仿射变换
        warpAffine(tempImg, dst, m, new Size(length, length));
        return matCovert(dst);
    }

    public static byte[] file2Byte(File file) {
        byte[] buffer = null;
        FileInputStream fis;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
            if (file.exists()) {
                // file.delete();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static org.bytedeco.opencv.opencv_core.Mat matCovert(org.opencv.core.Mat mat) {
        return converter1.convert(converter2.convert(mat));
    }

    public static org.opencv.core.Mat matCovert(org.bytedeco.opencv.opencv_core.Mat mat) {
        return converter2.convert(converter1.convert(mat));
    }


    /**
     * 图片裁剪
     *
     * @param src
     * @param rect
     * @return
     */
    public static Mat cutImage(Mat src, Rect rect) {
        Mat srcRoi = new Mat(src, rect);
        // imshow("srcRoi",srcRoi);
        Mat cutImage = new Mat(rect.width(), rect.height(), CvType.CV_8UC3, new Scalar(255));
        srcRoi.copyTo(cutImage);
//        imshow("cutImage",cutImage);
        return cutImage;
    }

}
