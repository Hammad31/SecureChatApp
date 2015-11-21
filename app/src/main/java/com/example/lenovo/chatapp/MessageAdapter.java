package com.example.lenovo.chatapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<String> {
    String user;
    java.security.PrivateKey myPrivateKey;

    public MessageAdapter(Context context, String mUser, ArrayList<String> messages, java.security.PrivateKey myPrivateKey) {
        super(context, 0, messages);
        user = mUser;
        this.myPrivateKey = myPrivateKey;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_row, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.header = (ImageView) convertView.findViewById(R.id.imgHeader);
            holder.message = (TextView) convertView.findViewById(R.id.tvMssage);
            holder.message.setText(getItem(position));
            holder.back = (LinearLayout) convertView.findViewById(R.id.messageBack);
            holder.message.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMsgBlue));
            holder.header.setImageResource(R.drawable.back_b);
            holder.message.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.back.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            convertView.setTag(holder);
            return convertView;
        }else{
            final ViewHolder holder = (ViewHolder)convertView.getTag();
            holder.header = (ImageView) convertView.findViewById(R.id.imgHeader);
            holder.message = (TextView) convertView.findViewById(R.id.tvMssage);
            holder.message.setText(getItem(position));
            holder.back = (LinearLayout) convertView.findViewById(R.id.messageBack);
            holder.message.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorMsgBlue));
            holder.header.setImageResource(R.drawable.back_b);
            holder.message.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.back.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            convertView.setTag(holder);
            return convertView;
        }
    }

    class ViewHolder {
        public TextView message;
        public TextView time;
        public ImageView header;
        public LinearLayout back;
    }
}
