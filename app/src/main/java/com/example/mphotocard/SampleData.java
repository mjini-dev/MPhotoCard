package com.example.mphotocard;

public class SampleData {

    //아이템리스트xml에서 만든 위젯4개(이미지뷰2, 텍스트뷰2)를 데이터로서 선언해줘야함

    private int temp_1; //이미지는 int
    private  String tv_temp1;
    private int temp_2;
    private  String tv_temp2;


    //alt+insert -> constructor : 구조만들기 -> 위 4개 데이터를 만들기 위한(파라미터를 갖는) 생성자추가
    public SampleData(int temp_1, String tv_temp1, int temp_2, String tv_temp2) {
        this.temp_1 = temp_1;
        this.tv_temp1 = tv_temp1;
        this.temp_2 = temp_2;
        this.tv_temp2 = tv_temp2;
    }


    //alt+insert -> getter and setter : getter, setter 만들기 -> 4개의 변수를위한 속성(멤버변수)
    public int getTemp_1() {
        return temp_1;
    }

    public void setTemp_1(int temp_1) {
        this.temp_1 = temp_1;
    }

    public String getTv_temp1() {
        return tv_temp1;
    }

    public void setTv_temp1(String tv_temp1) {
        this.tv_temp1 = tv_temp1;
    }

    public int getTemp_2() {
        return temp_2;
    }

    public void setTemp_2(int temp_2) {
        this.temp_2 = temp_2;
    }

    public String getTv_temp2() {
        return tv_temp2;
    }

    public void setTv_temp2(String tv_temp2) {
        this.tv_temp2 = tv_temp2;
    }
}
