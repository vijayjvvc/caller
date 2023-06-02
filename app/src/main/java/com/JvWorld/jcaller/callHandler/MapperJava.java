package com.JvWorld.jcaller.callHandler;

import static android.telecom.Call.STATE_DIALING;

import android.telecom.Call;
import android.annotation.TargetApi;
import android.os.Build;


import org.jetbrains.annotations.NotNull;

import static android.telecom.Call.STATE_ACTIVE;
import static android.telecom.Call.STATE_CONNECTING;
import static android.telecom.Call.STATE_DIALING;
import static android.telecom.Call.STATE_DISCONNECTED;
import static android.telecom.Call.STATE_HOLDING;
import static android.telecom.Call.STATE_RINGING;
import static com.JvWorld.jcaller.callHandler.GsmCall.Status.DISCONNECTED;

public class MapperJava {
    //    @TargetApi(Build.VERSION_CODES.M)
    public static GsmCall toGsmCall(Call call) {
        GsmCall.Status status = DISCONNECTED;
        String displayName = "";
        if (call != null) {
            status = toGsmCallStatus(call.getState());
            displayName = call.getDetails().getHandle().getSchemeSpecificPart();
        }
        return new GsmCall(status, displayName);
    }

    private static final GsmCall.Status toGsmCallStatus(int callState) {
        GsmCall.Status gsmCallState;
        switch (callState) {
            case STATE_DIALING:
                gsmCallState = GsmCall.Status.DIALING;
                break;
            case STATE_RINGING:
                gsmCallState = GsmCall.Status.RINGING;
                break;
            case STATE_ACTIVE:
                gsmCallState = GsmCall.Status.ACTIVE;
                break;
            case STATE_DISCONNECTED:
                gsmCallState = DISCONNECTED;
                break;
            case STATE_CONNECTING:
                gsmCallState = GsmCall.Status.CONNECTING;
                break;
            case STATE_HOLDING:
                gsmCallState = GsmCall.Status.HOLDING;
                break;
            default:
                gsmCallState = GsmCall.Status.UNKNOWN;
                break;
        }

        return gsmCallState;
    }
}
