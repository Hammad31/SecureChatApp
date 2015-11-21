package com.example.lenovo.chatapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;


public class UserAdapter extends ArrayAdapter<ParseUser> {
    public UserAdapter(Context context, ArrayList<ParseUser> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ParseUser user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_row, parent, false);
            final ViewHolder holder = new ViewHolder();
            holder.userImage = (ImageView) convertView.findViewById(R.id.UserProfileImage);
            holder.username = (TextView) convertView.findViewById(R.id.UserProfileName);
            holder.username.setText(getItem(position).getUsername());
            convertView.setTag(holder);
            holder.username.setVisibility(View.VISIBLE);
            ParseFile file = (ParseFile) user.get("image");
            if (file != null)
                try {
                    Bitmap mIcon_val2 = BitmapFactory.decodeByteArray(file.getData(), 0, file.getData().length);
                    holder.userImage.setImageBitmap(mIcon_val2);
                } catch (Exception e) {
                }
            holder.userImage.setVisibility(View.VISIBLE);

        } else {
            final ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.userImage = (ImageView) convertView.findViewById(R.id.UserProfileImage);
            holder.username = (TextView) convertView.findViewById(R.id.UserProfileName);
            holder.username.setText(getItem(position).getUsername());
            holder.username.setVisibility(View.VISIBLE);
            ParseFile file = (ParseFile) user.get("image");
            if (file != null)
                try {
                    Bitmap mIcon_val2 = BitmapFactory.decodeByteArray(file.getData(), 0, file.getData().length);
                    holder.userImage.setImageBitmap(mIcon_val2);
                } catch (Exception e) {
                }
            holder.userImage.setVisibility(View.VISIBLE);

            convertView.setTag(holder);

        }
        return convertView;
    }

    class ViewHolder {
        public ImageView userImage;
        public TextView username;
    }


}
