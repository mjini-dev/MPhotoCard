package com.example.mphotocard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Edit_Text_edt extends AppCompatActivity
        implements View.OnClickListener,
        ColorPickerDialog.OnColorChangedListener {

    InputMethodManager imm;  //키보드 내리기

    private int pos;

    private Button btn_input_txt;
    private Button btn_cancel1;
    private EditText txt_edit;
    private TextView txt_making;
    private ImageView ex_img;

    //폰트설정
    private Button btn_font;
    private Spinner txt_spinner;
    private String fontPath;

    //폰트정렬
    private Button btn_txt_left;
    private Button btn_txt_center;
    private Button btn_txt_right;

    //내용색상
    private Button btn_txt_color;
    int color_edt;

    //드래그드랍
    private float oldXvalue;
    private float oldYvalue;

    //폰트 크기
    private SeekBar txt_size_seekBar;
    private TextView tv_percent;

    //글씨 테두리

    private Switch sw_shadow;
    private LinearLayout li_shdw;
    private Button btn_shd_color;



    //private String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text_edt);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        txt_edit = findViewById(R.id.txt_edit_edt);
        ex_img = findViewById(R.id.ex_img_edt);
        txt_making = findViewById(R.id.txt_making_edt);

        btn_input_txt = findViewById(R.id.btn_input_txt_edt);
        btn_cancel1 = findViewById(R.id.btn_cancel1_edt);

        //btn_font = findViewById(R.id.btn_font);
        txt_spinner = findViewById(R.id.txt_spinner_edt);

        btn_txt_left = findViewById(R.id.btn_txt_left_edt);
        btn_txt_center = findViewById(R.id.btn_txt_center_edt);
        btn_txt_right = findViewById(R.id.btn_txt_right_edt);

        btn_txt_color = findViewById(R.id.btn_txt_color_edt);

        txt_size_seekBar = findViewById(R.id.txt_size_seekBar_edt);
        tv_percent = findViewById(R.id.tv_percent_edt);


        sw_shadow = findViewById(R.id.sw_shadow_edt);
        li_shdw = findViewById(R.id.li_shdw_edt);
        btn_shd_color = findViewById(R.id.btn_shd_color_edt);




        ex_img.setOnClickListener(myClickListener);
        btn_input_txt.setOnClickListener(myClickListener);
        btn_cancel1.setOnClickListener(myClickListener);

        //메이킹액티비티에서 인텐트에 담아온 값 꺼내오기
        //나를 호출한 인텐트 얻어오기
        Intent intent = getIntent();

        pos = intent.getIntExtra("pos",0);

        //인텐트에 담겨있는 값 꺼내오기_메이킹에서넘어온값
        String key = intent.getStringExtra("key");
        int txtColor = intent.getIntExtra("txtColor", Color.BLACK);
        int txtGravity = intent.getIntExtra("txtGravity", Gravity.CENTER_HORIZONTAL);
        float txtSize = intent.getFloatExtra("txtSize", (float) 20);
        String ex_img_uri = intent.getStringExtra("ex_img");


        //글씨 그림자 값
        float txtShadow_rd = intent.getFloatExtra("txtShadow_rd", 0);
        float txtShadow_x = intent.getFloatExtra("txtShadow_x", 0);
        float txtShadow_y = intent.getFloatExtra("txtShadow_y", 0);
        int txtShadow_color = intent.getIntExtra("txtShadow_color", 0);


        //에디트텍스트뷰에 문자열,색상값 넣기
        txt_edit.setText(key);
        txt_edit.setTextColor(txtColor);
        btn_txt_color.setBackgroundColor(txtColor);
        txt_edit.setGravity(txtGravity);
        //주의!! 사용자 입력값 또는 넘겨받은 값을 그대로 받으려면 픽셀 Unit을 설정해줘야함
        txt_edit.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);

        //넘겨받은 그림자 값 설정
        txt_edit.setShadowLayer(txtShadow_rd, txtShadow_x, txtShadow_y, txtShadow_color);


        float X = intent.getFloatExtra("x",0);
        float Y = intent.getFloatExtra("y",0);
        //txt_edit.setX(ex_img.getWidth()-txt_edit.getWidth());
        Log.d("좌표값받","x"+X+"y"+Y);

        int parentWidth = ((ViewGroup)ex_img.getParent()).getWidth();    // 부모 View 의 Width
        int parentHeight = ((ViewGroup)ex_img.getParent()).getHeight();    // 부모 View 의 Height

//        if(X < 0){
//            txt_edit.setX(0);
//        }else if((X + txt_edit.getWidth()) >parentWidth){
//            txt_edit.setX(parentWidth-X);
//        }
//
//        if(Y < 0){
//            txt_edit.setY(0);
//        }else if((Y + txt_edit.getHeight()) > parentHeight){
//            txt_edit.setY(parentHeight-Y);
//        }

