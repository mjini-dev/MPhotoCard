package com.example.mphotocard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sample_3 extends AppCompatActivity {

    private SampleAdapter_2 adapter_2;
    private ArrayList<CardItem_2> item_2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_3);

        Button buttonList = findViewById(R.id.btn_go_list);
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), MyCardList.class);
                startActivity(intent4);
            }
        });


        RecyclerView recyclerView = findViewById(R.id.rv_3);
        //레이아웃매니저
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        //레이아웃매니저를 리싸이클러뷰에 set
        recyclerView.setLayoutManager(layoutManager);

        item_2 = new ArrayList<>();

        adapter_2 = new SampleAdapter_2(getApplicationContext());

        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample","아득히 떨어진 곳에서\n아무 관계없는 것들을 보며", Color.WHITE, Gravity.CENTER, 80, 5f, 5f,5f, Color.BLACK));
        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample2", "조금 쓸쓸한 기분으로\n나는 너를 보고픈너를 떠올린다", Color.BLACK, Gravity.CENTER, 80, 0f, 0f,0f, 0));
        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample3","어딘가 너 있는곳에도\n여기와 똑같은 하늘이 뜨나", Color.WHITE, Gravity.CENTER, 80, 5f, 5f,5f, Color.BLACK));
        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample4","문득 걸음이 멈춰지면\n그러면 너도 잠시 나를 떠올려 주라", Color.BLACK, Gravity.CENTER, 80, 0f, 0f,0f, 0));
        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample5", "다 너같다 이리도 많을까\n뜨고 흐르고 설키고 떨어진다", Color.BLACK, Gravity.CENTER, 80, 0f, 0f,0f, 0));
        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample6","별 하나 없는 새카만밤\n나는 너를 유일한 너를 떠올린다", Color.BLACK, Gravity.CENTER, 80, 0f, 0f,0f, 0));
        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample8", "꽃과같은 너\n생일을 축하합니다", Color.BLACK, Gravity.CENTER, 80, 0f, 0f,0f, 0));
        adapter_2.addItem(new CardItem_2("android.resource://com.example.mphotocard/drawable/sample9","환절기\n비염쟁이는 코통스럽습니다", Color.BLACK, Gravity.CENTER, 80, 0f, 0f,0f, 0));

//        adapter_2.addItem(new CardItem_2("file://storage/emulated/0/DCIM/Camera/sample9.jpg","환절기\n비염쟁이는 코통스럽습니다"));
//      imgView.setImageDrawable(ActivityCompat.getDrawable(context,R.drawable.dog));
        adapter_2.notifyDataSetChanged();


        recyclerView.setAdapter(adapter_2);

        //클릭이벤트1(리스너 어뎁터에)
        adapter_2.setOnSampleItemClickListener(new OnSampleItemClickListener() {
            @Override
            public void onItemClicked(SampleAdapter_2.ViewHolder holder, View view, int position) {
                //아이템 클릭 시 어댑터에서 해당아이템의 CardItem 객체 가져오기
                CardItem_2 item_2 = adapter_2.getItem(position);

                Toast.makeText(getApplicationContext(),position + "템플릿 을 선택했습니다." , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MakingCard.class);
                intent.putExtra("content", item_2.getContent());  //내용
                intent.putExtra("resId", item_2.getResId());      //이미지
                intent.putExtra("txtColor", item_2.getTxtColor()); //색상
                intent.putExtra("txtGravity", item_2.getTxtGravity()); //텍스트정렬
                intent.putExtra("txtSize", item_2.getTxtSize()); //텍스트크기

                intent.putExtra("txtShadow_rd",item_2.getTxtShadow_rd());
                intent.putExtra("txtShadow_x",item_2.getTxtShadow_x());
                intent.putExtra("txtShadow_y",item_2.getTxtShadow_y());
                intent.putExtra("txtShadow_color",item_2.getTxtShadow_color());


                startActivity(intent);
                finish();
            }
        });


    }



}
