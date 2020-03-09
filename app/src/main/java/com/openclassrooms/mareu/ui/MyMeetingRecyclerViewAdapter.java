package com.openclassrooms.mareu.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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

public class MyMeetingRecyclerViewAdapter extends RecyclerView.Adapter<MyMeetingRecyclerViewAdapter.ViewHolder> implements Filterable {

    private OnMeetingListener mOnMeetingListener;
    private Context mContext;

    private List<Meeting> mMeetings;
    private String mFilterItem;
    private List<Meeting> mList;

    MyMeetingRecyclerViewAdapter(Context context, List<Meeting> meetings, OnMeetingListener onMeetingListener) {
        mList = new ArrayList<>(meetings);
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

    void getFilterItem(String item) { mFilterItem = item; }

    @Override
    public Filter getFilter() { return mMeetingsFilter; }

    private Filter mMeetingsFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Meeting> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) { filteredList.addAll(mList); }
            else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Meeting m : mList) {
                    String id = mFilterItem;

                    if(id.equals("days") && m.getDate().toLowerCase().contains(filterPattern)) {filteredList.add(m);}
                    if(id.equals("rooms") && m.getRoom().toLowerCase().contains(filterPattern)) {filteredList.add(m);}
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mMeetings.clear();
            mMeetings.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
