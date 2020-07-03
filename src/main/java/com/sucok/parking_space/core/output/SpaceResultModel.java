package com.sucok.parking_space.core.output;

import lombok.Data;

/**
 * @author： wuyajun
 * @date： 2020/6/19 11:10
 * @description： 匹配的结果  因为考虑数据比较大，所以属性名得短
 * @modifiedBy：
 * @version: 1.0
 */
@Data
public class SpaceResultModel {

    /**
     * 文本
     */
    private String t;

    /**
     * 左上 x
     */
    private float x;

    /**
     * 左上 y
     */
    private float y;

    /**
     * 宽
     */
    private float w;

    /**
     * 高
     */
    private float h;

    /**
     * 角度
     */
    private float r;

    public SpaceResultModel(String t, float x, float y, float w, float h) {
        this.t = t;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public SpaceResultModel(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public String toString() {
        return "SpaceResultModel{" +
                "t='" + t + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                ", r=" + r +
                '}';
    }
}
