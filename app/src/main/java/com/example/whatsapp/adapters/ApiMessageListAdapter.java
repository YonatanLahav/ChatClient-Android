package com.example.whatsapp.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.entities.ApiMessage;

import java.util.List;

public class ApiMessageListAdapter extends RecyclerView.Adapter<ApiMessageListAdapter.MessagesViewHolder> {
    private final LayoutInflater _layoutInflater;
    private List<ApiMessage> _messages;
    private String _contactName;


    public void setMessages(List<ApiMessage> messages) {
        this._messages = messages;
        notifyDataSetChanged();
    }

    public ApiMessageListAdapter(Context context, String contactName) {
        _layoutInflater = LayoutInflater.from(context);
        _contactName = contactName;
    }

    @NonNull
    @Override
    public ApiMessageListAdapter.MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = _layoutInflater.inflate(R.layout.message_list_item, parent, false);
        return new ApiMessageListAdapter.MessagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        if (_messages != null) {
            final ApiMessage message = _messages.get(position);
            if (message.is_sent()) {
                holder.layout.setBackgroundResource(R.drawable.sent_message);
                holder.tvName.setText("Me:");
            } else {
                holder.layout.setBackgroundResource(R.drawable.recived_message);
                holder.tvName.setText(_contactName + ":");
            }
            holder.tvTime.setText(message.get_created());
            holder.tvContent.setText(message.get_content());
        }
    }

    @Override
    public int getItemCount() {
        if (_messages != null)
            return _messages.size();
        return 0;
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder {
        private final ConstraintLayout layout;
        private final TextView tvName;
        private final TextView tvTime;
        private final TextView tvContent;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.msg_layout);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}
