package com.github.android.flux;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.android.flux.actions.ActionsCreator;
import com.github.android.flux.model.Singer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlove on 2018/2/5.
 */

public class SingerRecyclerAdapter extends RecyclerView.Adapter<SingerRecyclerAdapter.ViewHolder> {

    private static ActionsCreator actionsCreator;
    private List<Singer> singers;

    public SingerRecyclerAdapter(ActionsCreator actionsCreator) {
        this.singers = new ArrayList<>();
        this.actionsCreator = actionsCreator;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_singer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(singers.get(position));
    }

    @Override
    public int getItemCount() {
        return singers.size();
    }

    public void setItems(List<Singer> singers) {
        this.singers = singers;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvName;
        public TextView tvGender;
        private TextView tvAge;
        public Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvGender = (TextView) itemView.findViewById(R.id.gender);
            tvAge = (TextView) itemView.findViewById(R.id.age);
            btnDelete = (Button) itemView.findViewById(R.id.delete);
        }

        public void bindView(final Singer singer) {
            tvName.setText("name: " + singer.getName());
            tvGender.setText("gender: " + singer.getGender());
            tvAge.setText("age: " + singer.getAge());

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long id = singer.getId();
                    actionsCreator.delete(id);
                }
            });
        }
    }
}