//
//        txt_edit.setX(X-279);
//        txt_edit.setY(Y-599);

        ex_img.setImageURI(Uri.parse(ex_img_uri));



        SharedPreferences j_pref = getSharedPreferences("j_pref", MODE_PRIVATE);
        String json = j_pref.getString("list", "");
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = jsonArray.getJSONObject(pos);
            int txt_font = jsonObject.getInt("txt_font");
            switch (txt_font) {
                case 0:
                    txt_edit.setTypeface(Typeface.DEFAULT);
                    txt_spinner.setSelection(0);
                    break;

                case 1:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));
                    txt_spinner.setSelection(1);
                    break;

                case 2:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));
                    txt_spinner.setSelection(2);
                    break;

                case 3:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/ss_flower_road.ttf"));
                    txt_spinner.setSelection(3);
                    break;

                case 4:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/seoul_hangang_b.ttf"));
                    txt_spinner.setSelection(4);
                    break;

                case 5:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));
                    txt_spinner.setSelection(5);
                    break;

                case 6:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/bmyeonsung.ttf"));
                    txt_spinner.setSelection(6);

                    break;

                case 7:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jalnan.ttf"));
                    txt_spinner.setSelection(7);
                    break;

                case 8:
                    txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jeongum.ttf"));
                    txt_spinner.setSelection(8);

                    break;

                case 9:
                    txt_edit.setTypeface(Typeface.createFromAsset(Edit_Text_edt.this.getAssets(), "myfont/jejuhallasan.ttf"));
                    txt_spinner.setSelection(9);
                    break;

                case 10:
                    txt_edit.setTypeface(Typeface.createFromAsset(Edit_Text_edt.this.getAssets(), "myfont/tvn_bold.ttf"));
                    txt_spinner.setSelection(10);

                    break;



            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        //커서 끝에 위치시키기
        txt_edit.setSelection(txt_edit.length());


        //그림자 스위치 설정 저장
        SharedPreferences shadowPref = getSharedPreferences("shadow", MODE_PRIVATE);
        SharedPreferences.Editor editor = shadowPref.edit();

        if (txtShadow_rd != 0 && txtShadow_x != 0 && txtShadow_y != 0 && txtColor != 0) {

            sw_shadow.setChecked(shadowPref.getBoolean("shadow_SW", false)); //false는 기본값

        } else {
            sw_shadow.setChecked(false);
        }


////=============텍스트뷰 드래그드랍==================
//
//        txt_edit.setOnTouchListener(this);




//=============폰트설정 다이얼로그==================

       txt_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

               //Toast.makeText(Edit_Text.this,"선택된 아이템 : "+txt_spinner.getItemAtPosition(position),Toast.LENGTH_SHORT).show();

               String str_font = txt_spinner.getSelectedItem().toString();
