package ru.production.ssobolevsky.servicetest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {


    public static final int WHAT = 1001;

    public static final int REGISTER = 1002;

    public static final int UNREGISTER = 1003;

    public static final int SET = 1004;

    private final static int MODE = Service.START_REDELIVER_INTENT;

    private List<Messenger> mClients = new ArrayList<>();

    private Messenger mMessenger = new Messenger(new IncomingHandler());


    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = Message.obtain(null, WHAT);
                    message.obj = Math.random() * 100;
                    try {
                        for (Messenger messenger : mClients) {
                            messenger.send(message);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return MODE;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mMessenger.getBinder();
    }

    public static final Intent newIntent(Context context) {
        Intent intent = new Intent(context, MyService.class);
        return intent;
    }


    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REGISTER :
                    mClients.add(msg.replyTo);
                    break;
                case UNREGISTER :
                    mClients.remove(msg.replyTo);
                    break;
                case SET :
                    break;
            }
        }
    }
}
