package com.example.mphotocard;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Dictionary;

public class MyCardList extends AppCompatActivity {

    //배너광고
    private AdView mAdView;

//


    private int i = 0;

    private RecyclerView recyclerView;
    private ArrayList<MyCardItem> items = new ArrayList<>();
    private MyCardListAdapter adapter;

    private ImageView list_title_img;
    private TextView list_content_text;
    private TextView list_title_text;

    private Uri uri;


    String cardImg = "";
    String cardContent = "";
    String cardTitle = "";
    int txtColor = Color.WHITE;
    int txtGravity = Gravity.CENTER_HORIZONTAL;
    float txtSize = 20f;

    float txtShadow_rd = 0f;
    float txtShadow_x = 0f;
    float txtShadow_y = 0f;
    int txtShadow_color = 0;

    float txt_x = 280f;
    float txt_y = 600f;

    int txt_font = 0;

//    double txt_x = 280f;
//    double txt_y = 600f;






    Context context;
    OnMyCardItemClickListener listener;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_card_list);

//==================배너광고=========================================
        //MobileAds.initialize(this, "YOUR_ADMOB_APP_ID");
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

//        Thread myThread = new Thread(new Runnable() {
//            public void run() {
//                while (true) {
//                    try {
//                        handler.sendMessage(handler.obtainMessage());
//                        Thread.sleep(1000);
//                    } catch (Throwable t) {
//                    }
//                }
//            }
//        });
//
//        myThread.start();


