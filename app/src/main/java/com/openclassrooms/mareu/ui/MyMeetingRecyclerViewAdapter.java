package com.openclassrooms.mareu.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.model.Meeting;
import com.openclassrooms.mareu.model.RoomItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.openclassrooms.mareu.service.DummyRoomGenerator.DUMMY_ROOMS;

public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> {

    private OnMeetingListener mOnMeetingListener;
    private Context mContext;

    private List<Meeting> mMeetings;

    MyMeetingRecyclerViewAdapter(Context context, List<Meeting> meetings, OnMeetingListener onMeetingListener) {
        mContext = context;
        mMeetings = meetings;
        mOnMeetingListener = onMeetingListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new ViewHolder(view, mOnMeetingListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meeting meeting = mMeetings.get(position);

        holder.mDate.setText(meeting.getDate());

        List<RoomItem> mRoomItems = new ArrayList<>(DUMMY_ROOMS);

        for(RoomItem item : mRoomItems) {
            if(item.getName().equals(meeting.getRoom())) {
                holder.mImg.setImageTintList(mContext.getResources().getColorStateList(item.getImgColor()));
            }
        }

        holder.mTitle.setText(String.format("%s - %s - Room %s", meeting.getTopic(), meeting.getTime(), meeting.getRoom()));
        holder.mParticipants.setText(TextUtils.join(", ",meeting.getParticipants()));
    }

    @Override
    public int getItemCount() { return mMeetings.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.item_date) TextView mDate;
        @BindView(R.id.item_img) ImageView mImg;
        @BindView(R.id.item_title) TextView mTitle;
        @BindView(R.id.item_participants) TextView mParticipants;
        @BindView(R.id.item_delete_btn) ImageButton mDelete;

        OnMeetingListener onMeetingListener;

        ViewHolder(@NonNull View itemView, OnMeetingListener onMeetingListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.onMeetingListener = onMeetingListener;
            mDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMeetingListener.onDeleteMeeting(getAdapterPosition());
        }
    }

    public interface OnMeetingListener {
        void onDeleteMeeting(int position);
    }
}
