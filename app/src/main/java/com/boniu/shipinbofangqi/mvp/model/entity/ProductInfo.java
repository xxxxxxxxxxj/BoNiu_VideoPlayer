package com.boniu.shipinbofangqi.mvp.model.entity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2020-03-09 22:04
 */
public class ProductInfo {
    private double discountPrice;
    private String tag;
    private int days;

    @Override
    public String toString() {
        return "ProductInfo{" +
                "discountPrice=" + discountPrice +
                ", tag='" + tag + '\'' +
                ", days=" + days +
                '}';
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
