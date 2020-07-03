package com.sucok.parking_space.core.factory;

import cn.hutool.core.lang.Assert;

import com.sucok.parking_space.core.output.SpaceResultModel;
import com.sucok.parking_space.core.tesseract.TextDistinguish;
import lombok.Getter;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.global.opencv_highgui;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_core.minMaxLoc;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.warpAffine;


/**
 * @author： githubwyj
 * @date： 2020/6/11 16:25
 * @description： 图像模板匹配
 * @modifiedBy：
 * @version: 1.0
 */
public class MatchingFactory {

    private Mat image;
    private Mat template;
    private Mat result;

    private Mat imgDisplay;

    @Getter
    List<SpaceResultModel> spaceResultModelList = new ArrayList<>();


    public MatchingFactory(Mat image, Mat template) {
        this.image = image;
        this.template = template;
        this.result = new Mat();
    }

    public MatchingFactory(Mat image, Mat template, Mat result) {
        this.image = image;
        this.template = template;
        this.result = result;
    }


    /**
     * 匹配的结果
     *
     * @param f 匹配的相似度
     * @return
     */
    public List<SpaceResultModel> templateMatching(float f) {
        Assert.notNull(image);
        Assert.notNull(template);

        int resultCols = image.cols() - template.cols() + 1;
        int resultRows = image.rows() - template.rows() + 1;

        result.create(resultRows, resultCols, CV_32FC1);

        opencv_imgproc.matchTemplate(image, template, result, opencv_imgproc.TM_CCORR_NORMED);
        //归一
        normalize(result, result, 0, 1, NORM_MINMAX, -1, new Mat());
        DoublePointer minVal = new DoublePointer();
        DoublePointer maxVal = new DoublePointer();
        Point min = new Point();
        Point max = new Point();
        //取最匹配的结果
        minMaxLoc(result, minVal, maxVal, min, max, null);
        return this.getTarget(f);
    }

    /**
     * 获取匹配后的结果
     *
     * @param t
     * @return
     */
    private List<SpaceResultModel> getTarget(float t) {
        imgDisplay = new Mat();
        image.copyTo(imgDisplay);

        getPointsFromMatAboveThreshold(result, t).forEach((point) -> {
            Rect rect = new Rect(point.x(), point.y(), template.cols(), template.rows());
            opencv_imgproc.rectangle(imgDisplay, rect, randomColor(), 2, 0, 0);
            Mat imageTemplate = image.apply(rect);
            spaceResultModelList.add(new SpaceResultModel(point.x(), point.y(), imageTemplate.cols(), imageTemplate.rows()));
        });
        this.duplicateFix();
        return spaceResultModelList;
    }


    /**
     * 根据文字去重
     *
     * @return
     */
    private void duplicateFix() {


    }


    public void showResult() {
        imwrite("E:\\Work\\Project\\self\\parking_space_opencv\\log\\result.png", imgDisplay);
        opencv_highgui.imshow("result", imgDisplay);
        opencv_highgui.waitKey(0);
        opencv_highgui.destroyAllWindows();
    }


    /**
     * 随机颜色
     *
     * @return
     */
    public Scalar randomColor() {
        Random rng = new Random();
        int r = rng.nextInt(256);
        int g = rng.nextInt(256);
        int b = rng.nextInt(256);
        return new Scalar(r, g, b, 1);
    }

    /**
     * 获取匹配的点
     *
     * @param m 模板匹配的结果
     * @param t 相似度 最大为1
     * @return
     */
    public static List<Point> getPointsFromMatAboveThreshold(Mat m, float t) {

        List<Point> matches = new ArrayList<Point>();
        FloatIndexer indexer = m.createIndexer();
        for (int y = 0; y < m.rows(); y++) {
            for (int x = 0; x < m.cols(); x++) {
                if (indexer.get(y, x) > t) {
                    matches.add(new Point(x, y));

                }
            }
        }
        return matches;
    }


}
