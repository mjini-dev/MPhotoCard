package com.example.mphotocard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


//샘플어댑터 안에 부홀더를 정의하려고한다- 뷰홀더는 각각의 아이템을 위한 뷰를 담아두는 용도로 사용한다.
public class SampleAdapter extends RecyclerView.Adapter<SampleAdapter.ViewHolder> {

//    private ArrayList<SampleData> arrayList;
////    public SampleAdapter(ArrayList<SampleData> arrayList) {
////        this.arrayList = arrayList;
////    }

    Context context;

    ArrayList<SampleData> items = new ArrayList<SampleData>();

    OnItemClickListener listener;



    public static interface OnItemClickListener { //OnItemClickListener를 하나의 인터페이스로 직접 정의
        public void onItemclick(ViewHolder holder, View view, int position);
    }


    public SampleAdapter(Context context) {
        this.context = context;
    }


    @NonNull

    @Override  //아이템 개수
    public int getItemCount() {
        return items.size();
    }


    @Override //(각각의 아이템을 위한)뷰 홀더가 만들어지는 시점에 호출되는 메소드
    //뷰 홀더가 재사용 될 수 있는 상태라면 호출되지 않음
    public SampleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //레이아웃 인플레이터 참조
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_sample_list,parent,false);
        //parent : 각각의 아이템을 위해 정의한 xml레이아웃의 최상위 레이아웃임

        //각각의 뷰를 담고있는 뷰홀더 객체를 만듬
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override //데이터와 뷰홀더...각각의 아이템을 위한 뷰의 xml레이아웃과 서로 뭉쳐지는(결합되는)경우 on(호출)된다
    public void onBindViewHolder(@NonNull SampleAdapter.ViewHolder holder, int position) {
        SampleData item = items.get(position); //리싸이클러뷰에서 몇번째 게 지금 보여야되는 시점에 샘플데이터 item에 저장
        holder.setItem(item);

        holder.setOnItemClickListener(listener);

    }

    //어댑터에 아이템추가
    public void addItem(SampleData item) {
        items.add(item); //파라미터로 전달받은 item객체를 추가
    }

    //ArrayList라고하는 items를 그대로 설정하는 경우 (당장쓰진않지만 미리 만들어놓음..)
    public void addItems(ArrayList<SampleData> items) {
        this.items = items;
    }

    public SampleData getItem(int position) { //SampleData를 리턴하는 겟아이템 메소드
        //파라미터로 인덱스값(int position)을 주면 items를 리턴
        return items.get(position);
    }


    //클릭이벤트 생성
    public void setOnItemClickListener(OnItemClickListener listener) {

        this.listener = listener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{  //뷰홀더는 각각의 아이템을위한 뷰를 담고있을수있다

        protected ImageView temp_1;
        protected TextView tv_temp1;
        protected ImageView temp_2;
        protected TextView tv_temp2;

        OnItemClickListener listener;

        //alt+enter : create matching super
        public ViewHolder(@NonNull View itemView) {  //뷰(뷰홀더에 담긴 각각의 아이템의 뷰)를 파라미터로 전달받음
            //뷰와 실제 데이터를 매칭(데이터 설정)
            super(itemView);

            this.temp_1 = (ImageView) itemView.findViewById(R.id.temp_1);
            this.tv_temp1 = (TextView) itemView.findViewById(R.id.tv_temp1);
            this.temp_2 = (ImageView) itemView.findViewById(R.id.temp_2);
            this.tv_temp2 = (TextView) itemView.findViewById(R.id.tv_temp2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition(); //몇번째 뷰인지 인덱스값을 getAdapterPosition()메서드를 통해 확인

                    if (listener != null) {
                        listener.onItemclick(ViewHolder.this, view, position);
                    }
                }
            });
        }

        //데이터 설정
        public void setItem(SampleData item) {
            temp_1.setImageResource(item.getTemp_1());
            tv_temp1.setText(item.getTv_temp1());
            temp_2.setImageResource(item.getTemp_2());
            tv_temp2.setText(item.getTv_temp2());
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }


    }
}
