package com.android.anti1991.trabajaryvivirenlayuma;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Anti1991 on 8/28/2017.
 * Displays the data
 * on click goes in here
 *
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private TextView simpleTextView;

    /**
     * The ViewHolder that will be used to display the data in each item shown
     * in the RecyclerView
     *
     * @param itemView
     *         The layout view group used to display the data
     */
    public ViewHolder(final View itemView) {
        super(itemView);
        simpleTextView = (TextView) itemView.findViewById(R.id.item_category_title);
    }

    /**
     * Method that is used to bind the data to the ViewHolder
     *
     * @param viewModel
     *         The viewmodel that contains the data
     */
    public void bindData(final ViewModel viewModel) {
        simpleTextView.setText(viewModel.getSimpleText());
    }
}