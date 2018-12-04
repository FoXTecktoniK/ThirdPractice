package com.example.rumyantsevok.thirdpractice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private Button stopServiceBtn;
    private TextView mTextView;

    private Messenger mService;

    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = new Messenger(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        init();
        setClickListeners();
        bindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unBindService();
    }

    private void init() {
        stopServiceBtn = findViewById(R.id.stopServiceBtn);
        mTextView = findViewById(R.id.secActTV);
    }

    private void setClickListeners() {
        stopServiceBtn.setOnClickListener(new StopServiceClickListener());
    }


    public static final Intent newIntent(Context context) {
        return new Intent(context, SecondActivity.class);
    }

    private class StopServiceClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            unBindService();
        }
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyIntentService.MSG_SEND_DATA:
                    setTextView(Integer.toString(msg.arg1, 5));
                    break;

                    default:
                        super.handleMessage(msg);
            }
        }
    }

    private void setTextView(String string) {
        mTextView.setText(string);
    }

    private void bindService() {
        bindService(MyIntentService.newIntent(SecondActivity.this),
                mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        Message msg = Message.obtain(null,
                MyIntentService.MSG_UNREGISTER_CLIENT);
        msg.replyTo = mMessenger;
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(mServiceConnection);
    }
}
