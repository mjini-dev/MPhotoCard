package com.example.mphotocard;

public class MyCardItem {

    private String cardImg;  //카드이미지 경로
    private String cardContent;
    private String cardTitle;
    private int txtColor;
    private int txtGravity;
    private float txtSize;

    private float txtShadow_rd;
    private float txtShadow_x;
    private float txtShadow_y;
    private int txtShadow_color;

    private float txt_x;
    private float txt_y;



    private int txt_font;

//    private double txt_x;
//    private double txt_y;
//
//    public MyCardItem(String cardImg, String cardContent, String cardTitle, int txtColor, int txtGravity, float txtSize,
//                      float txtShadow_rd, float txtShadow_x, float txtShadow_y, int txtShadow_color, double txt_x, double txt_y) {
//        this.cardImg = cardImg;
//        this.cardContent = cardContent;
//        this.cardTitle = cardTitle;
//        this.txtColor = txtColor;
//        this.txtGravity = txtGravity;
//        this.txtSize = txtSize;
//        this.txtShadow_rd = txtShadow_rd;
//        this.txtShadow_x = txtShadow_x;
//        this.txtShadow_y = txtShadow_y;
//        this.txtShadow_color = txtShadow_color;
//        this.txt_x = txt_x;
//        this.txt_y = txt_y;
//    }
//
//    public double getTxt_x() {
//        return txt_x;
//    }
//
//    public void setTxt_x(double txt_x) {
//        this.txt_x = txt_x;
//    }
//
//    public double getTxt_y() {
//        return txt_y;
//    }
//
//    public void setTxt_y(double txt_y) {
//        this.txt_y = txt_y;
//    }

        public float getTxt_x() {
        return txt_x;
    }

    public void setTxt_x(float txt_x) {
        this.txt_x = txt_x;
    }

    public float getTxt_y() {
        return txt_y;
    }

    public void setTxt_y(float txt_y) {
        this.txt_y = txt_y;
    }

    public int getTxtFont() {
        return txt_font;
    }

    public void setTxtFont(int txtFont) {
        this.txt_font = txtFont;
    }



    public MyCardItem(String cardImg, String cardContent, String cardTitle, int txtColor, int txtGravity, float txtSize,
                      float txtShadow_rd, float txtShadow_x, float txtShadow_y, int txtShadow_color, float txt_x, float txt_y, int txt_font) {
        this.cardImg = cardImg;
        this.cardContent = cardContent;
        this.cardTitle = cardTitle;
        this.txtColor = txtColor;
        this.txtGravity = txtGravity;
        this.txtSize = txtSize;
        this.txtShadow_rd = txtShadow_rd;
        this.txtShadow_x = txtShadow_x;
        this.txtShadow_y = txtShadow_y;
        this.txtShadow_color = txtShadow_color;
        this.txt_x = txt_x;
        this.txt_y = txt_y;
        this.txt_font = txt_font;
    }

//    public MyCardItem(String cardImg, String cardContent) {
//        this.cardImg = cardImg;
//        this.cardContent = cardContent;
//
//    }

//    public MyCardItem(String cardImg, String cardContent, String cardTitle, int txtColor, int txtGravity, float txtSize,
//                      float txtShadow_rd, float txtShadow_x, float txtShadow_y, int txtShadow_color) {
//        this.cardImg = cardImg;
//        this.cardContent = cardContent;
//        this.cardTitle = cardTitle;
//        this.txtColor = txtColor;
//        this.txtGravity = txtGravity;
//        this.txtSize = txtSize;
//        this.txtShadow_rd = txtShadow_rd;
//        this.txtShadow_x = txtShadow_x;
//        this.txtShadow_y = txtShadow_y;
//        this.txtShadow_color = txtShadow_color;
//    }

    public String getCardImg() {
        return cardImg;
    }

    public void setCardImg(String cardImg) {
        this.cardImg = cardImg;
    }

    public String getCardContent() {
        return cardContent;
    }

    public void setCardContent(String cardContent) {
        this.cardContent = cardContent;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public void setCardTitle(String cardTitle) {
        this.cardTitle = cardTitle;
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

}
