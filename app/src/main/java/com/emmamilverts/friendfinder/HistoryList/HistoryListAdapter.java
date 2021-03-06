package com.emmamilverts.friendfinder.HistoryList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emmamilverts.friendfinder.DTO.HistoryDTO;
import com.emmamilverts.friendfinder.GoogleMapsEncode;
import com.emmamilverts.friendfinder.R;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class HistoryListAdapter extends RecyclerView.Adapter {
    private List<HistoryDTO> historyList;
    private Context context;
    public HistoryListAdapter(List<HistoryDTO> historyList, Context context){
        this.historyList = historyList;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historylist_item,parent,false);
        return new ListViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewholder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
    private class ListViewholder extends RecyclerView.ViewHolder {
        public TextView userName_TextView;
        public TextView location_TextView;
        public TextView updateTime_TextView;


        public ListViewholder(@NonNull View itemView) {
            super(itemView);
            userName_TextView = itemView.findViewById(R.id.userName_textView);
            location_TextView = itemView.findViewById(R.id.location_TextView);
            updateTime_TextView = itemView.findViewById(R.id.updateTime_TextView);

            itemView.setOnClickListener(v -> {
                HistoryDTO clickedItem = historyList.get(getAdapterPosition());
                String encodedSearchString = GoogleMapsEncode.encodeString(clickedItem.coordinates);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.Google_Maps_URL) + encodedSearchString));
                context.startActivity(browserIntent);
            });
        }

        public void bindView(int position){
            userName_TextView.setText(historyList.get(position).username);
            location_TextView.setText(historyList.get(position).coordinates);
            long timeInMillisecond = historyList.get(position).getTime();
            if (TimeUnit.MILLISECONDS.toMinutes(timeInMillisecond) > 60){
                updateTime_TextView.setText(String.format("%s %s", String.valueOf(TimeUnit.MILLISECONDS.toHours(timeInMillisecond)), context.getString(R.string.hours_ago)));
            }
            else {
                updateTime_TextView.setText(String.format("%s %s", String.valueOf(TimeUnit.MILLISECONDS.toMinutes(timeInMillisecond)), context.getString(R.string.minutes_ago)));
            }
        }
    }
}
