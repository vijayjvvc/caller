package com.JvWorld.jcaller.callHandler;


import android.bluetooth.BluetoothDevice;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.CallAudioState;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.RemoteConference;
import android.telecom.RemoteConnection;
import android.telecom.StatusHints;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutgoingService extends ConnectionService {

    private final Map<RemoteConnection, TestManagedConnection> mManagedConnectionByRemote
            = new HashMap<>();

    static void log(String msg) {
        Log.w("telecomtestcs", "[TestConnectionManager] " + msg);
    }

    @Override
    public Connection onCreateOutgoingConnection(
            PhoneAccountHandle connectionManagerAccount,
            final ConnectionRequest request) {
//        Toast.makeText(this, String.valueOf(connectionManagerAccount), Toast.LENGTH_SHORT).show();
        return super.onCreateOutgoingConnection(connectionManagerAccount, request);
//        return makeConnection(request, false);
    }

    @Override
    public Connection onCreateIncomingConnection(
            PhoneAccountHandle connectionManagerAccount,
            final ConnectionRequest request) {
        return makeConnection(request, true);
    }

    @Override
    public void onConference(Connection a, Connection b) {
        conferenceRemoteConnections(
                ((TestManagedConnection) a).mRemote,
                ((TestManagedConnection) b).mRemote);
    }

    Map<RemoteConnection, TestManagedConnection> getManagedConnectionByRemote() {
        return mManagedConnectionByRemote;
    }

    private Connection makeConnection(ConnectionRequest request, boolean incoming) {

        RemoteConnection remote = incoming
                ? createRemoteIncomingConnection(request.getAccountHandle(), request)
                : createRemoteOutgoingConnection(request.getAccountHandle(), request);
        TestManagedConnection local = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            local = new TestManagedConnection(remote, false);
        }
        Toast.makeText(this, String.valueOf(request.getAccountHandle() + "00"), Toast.LENGTH_SHORT).show();
        mManagedConnectionByRemote.put(remote, local);
        return local;
    }

    public final class TestManagedConnection extends Connection {
        private final RemoteConnection mRemote;
        @RequiresApi(api = Build.VERSION_CODES.N_MR1)

        private final RemoteConnection.Callback mRemoteCallback = new RemoteConnection.Callback() {
            @Override
            public void onStateChanged(RemoteConnection connection, int state) {
                setState(state);
            }

            @Override
            public void onDisconnected(
                    RemoteConnection connection, DisconnectCause disconnectCause) {
                setDisconnected(disconnectCause);
                destroy();
            }

            @Override
            public void onRingbackRequested(RemoteConnection connection, boolean ringback) {
                setRingbackRequested(ringback);
            }

            @Override
            public void onConnectionCapabilitiesChanged(RemoteConnection connection,
                                                        int connectionCapabilities) {
                setConnectionCapabilities(connectionCapabilities);
            }

            @Override
            public void onConnectionPropertiesChanged(RemoteConnection connection,
                                                      int connectionProperties) {
                setConnectionProperties(connectionProperties);
            }

            @Override
            public void onPostDialWait(RemoteConnection connection, String remainingDigits) {
                setPostDialWait(remainingDigits);
            }

            @Override
            public void onVoipAudioChanged(RemoteConnection connection, boolean isVoip) {
                setAudioModeIsVoip(isVoip);
            }

            @Override
            public void onStatusHintsChanged(RemoteConnection connection, StatusHints statusHints) {
                setStatusHints(statusHints);
            }

            @Override
            public void onVideoStateChanged(RemoteConnection connection, int videoState) {
                setVideoState(videoState);
            }

            @Override
            public void onAddressChanged(
                    RemoteConnection connection, Uri address, int presentation) {
                setAddress(address, presentation);
            }

            @Override
            public void onCallerDisplayNameChanged(
                    RemoteConnection connection, String callerDisplayName, int presentation) {
                setCallerDisplayName(callerDisplayName, presentation);
            }

            @Override
            public void onDestroyed(RemoteConnection connection) {
                destroy();
                mManagedConnectionByRemote.remove(mRemote);
            }

            @Override
            public void onConferenceableConnectionsChanged(
                    RemoteConnection connect,
                    List<RemoteConnection> conferenceable) {
                List<Connection> c = new ArrayList<>();
                for (RemoteConnection remote : conferenceable) {
                    if (mManagedConnectionByRemote.containsKey(remote)) {
                        c.add(mManagedConnectionByRemote.get(remote));
                    }
                }
                setConferenceableConnections(c);
            }

        };
        private final boolean mIsIncoming;

        @RequiresApi(api = Build.VERSION_CODES.N_MR1)
        TestManagedConnection(RemoteConnection remote, boolean isIncoming) {
            mRemote = remote;
            mIsIncoming = isIncoming;
            mRemote.registerCallback(mRemoteCallback);
            setState(mRemote.getState());
            setVideoState(mRemote.getVideoState());
            setConnectionProperties(remote.getConnectionProperties());
        }

        @Override
        public void onAbort() {
            mRemote.abort();
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onAnswer(int videoState) {
            mRemote.answer();
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onDisconnect() {
            mRemote.disconnect();
        }

        @Override
        public void onPlayDtmfTone(char c) {
            mRemote.playDtmfTone(c);
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onHold() {
            mRemote.hold();
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onReject() {
            mRemote.reject();
        }

        /**
         * ${inheritDoc}
         */
        @Override
        public void onUnhold() {
            mRemote.unhold();
        }

        @Override
        public void onCallAudioStateChanged(CallAudioState state) {
            mRemote.setCallAudioState(state);
        }

        private void setState(int state) {
            log("setState: " + state);
            switch (state) {
                case STATE_ACTIVE:
                    setActive();
                    break;
                case STATE_HOLDING:
                    setOnHold();
                    break;
                case STATE_DIALING:
                    setDialing();
                    break;
                case STATE_RINGING:
                    setRinging();
                    break;
            }
        }
    }

}
