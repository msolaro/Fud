package com.example.fud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fud.Models.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class ChatArrayAdapter extends RecyclerView.Adapter {

    private static final int TYPE_MESSAGE_SENT = 1;
    private static final int TYPE_MESSAGE_RECEIVED = 2;
    private List<ChatModel> mChatList = new ArrayList<ChatModel>();
    private Context mContext;

    /**
     * ChatArrayAdapter constructor
     * @param context
     * @param chatList List of ChatModels in chat
     */
    public ChatArrayAdapter(Context context, List<ChatModel> chatList) {
        mContext = context;
        mChatList = chatList;
    }

    /**
     * Adds a ChatModel to the chat list
     * @param object ChatModel being added to the chat
     */
    public void add(ChatModel object) {
        mChatList.add(object);
    }

    /**
     * returns the number of messages in the chat
     * @return number of messages
     */
    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    /**
     * returns the type of message
     * @param position
     * @return Integer for the type of message (1=sent and  2=received)
     */
    @Override
    public int getItemViewType(int position) {
        ChatModel chatMessageObj = mChatList.get(position);

        if (chatMessageObj.sent) {
            // If the current user is the sender of the message
            return TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return TYPE_MESSAGE_RECEIVED;
        }
    }

    /**
     * Inflates the appropriate layout whether it is a sent or received message.
     * @param parent View group
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.their_message, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    /**
     * Passes the message object to a ViewHolder so the contents can be bound to UI.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatModel chat = (ChatModel) mChatList.get(position);

        switch (holder.getItemViewType()) {
            case TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(chat);
                break;
            case TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(chat);
        }
    }

    /**
     * class to set the fields of the sent message
     */
    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_body);
        }

        void bind(ChatModel chat) {
            messageText.setText(chat.getMessage());
        }
    }

    /**
     * class to set the fields of the recieved message
     */
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, idText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_body);
            idText = (TextView) itemView.findViewById(R.id.sender_id);
        }

        void bind(ChatModel chat) {
            messageText.setText(chat.getMessage());
            idText.setText(chat.getOwner());
        }
    }

}
