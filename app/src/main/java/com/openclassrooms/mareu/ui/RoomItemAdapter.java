package com.openclassrooms.mareu.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.RoomItem;

import java.util.ArrayList;

public class RoomItemAdapter extends ArrayAdapter<RoomItem> {

    RoomItemAdapter(@NonNull Context context, @NonNull ArrayList<RoomItem> roomsList) {
        super(context, 0, roomsList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_room, parent, false);
        }

        TextView mNameRoomItem = convertView.findViewById(R.id.name_item_room);
        ImageView mImgRoomItem = convertView.findViewById(R.id.img_item_room);
        Context mContext = getContext();

        RoomItem currentRoom = getItem(position);

        if(currentRoom != null) {
            mNameRoomItem.setText(currentRoom.getName());
            mImgRoomItem.setImageTintList(mContext.getResources().getColorStateList(currentRoom.getImgColor()));
        }

        return convertView;
    }
}
