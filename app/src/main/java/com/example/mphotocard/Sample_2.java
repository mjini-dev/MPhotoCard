package com.example.mphotocard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Sample_2 extends AppCompatActivity {

    private ArrayList<SampleData> items;
    private SampleAdapter sampleAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    //private GridLayoutManager gridLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_2);

        Button buttonList = findViewById(R.id.btn_go_list);
        buttonList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), MyCardList.class);
                startActivity(intent4);
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.rv_2);

        linearLayoutManager = new LinearLayoutManager(this); //리싸이클러뷰를 위한 레이아웃매니저객체생성
        recyclerView.setLayoutManager(linearLayoutManager); //레이아웃매니저를 리싸이클려뷰에 설정
//             int numberOfColumns = 3;
//        gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
//        recyclerView.setLayoutManager(gridLayoutManager);


        sampleAdapter = new SampleAdapter(getApplicationContext());

        //데이터설정
        sampleAdapter.addItem(new SampleData(R.drawable.sample,"안녕하세요",R.drawable.dog,"멍멍"));
        sampleAdapter.addItem(new SampleData(R.drawable.jjs,"즐겁다고",R.drawable.sample,"시원하다요"));
        sampleAdapter.addItem(new SampleData(R.drawable.sample,"안녕하세요",R.drawable.dog,"멍멍"));
        sampleAdapter.addItem(new SampleData(R.drawable.jjs,"즐겁다고",R.drawable.sample,"시원하다요"));
        sampleAdapter.addItem(new SampleData(R.drawable.sample,"안녕하세요",R.drawable.dog,"멍멍"));
        sampleAdapter.addItem(new SampleData(R.drawable.jjs,"즐겁다고",R.drawable.sample,"시원하다요"));
        sampleAdapter.addItem(new SampleData(R.drawable.sample,"안녕하세요",R.drawable.dog,"멍멍"));
        sampleAdapter.addItem(new SampleData(R.drawable.jjs,"즐겁다고",R.drawable.sample,"시원하다요"));
        sampleAdapter.addItem(new SampleData(R.drawable.sample,"안녕하세요",R.drawable.dog,"멍멍"));
        sampleAdapter.addItem(new SampleData(R.drawable.jjs,"즐겁다고",R.drawable.sample,"시원하다요"));


        recyclerView.setAdapter(sampleAdapter);

        //findViewById(R.id.temp_1).setOnItemClickListener(new SampleAdapter.OnItemClickListener() {
        sampleAdapter.setOnItemClickListener(new SampleAdapter.OnItemClickListener() {
            @Override
            public void onItemclick(SampleAdapter.ViewHolder holder, View view, int position) {
                SampleData item = sampleAdapter.getItem(position);

                Toast.makeText(getApplicationContext(),position + "템플릿 을 선택했습니다." , Toast.LENGTH_SHORT).show();

                //이미지파일을 비트맵변수(sendBitmap)에 담음
                Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sample);
                //비트맵 변수에 담은 이미지파일을 바이트어레이로 변경
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //Intent intent = new Intent(Sample.this, MakingCard.class);
                Intent intent = new Intent(getApplicationContext(), MakingCard.class);

                intent.putExtra("key","오늘하루도 행복하세요.");

                //바이트어레이 부가데이터로 저장
                intent.putExtra("image",byteArray);

                //액티비티 이동
                startActivity(intent);

                finish();



            }
        });

    }
}
