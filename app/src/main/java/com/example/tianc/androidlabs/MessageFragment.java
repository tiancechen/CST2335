package com.example.tianc.androidlabs;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class MessageFragment extends Fragment {
    private TextView messageContent;
    private TextView messageID;
    private Button deleteButton;
    private Bundle runningBundle;
    private boolean runningOnPhone;
    private Context parent;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        runningBundle = this.getArguments();
        runningOnPhone = runningBundle.getBoolean("phone");
    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment,container,false);
        messageContent = result.findViewById(R.id.message_content);
        messageID = result.findViewById(R.id.message_id);
        deleteButton = result.findViewById(R.id.delete_button);
        String ID = Long.toString(runningBundle.getLong("ID"));
        messageID.setText(ID);
        String msg = runningBundle.getString("message");
        messageContent.setText(msg);
        runningDelete();
        return result;
    }

    public void runningDelete(){
        deleteButton.setOnClickListener(v->{
            if (runningOnPhone){
                Intent intent = new Intent(getActivity(),ChatWindow.class);
                intent.putExtra("delete",runningBundle.getLong("ID"));
                MessageDetails messageDetails = (MessageDetails)getActivity();
                messageDetails.setResult(-1,intent);
                messageDetails.finish();
            }else{
                ChatWindow chatWindow = (ChatWindow) getActivity();
                getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                chatWindow.deleteMessage(runningBundle.getLong("ID"));

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        runningBundle = this.getArguments();
        runningOnPhone = runningBundle.getBoolean("phone");
        parent = context;


    }
}