//==================배너광고=========================================


        list_title_img = findViewById(R.id.list_title_img);
        list_content_text = findViewById(R.id.list_content_text);
        list_title_text = findViewById(R.id.list_title_text);

        recyclerView = findViewById(R.id.rv_4);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        //리스트 역순으로 출력
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyCardListAdapter(items);
        recyclerView.setAdapter(adapter);

        getJasonPref();


        adapter.setOnItemClickListener(new MyCardListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, final int pos) {

//                Toast.makeText(getApplicationContext(), pos + "짧게누름", Toast.LENGTH_SHORT).show();
//                Intent intent1 = new Intent(getApplicationContext(),MakingCard.class);
//
//                intent1.putExtra("content", adapter.items.get(pos).getCardContent());
//                intent1.putExtra("resId", adapter.items.get(pos).getCardImg());
//                intent1.putExtra("txtColor", adapter.items.get(pos).getTxtColor());
//                intent1.putExtra("txtGravity", adapter.items.get(pos).getTxtGravity());
//                intent1.putExtra("txtSize", adapter.items.get(pos).getTxtSize());
//
//                startActivity(intent1);
//                finish();

                final PopupMenu p = new PopupMenu(
                        MyCardList.this, // 현재 화면의 제어권자
                        v, Gravity.RIGHT); // anchor : 팝업을 띄울 기준될 위젯
                getMenuInflater().inflate(R.menu.menu_main, p.getMenu());
                // 이벤트 처리
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Toast.makeText(MyCardList.this, "팝업메뉴 이벤트 처리 - " + item.getTitle(), Toast.LENGTH_SHORT).show();

                        switch (item.getItemId()) {
                            case R.id.editing:
                                Intent intent1 = new Intent(getApplicationContext(), MakingCard.class);

                                intent1.putExtra("title",adapter.items.get(pos).getCardTitle());
                                intent1.putExtra("content", adapter.items.get(pos).getCardContent());
                                intent1.putExtra("resId", adapter.items.get(pos).getCardImg());
                                intent1.putExtra("txtColor", adapter.items.get(pos).getTxtColor());
                                intent1.putExtra("txtGravity", adapter.items.get(pos).getTxtGravity());
                                intent1.putExtra("txtSize", adapter.items.get(pos).getTxtSize());

                                float txtShadow_rd = adapter.items.get(pos).getTxtShadow_rd();
                                float txtShadow_x = adapter.items.get(pos).getTxtShadow_x();
                                float txtShadow_y = adapter.items.get(pos).getTxtShadow_y();
                                int txtShadow_color = adapter.items.get(pos).getTxtShadow_color();



                                intent1.putExtra("txtShadow_rd", txtShadow_rd);
                                intent1.putExtra("txtShadow_x", txtShadow_x);
                                intent1.putExtra("txtShadow_y", txtShadow_y);
                                intent1.putExtra("txtShadow_color", txtShadow_color);


                                float txt_x = adapter.items.get(pos).getTxt_x()+279f;
                                float txt_y = adapter.items.get(pos).getTxt_y()+599f;
                                intent1.putExtra("txt_x",txt_x);
                                intent1.putExtra("txt_y",txt_y);

                                int position1 = pos;
                                intent1.putExtra("pos",pos);

                                startActivity(intent1);
                                finish();
                                break;




                            case R.id.modify:

                                Intent intent2 = new Intent(getApplicationContext(), EditingCard.class);

                                intent2.putExtra("title",adapter.items.get(pos).getCardTitle());
                                intent2.putExtra("content", adapter.items.get(pos).getCardContent());
                                intent2.putExtra("resId", adapter.items.get(pos).getCardImg());
                                intent2.putExtra("txtColor", adapter.items.get(pos).getTxtColor());
                                intent2.putExtra("txtGravity", adapter.items.get(pos).getTxtGravity());
                                intent2.putExtra("txtSize", adapter.items.get(pos).getTxtSize());

                                float txtShadow_rd_edt = adapter.items.get(pos).getTxtShadow_rd();
                                float txtShadow_x_edt = adapter.items.get(pos).getTxtShadow_x();
                                float txtShadow_y_edt = adapter.items.get(pos).getTxtShadow_y();
                                int txtShadow_color_edt = adapter.items.get(pos).getTxtShadow_color();

                                intent2.putExtra("txtShadow_rd", txtShadow_rd_edt);
                                intent2.putExtra("txtShadow_x", txtShadow_x_edt);
                                intent2.putExtra("txtShadow_y", txtShadow_y_edt);
                                intent2.putExtra("txtShadow_color", txtShadow_color_edt);


                                float txt_x_edt = adapter.items.get(pos).getTxt_x()+279f;
                                float txt_y_edt = adapter.items.get(pos).getTxt_y()+599f;
                                intent2.putExtra("txt_x",txt_x_edt);
                                intent2.putExtra("txt_y",txt_y_edt);

                                int position = pos;
                                intent2.putExtra("pos",pos);



                                startActivity(intent2);
                                finish();

                                break;


//                            case R.id.share:
//
//                                final RelativeLayout capture = findViewById(R.id.ral_list);//캡쳐할영역
//
//                                capture.buildDrawingCache();
//                                capture.setDrawingCacheEnabled(true);
//                                Bitmap bitmap = capture.getDrawingCache();
//
//                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//
//                                String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "uri", null);
//                                Uri uri_1 = Uri.parse(path);
//
//                                MediaScannerConnection.scanFile(MyCardList.this, //앨범에 사진을 보여주기 위해 Scan을 합니다.
//                                        new String[]{uri_1.getPath()}, null,
//                                        new MediaScannerConnection.OnScanCompletedListener() {
//                                            public void onScanCompleted(String path, Uri uri) {
//                                            }
//                                        });
//
//
//                                // OS 콘텐츠 공유를 통한 Image를 전송할 때
//                                //Intent intent = new Intent(Intent.ACTION_SEND);
//                                Intent intent = new Intent();
//                                intent.setAction(Intent.ACTION_SEND);
//                                intent.setType("image/*");
//                                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(uri_1.toString()));
//                                intent.setPackage("com.kakao.talk");
//                                startActivity(intent);
//                                break;

                            case R.id.deletion:

                                AlertDialog.Builder builder = new AlertDialog.Builder(MyCardList.this); //컨텍스트 !!! 잘확인학!!!!!
                                builder.setTitle("안내");
                                builder.setMessage("해당 항목을 삭제하시겠습니까?");

                                builder.setPositiveButton("삭제",
                                        new DialogInterface.OnClickListener() { //계속하기 버튼 추가
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                //SharedPreferences pref = getSharedPreferences("j_pref", MODE_PRIVATE);
                                                //pref.edit().remove().apply();


//                        int count, checked;
//                        count = adapter.getItemCount();
//
//                        if (count > 0) {
//                            // 현재 선택된 아이템의 position 획득.
//                            //checked = listview.getCheckedItemPosition();
//                            checked = recyclerView.getChildLayoutPosition(recyclerView);
//
//                            if (checked > -1 && checked < count) {
//                                // 아이템 삭제
//                                items.remove(checked);
//
//                                // listview 선택 초기화.
////                        recyclerView.clear
//                                // listview 갱신.
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        }

                                                adapter.items.remove(pos);

                                                //adapter.notifyItemRangeChanged(pos,items.size());
                                                adapter.notifyItemRemoved(pos);


                                                SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
                                                String json = j_pref.getString("list", null);
                                                SharedPreferences.Editor editor = j_pref.edit();

                                                try {
                                                    JSONArray jsonArray = new JSONArray(json);

                                                    jsonArray.remove(pos);

                                                    String n_json = jsonArray.toString();

                                                    editor.putString("list", n_json);
                                                    editor.apply();

                                                    Log.d("제이슨저장되냐고 뉴뉴 : ", n_json + "");

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        });


                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() { //취소버튼 추가
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {


                                    }
                                });
                                //AlertDialog dialog = builder.create(); //대화상자 객체 생성
                                builder.show(); //생성된 대화상자객체 보여주기

                        }


                        return false;
                    }
                });
                p.show(); // 메뉴를 띄우기
            }
        });


        adapter.setOnItemLongClickListener(new MyCardListAdapter.OnItemLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemLongClick(View v, final int pos) {
                Toast.makeText(getApplicationContext(), pos + "길게누름", Toast.LENGTH_SHORT).show();


                AlertDialog.Builder builder = new AlertDialog.Builder(MyCardList.this); //컨텍스트 !!! 잘확인학!!!!!
                builder.setTitle("안내");
                builder.setMessage("해당 항목을 삭제하시겠습니까?");

                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() { //계속하기 버튼 추가
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                //SharedPreferences pref = getSharedPreferences("j_pref", MODE_PRIVATE);
                                //pref.edit().remove().apply();


//                        int count, checked;
//                        count = adapter.getItemCount();
//
//                        if (count > 0) {
//                            // 현재 선택된 아이템의 position 획득.
//                            //checked = listview.getCheckedItemPosition();
//                            checked = recyclerView.getChildLayoutPosition(recyclerView);
//
//                            if (checked > -1 && checked < count) {
//                                // 아이템 삭제
//                                items.remove(checked);
//
//                                // listview 선택 초기화.
////                        recyclerView.clear
//                                // listview 갱신.
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        }

                                adapter.items.remove(pos);
                                //adapter.notifyItemRangeChanged(pos,items.size());
                                adapter.notifyItemRemoved(pos);


                                SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
                                String json = j_pref.getString("list", null);
                                SharedPreferences.Editor editor = j_pref.edit();

                                try {
                                    JSONArray jsonArray = new JSONArray(json);

                                    jsonArray.remove(pos);

                                    String n_json = jsonArray.toString();

                                    editor.putString("list", n_json);
                                    editor.apply();

                                    Log.d("제이슨저장되냐고 뉴뉴 : ", n_json + "");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() { //취소버튼 추가
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {


                    }
                });
                //AlertDialog dialog = builder.create(); //대화상자 객체 생성
                builder.show(); //생성된 대화상자객체 보여주기


//                adapter.items.remove(pos);
//                //adapter.notifyItemRangeChanged(pos,items.size());
//                adapter.notifyItemRemoved(pos);
//
//
//                SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
//                String json = j_pref.getString("list", null);
//                SharedPreferences.Editor editor = j_pref.edit();
//
//                try {
//                    JSONArray jsonArray = new JSONArray(json);
//
//                    jsonArray.remove(pos);
//
//                    String n_json = jsonArray.toString();
//
//                    editor.putString("list", n_json);
//                    editor.apply();
//
//                    Log.d("제이슨저장되냐고 뉴뉴 : ", n_json + "");
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //adapter.notifyDataSetChanged();
//                //adapter.notifyItemRangeChanged(0,items.size());

            }
        });


    }


    private void showMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //대화상자를 만들기 위한 빌더객체 생성
        builder.setTitle("안내");
        builder.setMessage("해당 항목을 삭제하시겠습니까?");
        //builder.setIcon();

        builder.setPositiveButton("삭제",
                new DialogInterface.OnClickListener() { //계속하기 버튼 추가
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        SharedPreferences pref = getSharedPreferences("j_pref", MODE_PRIVATE);
                        //pref.edit().remove().apply();


//                        int count, checked;
//                        count = adapter.getItemCount();
//
//                        if (count > 0) {
//                            // 현재 선택된 아이템의 position 획득.
//                            //checked = listview.getCheckedItemPosition();
//                            checked = recyclerView.getChildLayoutPosition(recyclerView);
//
//                            if (checked > -1 && checked < count) {
//                                // 아이템 삭제
//                                items.remove(checked);
//
//                                // listview 선택 초기화.
////                        recyclerView.clear
//                                // listview 갱신.
//                                adapter.notifyDataSetChanged();
//                            }
//
//                        }

                    }
                });


        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() { //취소버튼 추가
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


            }
        });

        AlertDialog dialog = builder.create(); //대화상자 객체 생성
        dialog.show(); //생성된 대화상자객체 보여주기

    }


