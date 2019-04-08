package com.theeralabs.app.contentprovider.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theeralabs.app.contentprovider.R;
import com.theeralabs.app.contentprovider.model.Messages;

import java.util.List;

/**
 * Created by Kuldeep on 16-Nov-17.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private List<Messages> mMessagesList;
    private Context mContext;

    public MessagesAdapter(List<Messages> messages, Context context) {
        mMessagesList = messages;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.message_item_view, parent, false);
        return new MessagesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtFrom.setText(mMessagesList.get(position).getFrom());
        holder.txtBody.setText(mMessagesList.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFrom, txtBody;
        public ViewHolder(View itemView) {
            super(itemView);
            txtFrom = itemView.findViewById(R.id.txt_from);
            txtBody = itemView.findViewById(R.id.txt_body);
        }
    }
}
