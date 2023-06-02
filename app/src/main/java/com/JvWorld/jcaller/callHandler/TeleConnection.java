package com.JvWorld.jcaller.callHandler;

import android.content.Intent;
import android.os.Build;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class TeleConnection extends ConnectionService {

    private static Connection connection;

    public TeleConnection() {
        super();
    }

    public static Connection getConnection() {
        return connection;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreateOutgoingConferenceFailed(@NonNull PhoneAccountHandle connectionManagerPhoneAccount, @NonNull ConnectionRequest request) {
//        Toast.makeText(this, String.valueOf(request)+String.valueOf(connectionManagerPhoneAccount), Toast.LENGTH_LONG).show();
        super.onCreateOutgoingConferenceFailed(connectionManagerPhoneAccount, request);
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        Toast.makeText(this, String.valueOf(request) + String.valueOf(connectionManagerPhoneAccount), Toast.LENGTH_LONG).show();
        Connection outgoingCall = createConnection(request);
        outgoingCall.setDialing();
        return outgoingCall;
//        super.onCreateOutgoingConnection(connectionManagerPhoneAccount, request);
    }

    private Connection createConnection(ConnectionRequest request) {
        connection = new Connection() {
            @Override
            public void onCallAudioStateChanged(CallAudioState state) {
                super.onCallAudioStateChanged(state);
            }

        };

        connection.setConnectionCapabilities(Connection.CAPABILITY_MUTE);
        Toast.makeText(this, request.getAccountHandle().toString(), Toast.LENGTH_SHORT).show();

        connection.setCallerDisplayName("vijay", TelecomManager.PRESENTATION_ALLOWED);
        return connection;
    }

}