//
//               Typeface typeface1 = ResourcesCompat.getFont(getApplicationContext(),R.font.batang);
//               Typeface typeface2 = ResourcesCompat.getFont(getApplicationContext(),R.font.utoimage_son_guelssi);
//               Typeface typeface3 = ResourcesCompat.getFont(getApplicationContext(),R.font.ss_flower_road);
//               Typeface typeface4 = ResourcesCompat.getFont(getApplicationContext(),R.font.seoul_hangang_b);
//
//
//               //Typeface typeface = Typeface.createFromFile(fontPath);
//
//               String fontPath0 = getURLForResource(R.font.batang); //android.resource://com.example.mphotocard/2131296256
//               String fontPath1 = getURLForResource(R.font.utoimage_son_guelssi); //android.resource://com.example.mphotocard/2131296266
//               String fontPath2 = getURLForResource(R.font.ss_flower_road); //android.resource://com.example.mphotocard/2131296263
//               String fontPath3 = getURLForResource(R.font.seoul_hangang_b); //android.resource://com.example.mphotocard/2131296262
//               String fontPath4 = getURLForResource(R.font.tlab_shin_young_bok); //android.resource://com.example.mphotocard/2131296264
//               String fontPath5 = getURLForResource(R.font.bmyeonsung); //android.resource://com.example.mphotocard/2131296257
//               String fontPath6 = getURLForResource(R.font.jalnan); //android.resource://com.example.mphotocard/2131296258
//               String fontPath7 = getURLForResource(R.font.jeongum); //android.resource://com.example.mphotocard/2131296260
//               String fontPath8 = getURLForResource(R.font.jejuhallasan); //android.resource://com.example.mphotocard/2131296259
//               String fontPath9 = getURLForResource(R.font.tvn_bold); //android.resource://com.example.mphotocard/2131296265
//               Log.d("fontPath0","폰트경로는 바로~!~"+fontPath0);
//               Log.d("fontPath1","폰트경로는 바로~!~"+fontPath1);
//               Log.d("fontPath2","폰트경로는 바로~!~"+fontPath2);
//               Log.d("fontPath3","폰트경로는 바로~!~"+fontPath3);
//               Log.d("fontPath4","폰트경로는 바로~!~"+fontPath4);
//               Log.d("fontPath5","폰트경로는 바로~!~"+fontPath5);
//               Log.d("fontPath6","폰트경로는 바로~!~"+fontPath6);
//               Log.d("fontPath7","폰트경로는 바로~!~"+fontPath7);
//               Log.d("fontPath8","폰트경로는 바로~!~"+fontPath8);
//               Log.d("fontPath9","폰트경로는 바로~!~"+fontPath9);
//
//
//               fontPath = txt_edit.getFontFeatureSettings();
//
//               // Typeface typeface = Typeface.createFromFile(fontPath);
//
////               //시스템에 저장되어있는 폰트확인
////               String fontPath = "/system/fonts";
////               File fontFiles = new File(fontPath);
////               File fontFileArray[] = fontFiles.listFiles();
////               String fontPathString = "";
////               for(File index : fontFileArray)
////               {
////                   fontPathString += index.toString();
////                   fontPathString += "\n";
////               }
//               Log.d("font", fontPath+"으아아아아아아");
//
//               if (str_font.equals("기본")) {
//                   txt_edit.setTypeface(Typeface.DEFAULT);
//               } else if (str_font.equals("김포평화 바탕")) {
//                   txt_edit.setTypeface(typeface1);
//               } else if (str_font.equals("비비트리 손글씨")) {
//                   txt_edit.setTypeface(typeface2);
//               } else if (str_font.equals("상상토끼 꽃길")) {
//                   txt_edit.setTypeface(typeface3);
//               } else if (str_font.equals("서울한강")) {
//                   txt_edit.setTypeface(typeface4);
//               }
//
               if (str_font.equals("기본")) {
                   txt_edit.setTypeface(Typeface.DEFAULT);
               } else if (str_font.equals("김포평화 바탕")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/batang.ttf"));
               } else if (str_font.equals("비비트리 손글씨")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));
               } else if (str_font.equals("상상토끼 꽃길")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/ss_flower_road.ttf"));
               } else if (str_font.equals("서울한강")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/seoul_hangang_b.ttf"));
               } else if (str_font.equals("신영복")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));
               } else if (str_font.equals("연성")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/bmyeonsung.ttf"));
               } else if (str_font.equals("잘난")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jalnan.ttf"));
               } else if (str_font.equals("정음")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jeongum.ttf"));
               } else if (str_font.equals("제주한라산")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/jejuhallasan.ttf"));
               } else if (str_font.equals("티비엔")) {
                   txt_edit.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), "myfont/tvn_bold.ttf"));
               }


               //               switch (position) {
//                   case 0:
//                       txt_edit.setTypeface(getResources().getFont(R.font.ss_flower_road));
//                       break;
//               }

           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });








//=============폰트정렬==================
        btn_txt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_edit.setGravity(Gravity.LEFT);

            }
        });

        btn_txt_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_edit.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        });

        btn_txt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_edit.setGravity(Gravity.RIGHT);
            }
        });


//============폰트 색상===============
        btn_txt_color.setOnClickListener(this);


//==============그림자 스위치===============
        sw_shadow.setOnCheckedChangeListener(new shadowSwitchListener());

        //그림자 색상
        btn_shd_color.setOnClickListener(this);

//============폰트 크기===============
        float size = txtSize;  //초기 글자크기 txtSize 는 픽셀단위...? , getTextSize()는 크기를 px단위로 반환..

        float spSize = txtSize / getResources().getDisplayMetrics().scaledDensity; //픽셀을 sp로 변환

        String sizeS = Integer.toString((int) spSize); //sp값을 String으로 형변환

        final int minVal = 0;
        txt_size_seekBar.setMax(28);  //최대크기
        //txt_size_seekBar.setMin(10);
        txt_size_seekBar.setProgress((int) spSize);
        txt_edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        tv_percent.setText(sizeS);

        txt_size_seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (progress >= (float) (minVal + 10)) {
                    seekBar.setProgress(progress);
                    txt_edit.setTextSize(progress);
                    tv_percent.setText(progress + "");
                } else {
                    seekBar.setProgress(minVal);
                    txt_edit.setTextSize((float) (minVal + 10));
                    tv_percent.setText((minVal + 10) + "");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });




