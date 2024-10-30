package com.example.pract13;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {

    private List<GameStatistics> statisticsList;

    public StatisticsAdapter(List<GameStatistics> statisticsList) {
        this.statisticsList = statisticsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameStatistics statistics = statisticsList.get(position);
        holder.playerTextView.setText(statistics.getPlayer());
        holder.winsTextView.setText(String.valueOf(statistics.getWins()));
    }

    @Override
    public int getItemCount() {
        return statisticsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView playerTextView;
        TextView winsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playerTextView = itemView.findViewById(R.id.playerTextView);
            winsTextView = itemView.findViewById(R.id.winsTextView);
        }
    }
}
