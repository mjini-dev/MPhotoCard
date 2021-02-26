package com.example.mphotocard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//public class MyCardListAdapter extends RecyclerView.Adapter<MyCardListAdapter.ViewHolder> implements OnMyCardItemClickListener {
public class MyCardListAdapter extends RecyclerView.Adapter<MyCardListAdapter.ViewHolder> {

    Context context;

    //adapter에 들어갈 list
//    private List<MyCardItem> items;
    ArrayList<MyCardItem> items = new ArrayList<MyCardItem>();


    public MyCardListAdapter(List<MyCardItem> items) {

        items = items;
        //this.context = context;
    }

//    public MyCardListAdapter (Context context) {
//
//       this.context = context;
//    }


//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_my_card, parent, false);
//
//        ViewHolder holder = new ViewHolder(view, this);
//
//        return holder;
//
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_card, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyCardItem item = items.get(position);
        //holder.list_title_text.setText(items.get(position).getCardTitle());
        //holder.list_title_text.setText(item.getCardTitle());
        //holder.list_content_text.setText(item.getCardContent());
        holder.setItem(item);


    }

    @Override
    public int getItemCount() {

        return (null != items ? items.size() : 0);
    }


    //===========================================================


    public void addItem(MyCardItem item) {

        items.add(item);
    }


    public void setItems(ArrayList<MyCardItem> items) {

        this.items = items;
    }

    public MyCardItem getItem(int position) {
        //파라미터로 인덱스값(int position)을 주면 items를 리턴
        return items.get(position);
    }

    public MyCardItem setItem(int position, MyCardItem item) {

        return items.set(position, item);
    }


    //===========================================================

    public interface OnItemClickListener {
        void onItemClick(View v, int pos);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int pos);


    }

    private OnItemClickListener listener = null;
    private OnItemLongClickListener mLongListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = listener;

    }


    public void delete(int pos) {

        try {

            items.remove(pos);

            notifyItemRemoved(pos);

        } catch (IndexOutOfBoundsException ex) {

            ex.printStackTrace();

        }

    }


    //


//    OnMyCardItemClickListener listener;
//
//    public void setOnMyCardItemClickListener(OnMyCardItemClickListener listener) {
//        this.listener = listener;
//
//    }
//
//    @Override
//    public void onMyCardItemClick(ViewHolder holder, View view, int position) {
//        if(listener != null) {
//            listener.onMyCardItemClick(holder, view, position);
//        }
//    }
//
//    @Override
//    public void onLongClick(ViewHolder holder, View view, int position) {
//        if(listener != null) {
//            listener.onMyCardItemClick(holder, view, position);
//        }
//    }

    //=================================================================


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView list_title_img;
        TextView list_content_text;
        TextView list_title_text;


//        public ViewHolder(@NonNull View v, final OnMyCardItemClickListener listener) {
//            super(v);
//            //생성자가 생성될때 xml에 있는 위젯을 소스코드와 연결
//            list_title_img = v.findViewById(R.id.list_title_img);
//            list_content_text = v.findViewById(R.id.list_content_text);
//            list_title_text = v.findViewById(R.id.list_title_text);
//
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//
//                    if (listener != null) {
//                        listener.onMyCardItemClick(ViewHolder.this, view, position);
//                    }
//                }
//            });
//
//
//        }

        public ViewHolder(@NonNull View v) {
            super(v);
            //생성자가 생성될때 xml에 있는 위젯을 소스코드와 연결
            list_title_img = v.findViewById(R.id.list_title_img);
            list_content_text = v.findViewById(R.id.list_content_text);
            list_title_text = v.findViewById(R.id.list_title_text);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        // 데이터 리스트로부터 아이템 데이터 참조.
                        MyCardItem item = items.get(position);
                        if (listener != null) {
                            //listener.onMyCardItemClick(ViewHolder.this, view, position);
                            listener.onItemClick(view, position);


                        }


                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mLongListener.onItemLongClick(view, pos);
                        //delete(getAdapterPosition());
                    }
                    return true;
                }
            });


        }


        public void setItem(MyCardItem item) {
            //list_title_img.setImageResource(item.getCardImg());

            String picturePath = item.getCardImg();

            if (picturePath != null && !picturePath.equals("")) {
                //list_title_img.setImageURI(Uri.parse("file:///" + picturePath));
                list_title_img.setImageURI(Uri.parse(picturePath));

                Log.i(this.getClass().getName(), picturePath + "카드목록이미지경로");
            } else {
                list_title_img.setImageResource(R.drawable.ic_launcher_background);
            }


            list_title_text.setText(item.getCardTitle());

            list_content_text.setText(item.getCardContent());
            list_content_text.setTextColor(item.getTxtColor());
            list_content_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, item.getTxtSize());
            list_content_text.setShadowLayer(item.getTxtShadow_rd(), item.getTxtShadow_x(), item.getTxtShadow_y(), item.getTxtShadow_color());
            list_content_text.setGravity(item.getTxtGravity());

            list_content_text.setX(item.getTxt_x());
            list_content_text.setY(item.getTxt_y());

            switch (item.getTxtFont()) {
                case 0 :
                    list_content_text.setTypeface(Typeface.DEFAULT);
                    break;

                case 1 :
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/batang.ttf"));
                   // list_content_text.setTypeface(ResourcesCompat.getFont(context,R.font.batang));

                    break;

                case 2:
                    //list_content_text.setTypeface(Typeface.createFromAsset(MyCardListAdapter.this.context.getAssets(), "myfont/utoimage_son_guelssi.ttf"));
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/utoimage_son_guelssi.ttf"));
                    break;

                case 3:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/ss_flower_road.ttf"));
                    break;

                case 4:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/seoul_hangang_b.ttf"));
                    break;

                case 5:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/tlab_shin_young_bok.otf"));
                    break;

                case 6:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/bmyeonsung.ttf"));
                    break;

                case 7:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/jalnan.ttf"));
                    break;

                case 8:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/jeongum.ttf"));

                    break;

                case 9:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/jejuhallasan.ttf"));
                    break;

                case 10:
                    list_content_text.setTypeface(Typeface.createFromAsset(list_content_text.getContext().getAssets(), "myfont/tvn_bold.ttf"));
                    break;



            }


        }

    }


}
