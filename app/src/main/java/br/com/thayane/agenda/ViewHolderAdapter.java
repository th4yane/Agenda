package br.com.thayane.agenda;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Thayane on 13/12/2016.
 */

public class ViewHolderAdapter extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;

    ViewHolderAdapter(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.title);
    }
}