//    //============JSON테스트---대실패
//    private void receiveArray(String dataObject) {
//        items.clear();
//        try {
//
//            // String 으로 들어온 값 JSONObject 로 1차 파싱
//            JSONObject wrapObject = new JSONObject(dataObject);
//
//            // JSONObject 의 키 "list" 의 값들을 JSONArray 형태로 변환
//            JSONArray jsonArray = new JSONArray(wrapObject.getString("list"));
//            for(int i = 0; i < jsonArray.length(); i++){
//                // Array 에서 하나의 JSONObject 를 추출
//                JSONObject dataJsonObject = jsonArray.getJSONObject(i);
//                // 추출한 Object 에서 필요한 데이터를 표시할 방법을 정해서 화면에 표시
//                // RecyclerView 로 데이터를 표시 함
//                items.add(new MyCardItem(dataJsonObject.getString("profilePic")+i,dataJsonObject.getString("content")+i,
//                        dataJsonObject.getString("title")+i));
//            }
//            // Recycler Adapter 에서 데이터 변경 사항을 체크하라는 함수 호출
//            adapter.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    //============JSON테스트2---
//    private void receiveArray() {
//        //items.clear();
//        try {
//
//            // String 으로 들어온 값 JSONObject 로 1차 파싱
//            //JSONObject wrapObject = new JSONObject(dataObject);
//
//            // JSONObject 의 키 "list" 의 값들을 JSONArray 형태로 변환
//
//            //JSONObject jsonObject = new JSONObject();
//            JSONArray jsonArray = new JSONArray();
//
//
//            for(int i = 0; i < jsonArray.length(); i++){
//                // Array 에서 하나의 JSONObject 를 추출
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                // 추출한 Object 에서 필요한 데이터를 표시할 방법을 정해서 화면에 표시
//                // RecyclerView 로 데이터를 표시 함
//
//                String profilePic = jsonObject.getString("profilePic")+i;
//                String content = jsonObject.getString("content")+i;
//                String title = jsonObject.getString("title")+i;
//                int txtColor = jsonObject.getInt("txtColor")+i;
//                int txtGravity = jsonObject.getInt("txtGravity")+i;
//                float txtSize = jsonObject.getInt("txtSize")+i;
//
////                items.add(new MyCardItem(
////                        jsonObject.getString("profilePic")+i,
////                        jsonObject.getString("content")+i,
////                        jsonObject.getString("title")+i),
////                        );
//                items.add(new MyCardItem(profilePic,content,title,txtColor,txtGravity,txtSize));
//            }
//            // Recycler Adapter 에서 데이터 변경 사항을 체크하라는 함수 호출
//            adapter.notifyDataSetChanged();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


    //============JSON테스트3---

    private void getJasonPref() {
        SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
        String json = j_pref.getString("list", "");
        Log.e("받아오긴하냐고1 : ", json + "");

        items.clear();


            try {
                JSONArray jsonArray = new JSONArray(json);

                Log.e("어레이","래래읏"+jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {


                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    cardImg = jsonObject.getString("profilePic");
                    Log.e("1",cardImg+"");
                    cardContent = jsonObject.getString("content");
                    Log.e("1",cardContent+"");
                    cardTitle = jsonObject.getString("title");
                    Log.e("1",cardTitle+"");
                    txtColor = jsonObject.getInt("txtColor");
                    Log.e("1",txtColor+"");
                    txtGravity = jsonObject.getInt("txtGravity");
                    Log.e("1",txtGravity+"");
                    txtSize = Float.valueOf(jsonObject.getString("txtSize"));
                    Log.e("1",txtSize+"");
                    //txtShadow_rd = jsonObject.getInt("txtShadow_rd");
                    //txtShadow_rd = jsonObject.getDouble("txtShadow_rd");
//                    txtShadow_rd = Float.valueOf(jsonObject.getString("txtShadow_rd"));
//                    txtShadow_x = Float.valueOf(jsonObject.getString("txtShadow_x"));
//                    txtShadow_y = Float.valueOf(jsonObject.getString("txtShadow_y"));

                    txtShadow_rd = Float.parseFloat(jsonObject.getString("txtShadow_rd"));
                    Log.e("1",txtShadow_rd+"");
                    txtShadow_x = Float.parseFloat(jsonObject.getString("txtShadow_x"));
                    Log.e("1",txtShadow_x+"");
                    txtShadow_y = Float.parseFloat(jsonObject.getString("txtShadow_y"));
                    Log.e("1",txtShadow_y+"");
                    txtShadow_color = jsonObject.getInt("txtShadow_color");
                    Log.e("12222",txtShadow_color+"");


                    String test =jsonObject.getString("txt_x");
                    Log.e("있냐?0",test+"");

                    txt_x = Float.valueOf(jsonObject.getString("txt_x"))-278f;
                    Log.e("13333",txt_x+"");
                    txt_y = Float.valueOf(jsonObject.getString("txt_y"))-601f;
                    Log.e("1444",txt_y+"");



                    txt_font = jsonObject.getInt("txt_font");
                    Log.e("15555",txt_font+"");


                    String test2 =jsonObject.getString("txt_y");
                    Log.e("있냐?0",test2+"");



                    Log.e("받아오긴하냐고3 : ", "r우후훙"+jsonObject.getString("txt_x")+ "~~~~~~~~~~~~~~~~~~~~~~");
//                    txt_x = Float.parseFloat(jsonObject.getString("txt_x"));
//                    txt_y = Float.parseFloat(jsonObject.getString("txt_y"));
//                    txt_x = jsonObject.getDouble("txt_x");
//                    txt_y = jsonObject.getDouble("txt_y");
//                    Log.d("받아오긴하냐고3 좌표 : ", "x"+txt_x+"y"+txt_y+ "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");

                    Log.e("받아오긴하냐고3 : ", "rd"+txtShadow_rd +"x"+txtShadow_x+"y"+txtShadow_y+"color"+txtShadow_color+ "~~~~~~~~~~~~~~~~~~~~~~");

                    MyCardItem item = new MyCardItem(cardImg, cardContent, cardTitle, txtColor, txtGravity, txtSize,
                            txtShadow_rd, txtShadow_x, txtShadow_y, txtShadow_color, txt_x, txt_y,txt_font);
                   //MyCardItem item = new MyCardItem(cardImg, cardContent, cardTitle, txtColor, txtGravity, txtSize, txtShadow_rd, txtShadow_x, txtShadow_y, txtShadow_color);

                    //items.add(item);

                    adapter.addItem(item);
                    adapter.notifyDataSetChanged();
                    Log.e("받아오긴하냐고2 : ", json + i + "~~~~~~~~~~~~~~~~~~~~~~");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


    }


//    private void LoadPreferences() {
//
//        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
//        cardImg = pref.getString("profilePic","");
//        cardContent = pref.getString("content","");
//        cardTitle = pref.getString("title","");
//        txtColor = pref.getInt("txtColor",Color.WHITE);
//        txtGravity = pref.getInt("txtGravity", Gravity.CENTER_HORIZONTAL);
//        txtSize = pref.getFloat("txtSize",(float)20);
//
//        txtShadow_rd = pref.getFloat("txtShadow_rd",0);
//        txtShadow_x = pref.getFloat("txtShadow_x",0);
//        txtShadow_y = pref.getFloat("txtShadow_y",0);
//        txtShadow_color = pref.getInt("txtShadow_color",Color.BLACK);
//
//
//
//
//        MyCardItem item = new MyCardItem(cardImg,cardContent,cardTitle, txtColor, txtGravity, txtSize, txtShadow_rd, txtShadow_x, txtShadow_y, txtShadow_color);
//        items.add(item);
//        adapter.t-item);
//        adapter.notifyDataSetChanged();
//
//        //dapter.addItem(new MyCardItem(imgSet,contentSet,titleSet));
//
//
//
//    }

//    Handler handler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            updateThread();
//        }
//    };

//    private void updateThread() {
//
//        int mod = i % 4;
//
//        switch (mod) {
//            case 0:
//                i++;
//                imageView.setImageResource(R.drawable.images01);
//                break;
//            case 1:
//                i++;
//                imageView.setImageResource(R.drawable.images02);
//                break;
//            case 2:
//                i++;
//                imageView.setImageResource(R.drawable.images03);
//                break;
//            case 3:
//                i = 0;
//                imageView.setImageResource(R.drawable.images04);
//                break;
//        }
//
//    }



//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        items.clear();
//        getJasonPref();
//    }
}