//==============적용버튼!!!===============

        btn_input_txt.setOnClickListener(new View.OnClickListener() {
           //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                //수정입력한 내용 얻어오기
                String data = txt_edit.getText().toString();
                int txtColor = txt_edit.getCurrentTextColor();
                int txtGravity = txt_edit.getGravity();
                float txtSize = txt_edit.getTextSize();

                float txtShadow_rd = txt_edit.getShadowRadius();
                float txtShadow_x = txt_edit.getShadowDx();
                float txtShadow_y = txt_edit.getShadowDy();
                int txtShadow_color = txt_edit.getShadowColor();
                Log.d("그림자컬러","c"+txtShadow_color);

                int txtFont = txt_edit.getTypeface().getStyle();
                //int txtFont = txt_edit.getTypeface().
                //Typeface txtFont = txt_edit.getTypeface();
                //String txtFont = txt_edit.getTypeface().toString();
                Log.d("폰트폰트","오호호호"+txtFont);


                //수정된 내용을 담기위한 인텐트 생성
                Intent intent = new Intent();

                //수정된 정보 담기
                intent.putExtra("edtKey", data);
                intent.putExtra("txtColor", txtColor);
                intent.putExtra("txtGravity", txtGravity);
                intent.putExtra("txtSize", txtSize);

                intent.putExtra("txtShadow_rd", txtShadow_rd);
                intent.putExtra("txtShadow_x", txtShadow_x);
                intent.putExtra("txtShadow_y", txtShadow_y);
                intent.putExtra("txtShadow_color", txtShadow_color);

                String str_font = txt_spinner.getSelectedItem().toString();
                if (str_font.equals("기본")) {
                    intent.putExtra("txtFont", 0);

                } else if (str_font.equals("김포평화 바탕")) {
                    intent.putExtra("txtFont", 1);

                } else if (str_font.equals("비비트리 손글씨")) {
                    intent.putExtra("txtFont", 2);

                } else if (str_font.equals("상상토끼 꽃길")) {
                    intent.putExtra("txtFont", 3);

                } else if (str_font.equals("서울한강")) {
                    intent.putExtra("txtFont", 4);

                } else if (str_font.equals("신영복")) {
                    intent.putExtra("txtFont", 5);

                } else if (str_font.equals("연성")) {
                    intent.putExtra("txtFont", 6);

                } else if (str_font.equals("잘난")) {
                    intent.putExtra("txtFont", 7);

                } else if (str_font.equals("정음")) {
                    intent.putExtra("txtFont", 8);

                } else if (str_font.equals("제주한라산")) {
                    intent.putExtra("txtFont", 9);

                } else if (str_font.equals("티비엔")) {
                    intent.putExtra("txtFont", 10);
                }

//                intent.putExtra("X",txt_edit.getX()+oldXvalue-(ex_img.getWidth()/2));
//                intent.putExtra("Y",txt_edit.getY()+oldYvalue-(ex_img.getHeight()/2));

                //resultCode(성공의 의미)와 수정내용을 담은 인텐트 설정
                setResult(RESULT_OK, intent);

                //액티비티 종료(메이킹엑티비티로 이동)
                finish();


            }
        });

        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resultCode담기
                setResult(RESULT_CANCELED);

                //액티비티 종료(메이킹엑티비티로 이동)
                finish();
            }
        });

    }

    private String getURLForResource(int fontId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + fontId).toString();
    }
//=================온크리에이트 종료


    View.OnClickListener myClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideKeyboard();
            switch (v.getId()) {
                case R.id.ex_img_edt:
                    break;

                case R.id.btn_input_txt_edt:
                    break;

                case R.id.btn_cancel1_edt:
                    break;
            }
        }
    };

    private void hideKeyboard() {
        imm.hideSoftInputFromWindow(txt_edit.getWindowToken(), 0);

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_txt_color_edt :
                //color = PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.WHITE);
                color_edt = getSharedPreferences("color", MODE_PRIVATE).getInt("color", Color.WHITE);

                new ColorPickerDialog(this, this, color_edt).show();

                break;

            default:
                break;
        }

    }

    @Override
    public void colorChanged(int color) {
        //PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", color).commit();
        getSharedPreferences("color", MODE_PRIVATE).edit().putInt("color", color).apply();

        btn_txt_color.setBackgroundColor(color);
        txt_edit.setTextColor(color);
    }



    class shadowSwitchListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            SharedPreferences shadowPref = getSharedPreferences("shadow", MODE_PRIVATE);
            SharedPreferences.Editor editor = shadowPref.edit();

            if (isChecked) {
                txt_edit.setShadowLayer(5, 5, 5, Color.BLACK);
                //txt_making.setShadowLayer(5,5,5,Color.BLACK);

                //li_shdw.setVisibility(View.VISIBLE);

                editor.putBoolean("shadow_SW", isChecked);
                editor.apply();

            } else {
                txt_edit.setShadowLayer(0, 0, 0, Color.TRANSPARENT);

                li_shdw.setVisibility(View.GONE);

                editor.putBoolean("shadow_SW", false);
                editor.apply();


            }

        }
    }

}



