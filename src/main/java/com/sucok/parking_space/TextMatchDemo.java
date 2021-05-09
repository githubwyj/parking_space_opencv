package com.sucok.parking_space;


import cn.hutool.core.io.FileUtil;
import com.sucok.parking_space.core.tesseract.ImagePreprocessingService;
import com.sucok.parking_space.core.tesseract.TesseractClient;
import com.sucok.parking_space.core.tesseract.TesseractPool;
import com.sucok.parking_space.core.tesseract.TesseractService;
import com.sucok.parking_space.utils.ImageUtil;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.leptonica.PIX;

import java.io.File;
import java.nio.ByteBuffer;

import static org.bytedeco.leptonica.global.lept.*;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;

/**
 * 文字识别demo
 */
@Slf4j
class TextMatchDemo {

    public static void main(String[] args) {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@NonNull String string) {
//                String out = "E:\\Work\\Project\\self\\parking_space_opencv\\src\\main\\resources\\images\\text\\output\\out.png";
//                FileUtil.writeBytes(byteBuffer.array(),new File(out));

                log.info(string);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }

        };
        ImagePreprocessingService imagePreprocessingService = new ImagePreprocessingService();
        new TesseractService().extractTextFromImage(imagePreprocessingService.process(ImageUtil.readFileFromClasspath("images/text/1.png"))).subscribe(observer);
        waitKey(0);
    }

}