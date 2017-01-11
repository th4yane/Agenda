package br.com.thayane.agenda;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.List;

/**
 * Created by Thayane on 13/12/2016.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter{

    List<Task> list = Collections.emptyList();
    Context context;
    DatabaseController dc;
    String date;

    public RecyclerViewAdapter(List<Task> list, Context context, DatabaseController dc, String date) {
        this.list = list;
        this.context = context;
        this.dc = dc;
        this.date = date;
    }

    public void setList(List<Task> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolderAdapter holder = new ViewHolderAdapter(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBindViewHolder2((ViewHolderAdapter)holder,position);
    }

    public void onBindViewHolder2(ViewHolderAdapter holder, int position) {
        holder.title.setText(list.get(position).title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onItemDismiss(int position) {
        list.remove(position);
        notifyItemRemoved(position);

        if(list.isEmpty()){
            dc.deleteData(date);
        } else{
            Gson gson = new Gson();
            String inputString = gson.toJson(list);
            dc.alterData(date, inputString);
        }

    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

}
