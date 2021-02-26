package com.example.mphotocard;

public class CardItem_2 {

    private String resId; //이미지경로
    private String content;

    private int txtColor;
    private int txtGravity;
    private float txtSize;

    private float txtShadow_rd;
    private float txtShadow_x;
    private float txtShadow_y;
    private int txtShadow_color;


    public CardItem_2(String resId, String content, int txtColor, int txtGravity, float txtSize, float txtShadow_rd, float txtShadow_x, float txtShadow_y, int txtShadow_color) {
        this.resId = resId;
        this.content = content;
        this.txtColor = txtColor;
        this.txtGravity = txtGravity;
        this.txtSize = txtSize;
        this.txtShadow_rd = txtShadow_rd;
        this.txtShadow_x = txtShadow_x;
        this.txtShadow_y = txtShadow_y;
        this.txtShadow_color = txtShadow_color;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTxtColor() {
        return txtColor;
    }

    public void setTxtColor(int txtColor) {
        this.txtColor = txtColor;
    }

    public int getTxtGravity() {
        return txtGravity;
    }

    public void setTxtGravity(int txtGravity) {
        this.txtGravity = txtGravity;
    }

    public float getTxtSize() {
        return txtSize;
    }

    public void setTxtSize(float txtSize) {
        this.txtSize = txtSize;
    }

    public float getTxtShadow_rd() {
        return txtShadow_rd;
    }

    public void setTxtShadow_rd(float txtShadow_rd) {
        this.txtShadow_rd = txtShadow_rd;
    }

    public float getTxtShadow_x() {
        return txtShadow_x;
    }

    public void setTxtShadow_x(float txtShadow_x) {
        this.txtShadow_x = txtShadow_x;
    }

    public float getTxtShadow_y() {
        return txtShadow_y;
    }

    public void setTxtShadow_y(float txtShadow_y) {
        this.txtShadow_y = txtShadow_y;
    }

    public int getTxtShadow_color() {
        return txtShadow_color;
    }

    public void setTxtShadow_color(int txtShadow_color) {
        this.txtShadow_color = txtShadow_color;
    }






//    public CardItem_2(String resId, String content) {
//        this.resId = resId;
//        this.content = content;
//    }
//
//    public String getResId() {
//        return resId;
//    }
//
//    public void setResId(String resId) {
//
//        this.resId = resId;
//    }
//
//
//    public String getContent() {
//
//        return content;
//    }
//
//
//    public void setContent(String content) {
//
//        this.content = content;
//    }
}
