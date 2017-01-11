package br.com.thayane.agenda;

/**
 * Created by Thayane on 15/12/2016.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
