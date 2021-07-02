package com.example.fud.MainActivityFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fud.ChatArrayAdapter;
import com.example.fud.Models.ChatModel;
import com.example.fud.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.example.fud.AppActivities.MainActivity.USR_KEY;


public class ChatFragment extends Fragment {
    /**
     * String to hold the username
     */
    private String username;
    /**
     * Button for connecting to the chatroom
     */
    private Button connectButton;
    /**
     * ImageButton for the send message icon
     */
    private ImageButton sendMessageButton;
    /**
     * EditText for the message the user is sending
     */
    private EditText messageText;
    /**
     * TextView for new user's that join the class
     */
    private TextView userJoin;
    /**
     * The context of this fragment
     */
    private Context mContext;
    /**
     * Instance of the ChatArrayAdapter
     */
    private ChatArrayAdapter chatArrayAdapter;
    /**
     * RecyclerView for the chat
     */
    private RecyclerView recyclerView;
    /**
     * the List of all chat messages
     */
    private List<ChatModel> chatList;
    /**
     * Boolean for the type of message sender=true received=false
     */
    private boolean sent = false;
    /**
     * Instance of the WebSocket
     */
    private WebSocketClient cc;
    /**
     * Shared Preferences used to save the user's session key
     */
    private SharedPreferences mSharedPreferences;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSharedPreferences = context.getSharedPreferences(getString(R.string.package_name), Context.MODE_PRIVATE);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mContext = container.getContext();
        username = mSharedPreferences.getString(USR_KEY, null);
        chatList = new ArrayList<ChatModel>();

        connectButton = (Button) view.findViewById(R.id.connect_button);
        sendMessageButton = (ImageButton) view.findViewById(R.id.send_message_button);
        messageText = (EditText) view.findViewById(R.id.message_text);
        userJoin = (TextView) view.findViewById(R.id.user_joined);
        userJoin.setVisibility(View.INVISIBLE);

        recyclerView = (RecyclerView) view.findViewById(R.id.message_list);
        chatArrayAdapter = new ChatArrayAdapter(mContext, chatList);
        recyclerView.setAdapter(chatArrayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Draft[] drafts = {new Draft_6455()};
                String url = "ws://coms-309-vb-1.cs.iastate.edu:8080/websocket/" + username;
                //This URL is for testing purposes only
                //String url = "wss://echo.websocket.org";

                try {
                    cc = new WebSocketClient(new URI(url), (Draft) drafts[0]) {
                        @Override
                        public void onMessage(String message) {
                            Log.d("Message", message);
                            String[] messageArr = message.split(":",2);
                            if (messageArr[0].equals("User")) {
                                userJoin.setText(messageArr[1]);
                                userJoin.setVisibility(View.VISIBLE);
                            }
                            else {
                                if (messageArr[0].equals(username)) {
                                    sent = true;
                                } else {
                                    sent = false;
                                }
                                ChatModel cm = new ChatModel(messageArr[0], messageArr[1], sent);
                                chatArrayAdapter.add(cm);
                                messageText.setText("");
                            }
                        }

                        @Override
                        public void onOpen(ServerHandshake handshake) {
                            Log.d("OPEN", "run() returned: " + "is connecting");
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Log.d("CLOSE", "onClose() returned: " + reason);
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d("Exception:", e.toString());
                        }
                    };
                } catch (URISyntaxException e) {
                    Log.d("Exception:", e.getMessage());
                    e.printStackTrace();
                }
                cc.connect();
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cc.send(messageText.getText().toString());
                    messageText.setText("");
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage());
                }
            }
        });

        return view;
    }
}
