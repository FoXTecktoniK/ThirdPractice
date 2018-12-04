package com.example.rumyantsevok.thirdpractice;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MyIntentService extends IntentService {

    public static final int MSG_REGISTER_CLIENT = 0xf5f;
    public static final int MSG_UNREGISTER_CLIENT = 0xf60;
    public static final int MSG_SEND_DATA = 0xf61;
    public static final int MSG_GET_DATA = 0xf62;

    //private static final int MODE = Service.START_NOT_STICKY;

    public static final String MESSAGE_KEY = "my_intent_service_msg_key";

    private List<Messenger> observersList = new ArrayList<>();

    private Messenger mMessenger = new Messenger(new IncomingHandler());

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    observersList.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    observersList.remove(msg.replyTo);
                    break;
                case MSG_SEND_DATA:
                    break;
                case MSG_GET_DATA:
                    send_data(msg);
                    break;
            }
        }
    }

    private void send_data(Message msg) {
        Messenger messenger = msg.replyTo;
        Message message = Message.obtain(null,
                MSG_SEND_DATA,
                getValue(),
                0);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private int getValue() {
        return (int) System.currentTimeMillis();
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MyIntentService.class);
        return intent;
    }


}
