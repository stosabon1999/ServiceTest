package ru.production.ssobolevsky.servicetest;

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
import android.util.Log;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private Messenger mService;

    private TextView mTextView;

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = new Messenger(iBinder);
            Message message = Message.obtain(null, MyService.REGISTER);
            message.replyTo = mMessenger;
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        init();
    }

    private void init() {
        mTextView = findViewById(R.id.tv_text);
        bindService();
    }

    private void bindService() {
        bindService(MyService.newIntent(SecondActivity.this), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        Message message = Message.obtain(null,  MyService.UNREGISTER);
        message.replyTo = mMessenger;
        try {
            mService.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        unbindService(mServiceConnection);
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SecondActivity.class);
        return intent;
    }

    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyService.WHAT :
                    mTextView.setText(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindService();
    }
}
