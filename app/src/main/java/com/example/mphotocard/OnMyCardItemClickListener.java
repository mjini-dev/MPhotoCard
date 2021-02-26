package com.example.mphotocard;

import android.view.View;

public interface OnMyCardItemClickListener {

    void onMyCardItemClick(MyCardListAdapter.ViewHolder holder, View view, int position);

    void onLongClick(MyCardListAdapter.ViewHolder holder, View view, int position);



}


