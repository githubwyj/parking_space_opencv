package com.sucok.parking_space.core.tesseract;

import com.sucok.parking_space.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.tesseract.TessBaseAPI;

/**
 * @FileName: cn.netdiscovery.imageprocess.tesseract.TesseractClient
 * @author: Tony Shen
 * @date: 2020-04-04 17:57
 * @version: V1.0 <描述当前版本功能>
 */
@Slf4j
public class TesseractClient {

    /**
     * 字体库语言
     * https://github.com/tesseract-ocr/tessdata 下载
     */
    private static final String LANGUAGE = "chi_sim+eng";

    private final TessBaseAPI tessBaseAPI;

    public TesseractClient() {
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.SetVariable("tessedit_char_whitelist","0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");

        tessBaseAPI.Init(ImageUtil.readFileFromClasspath("tessdata").getAbsolutePath(), LANGUAGE, 1);
        this.tessBaseAPI = tessBaseAPI;
        log.info("Created new TesseractClient Instance");
    }

    public String getTextFrom(PIX image) {
        tessBaseAPI.SetImage(image);
        BytePointer pointer = tessBaseAPI.GetUTF8Text();
        return pointer == null ? "" : pointer.getString();
    }

    public void deallocate() {
        tessBaseAPI.End();
    }

    public void release() {
        tessBaseAPI.Clear();
        tessBaseAPI.ClearAdaptiveClassifier();
    }
}
