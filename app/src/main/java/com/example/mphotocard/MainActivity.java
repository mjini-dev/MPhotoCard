package com.example.mphotocard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_CARD = 101;
    public static final int REQUEST_CODE_SAMPLE = 102;

    Button btn_go_making;
    Button btn_go_list_1;

    ImageView image_ad;
    private int i = 0;


    TextView textView;
    TextView txt_making;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        txt_making = findViewById(R.id.txt_making);
        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf");
//        Typeface typeface1 = ResourcesCompat.getFont(getApplicationContext(),R.font.ss_flower_road);
        //textView.setTypeface(getResources().getFont(R.font.ss_flower_road));
        textView.setTypeface(typeface);
        txt_making.setTypeface(typeface);




        btn_go_making = findViewById(R.id.btn_go_making);
        btn_go_making.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplicationContext(), Sample_3.class);
                //Intent intent1 = new Intent(getApplicationContext(), test.class);
                startActivityForResult(intent1, REQUEST_CODE_SAMPLE);

//                Intent intent1 = new Intent(getApplicationContext(), test.class);
//                startActivity(intent1, REQUEST_CODE_SAMPLE);
            }

        });



        btn_go_list_1 = findViewById(R.id.btn_go_list_1);
        btn_go_list_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MyCardList.class);
                startActivity(intent);
            }
        });


       /* image_ad = findViewById(R.id.image_ad);
        Thread myThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        handler.sendMessage(handler.obtainMessage());
                        Thread.sleep(3000);
                    } catch (Throwable t) {
                    }
                }
            }
        });

        myThread.start();*/


    }


//    @Override
//    protected void onRestart() {
//        super.onRestart();
//
//        Button button = findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                showMessage();
//            }
//
//        });
//    }

    private void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //대화상자를 만들기 위한 빌더객체 생성
        builder.setTitle("안내");
        builder.setMessage("작업중인 카드가 있습니다. \n 작업을 계속 하시겠습니까?");
        //builder.setIcon();

        builder.setPositiveButton("계속하기",
                new DialogInterface.OnClickListener() { //계속하기 버튼 추가
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent intent = new Intent(getApplicationContext(), MakingCard.class);
                intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//                SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
//
//                String content = pref.getString("content","");




                startActivityForResult(intent,REQUEST_CODE_CARD );

            }
        });

        builder.setNegativeButton("신규", new DialogInterface.OnClickListener() { //신규버튼 추가
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                Intent intent = new Intent(getApplicationContext(), Sample_3.class);
                startActivityForResult(intent,REQUEST_CODE_SAMPLE );

            }
        });

        AlertDialog dialog = builder.create(); //대화상자 객체 생성
        dialog.show(); //생성된 대화상자객체 보여주기


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            updateThread();
        }
    };

    private void updateThread() {
        int mod = i % 4;

        switch (mod) {
            case 0:
                i++;
                image_ad.setImageResource(R.drawable.naver);
                image_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent( Intent. ACTION_VIEW, Uri.parse("https://m.naver.com"));
                        startActivity(intent);

                    }
                });
                break;
            case 1:
                i++;
                image_ad.setImageResource(R.drawable.daum);
                image_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent( Intent. ACTION_VIEW, Uri.parse("https://m.daum.net"));
                        startActivity(intent);
                    }
                });
                break;
            case 2:
                i++;
                image_ad.setImageResource(R.drawable.google);
                image_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent( Intent. ACTION_VIEW, Uri.parse("https://google.com"));
                        startActivity(intent);
                    }
                });
                break;
            case 3:
                i = 0;
                image_ad.setImageResource(R.drawable.nova);
                image_ad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent( Intent. ACTION_VIEW, Uri.parse("https://teamnova.co.kr"));
                        startActivity(intent);
                    }
                });
                break;
        }
    }
}
