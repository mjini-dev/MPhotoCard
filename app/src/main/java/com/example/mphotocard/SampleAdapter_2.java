package com.example.mphotocard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

public class SampleAdapter_2 extends RecyclerView.Adapter<SampleAdapter_2.ViewHolder> implements OnSampleItemClickListener {

    Context context;

    //adapter에 들어갈 list
    ArrayList<CardItem_2> mDataList = new ArrayList<CardItem_2>();
    OnSampleItemClickListener listener;


    //외부에서 데이터를 받을 수 있도록 constructor만들기
//    public SampleAdapter_2(ArrayList<CardItem_2> list) {
//
//        this.mDataList = list;
//    }

    public SampleAdapter_2 (Context context) {

        this.context = context;
    }



    @NonNull
    @Override
    //뷰홀더객체가 만들어질때 호출
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //context = parent.getContext();

        // LayoutInflater를 이용하여 전 단계에서 만들었던 item_sample_list_3.xml을 inflate 시킴.
        // return 인자는 ViewHolder
        //인플레이터로 뷰를 얻는다
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sample_list_3, parent, false);

        ViewHolder holder = new ViewHolder(view, this);

        return holder;
    }

    @Override
    //뷰홀더객체가 재사용될때 호출
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardItem_2 item_2 = mDataList.get(position);
        Log.i("ss", String.valueOf(item_2));
        holder.setItem(item_2);

    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수
        return (null != mDataList ? mDataList.size() :0);
    }

    //===========================================================

    //소스코드에서 어뎁터에 CardItem 객체를 넣거나 가져갈수 있게 add,set,get,setItems 메소드
    public void addItem(CardItem_2 item_2) {
        mDataList.add(item_2);
    }

    public void setItems(ArrayList<CardItem_2> mDataList) {
        this.mDataList = mDataList;
    }

    public CardItem_2 getItem(int position) { //SampleData를 리턴하는 겟아이템 메소드
        //파라미터로 인덱스값(int position)을 주면 items를 리턴
        return mDataList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    //=============================================================================================


    //클릭이벤트 생성->외부에서 리스너를 설정할 수 있게 매소드 추가
    public void setOnSampleItemClickListener(OnSampleItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemClicked(ViewHolder holder, View view, int position) {
        if(listener != null) {
            listener.onItemClicked(holder, view, position);
        }
    }


    //=============================================================================================
    //뷰홀더 생성, subView를 setting
    public class ViewHolder extends RecyclerView.ViewHolder {
        //뷰홀더에 이미지와 텍스트(내가 사용하는 뷰)를 담음
       ImageView imgView;
       TextView txtView;

       //OnSampleItemClickListener listener;

        public ViewHolder(@NonNull View itemView, final OnSampleItemClickListener listener) {
            super(itemView);

            imgView = itemView.findViewById(R.id.title_img);
            txtView = itemView.findViewById(R.id.title_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (listener != null) {
                        listener.onItemClicked(ViewHolder.this, view, position);
                    }
                }
            });
        }


        public void setItem(CardItem_2 item_2) {
            //imgView.setImageResource(item_2.getResId());

//            int tempID;
//            Bitmap[] testImg = new Bitmap[10];
//            for(int i =0; i<10; i++) {
//                tempID = res.getIdentifier( "smaple"+i, "drawable" , "com.example.mphotocard");
//                testImg[i] = BitmapFactory.decodeResource(res, tempID);
//            }

            String picturePath = item_2.getResId();

            if(picturePath != null && !picturePath.equals("")) {


                imgView.setImageURI(Uri.parse(picturePath));
                //imgView.setImageURI(Uri.parse("android.resource://com.example.mphotocard/drawable//"+ picturePath));
                //imgView.setImageURI(Uri.parse("drawable://" + picturePath));
                //imgView.setImageURI(Uri.parse("file://" + picturePath));

                //imgView.setImageDrawable(ActivityCompat.getDrawable(context,R.drawable.dog));
                //imgView.setImageDrawable(android.graphics.drawable.Drawable.);



            } else {
                imgView.setImageResource(R.drawable.ic_launcher_background);
            }

            txtView.setText(item_2.getContent());
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_PX, item_2.getTxtSize());
            txtView.setTextColor(item_2.getTxtColor());
            txtView.setShadowLayer(item_2.getTxtShadow_rd(),item_2.getTxtShadow_x(),item_2.getTxtShadow_y(),item_2.getTxtShadow_color());
        }
    }
}
