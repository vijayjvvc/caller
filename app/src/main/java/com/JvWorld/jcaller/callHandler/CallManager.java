package com.JvWorld.jcaller.callHandler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telecom.Call;
import android.telecom.CallAudioState;
import android.telecom.InCallService;
import android.util.Log;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class CallManager {

    @SuppressLint("StaticFieldLeak")
    public static InCallService incallService;


    public static int NUMBER_OF_CALLS = 0;
    @SuppressLint("StaticFieldLeak")
    public static CallManager INSTANCE;
    public static int Call_State = 0;
    public static boolean merge;
    private static BehaviorSubject subject;
    private static Call currentCall = null;
    private static List<Call> callList = new ArrayList<>();

    static {
//        CallManager var = new CallManager();
        INSTANCE = new CallManager();
        subject = BehaviorSubject.create();
    }

    public static Observable updates() {
        BehaviorSubject behaviorSubject = subject;
        return (Observable) behaviorSubject;
    }

    public static void updateCall(@Nullable Call call, int state) {
        Call_State = state;
        currentCall = call;
        if (call != null) {
            try {
                subject.onNext(MapperJava.toGsmCall(call));
            } catch (Exception e) {
                Log.e("eroMapper", e.getMessage());
                e.printStackTrace();
            }

        }

    }

    public static void cancelCall() {
        Call call = currentCall;
        if (call != null) {
            if (call.getState() == Call.STATE_RINGING) {
                INSTANCE.rejectCall();
            } else {
                if (merge) {
                    INSTANCE.disconnectAllCall();
                } else {
                    INSTANCE.disconnectCall();
                }
            }
        }

    }

    public static void acceptCall() {
        Call call = currentCall;
        if (call != null) {
            call.answer(call.getDetails().getVideoState());

        }

    }

    public static void rejectCall() {
        Call call = currentCall;
        if (call != null) {
            call.reject(false, "");
        }
    }

    public static void speakerCallOn(String isSpeaker) {
        switch (isSpeaker) {
            case "speaker":
                incallService.setAudioRoute(CallAudioState.ROUTE_SPEAKER);
                break;
            case "earphone":
                incallService.setAudioRoute(CallAudioState.ROUTE_EARPIECE);
                break;
            case "bluetooth":
                incallService.setAudioRoute(CallAudioState.ROUTE_BLUETOOTH);
                break;
            case "headPhone":
                incallService.setAudioRoute(CallAudioState.ROUTE_WIRED_HEADSET);
                break;
        }
    }

    public static void playDtmfTone(Call call, char c) {
        call.playDtmfTone(c);
        call.stopDtmfTone();
    }

    public static void rejectCallMsg(String value) {
        Call call = currentCall;
        if (call != null) {
            call.reject(true, value);
            NotificationHelper.toShowMissedNotification = false;
        }
    }

    public static void disconnectCall() {
        Call call = currentCall;
        if (call != null) {
            call.disconnect();
        }
    }

    public static void disconnectAllCall() {
//        Call call = currentCall;
        int totalCall = CallListHelper.callList.size();  //6
        int number;
        for (number = 0; totalCall - 1 >= number; number++) {
            CallListHelper.callList.get(number).disconnect();
        }

//        CallListHelper.callList.get(1).disconnect();
//        CallListHelper.callList.get(2).disconnect();
//        while (number <= totalCall-1){
//            CallListHelper.callList.get(totalCall-number).disconnect();
////            CallListHelper.callList.remove(0);
//            number++;
//        }

    }

    public static boolean holdCall() {
        Call call = currentCall;
        boolean hold = false;
        if (call != null) {
            call.hold();
            hold = true;
        }
        return hold;
    }

    public static boolean unHoldCall() {
        boolean unHold = false;
        Call call = currentCall;
        if (call != null) {
            call.unhold();
            unHold = true;
        }
        return unHold;
    }

    public static boolean mergeCall() {
        Call call = currentCall;
        merge = false;
        if (call != null) {
            CallListHelper.callList.get(NUMBER_OF_CALLS - 2).conference(CallListHelper.callList.get(CallManager.NUMBER_OF_CALLS - 1));
            CallListHelper.callList.get(NUMBER_OF_CALLS - 1).mergeConference();
            merge = true;
        }
        return merge;
    }

    public static boolean swap() {
        Call call = currentCall;
        call.swapConference();
        return true;
    }

}
