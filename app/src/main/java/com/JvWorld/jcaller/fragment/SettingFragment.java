package com.JvWorld.jcaller.fragment;


import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.JvWorld.jcaller.R;
import com.JvWorld.jcaller.settingsManagers.AllsettingsHandler;
import com.JvWorld.jcaller.settingsManagers.Premium;
import com.JvWorld.jcaller.someVariable.StcVariable;

public class SettingFragment extends Fragment {


    boolean isDarkModeOn;
    private View view1;
    private TextView settingText, settingActionBarText;
    private SwitchCompat darkMode;
    private ConstraintLayout constraintLayout;
    private CardView premiumCard, ringtoneCard, generalCard, aboutCard, helpCard, privacyCard, rateCard;
    private RelativeLayout speedDialCard, callBlockerCard, callAnnCard, quickCard,
            flashCard, fakeCard;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        isDarkModeOn = StcVariable.isDarkMode(getContext());
        view1 = inflater.inflate(R.layout.fragment_setting, container, false);
        SharedPreferences.Editor editor = getContext().getSharedPreferences("activity1", MODE_PRIVATE).edit();
        editor.putString("Value", "set");
        editor.apply();


        initUI();
        return view1;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUI() {
        TextView ringtoneCardSettingText = view1.findViewById(R.id.ringtone_card_setting_text);
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
        ringtoneCardSettingText.setText(ringtone.getTitle(getContext()));
        ringtoneCardSettingText.setSelected(true);
        //////////// All id ////////////////////
        darkMode = view1.findViewById(R.id.dark_mode_on_off);
        settingText = view1.findViewById(R.id.setting_text);
        constraintLayout = view1.findViewById(R.id.constraint_setting);
        settingActionBarText = view1.findViewById(R.id.action_bar_text);

        premiumCard = view1.findViewById(R.id.premium_card_setting);
        ringtoneCard = view1.findViewById(R.id.ringtone_card_setting);
        generalCard = view1.findViewById(R.id.general_card_setting);
        speedDialCard = view1.findViewById(R.id.speed_dial_settings);
        callBlockerCard = view1.findViewById(R.id.call_blocker_settings);
        callAnnCard = view1.findViewById(R.id.call_announcer_settings);
        quickCard = view1.findViewById(R.id.quick_response_settings);
        flashCard = view1.findViewById(R.id.flash_on_call_settings);
        fakeCard = view1.findViewById(R.id.fake_call_settings);
        aboutCard = view1.findViewById(R.id.about_us_card_setting);
        helpCard = view1.findViewById(R.id.help_card_setting);
        rateCard = view1.findViewById(R.id.rate_card_setting);
        privacyCard = view1.findViewById(R.id.privacy_card_setting);

        darkMode.setChecked(isDarkModeOn);

        btnHandler();

    }

    private void btnHandler() {

        darkMode.setOnClickListener(view -> {
            sharedCheck(!isDarkModeOn);
        });

        premiumCard.setOnClickListener(view -> {
//            gotoActivityNow(1);
            startActivity(new Intent(getContext(), Premium.class));
        });
        ringtoneCard.setOnClickListener(view -> {
//            gotoActivityNow(2);
        });
        generalCard.setOnClickListener(view -> {
            gotoActivityNow(3);
        });
        speedDialCard.setOnClickListener(view -> {
            gotoActivityNow(4);
        });
        callBlockerCard.setOnClickListener(view -> {
            gotoActivityNow(5);
        });
        callAnnCard.setOnClickListener(view -> {
            gotoActivityNow(6);
        });
        quickCard.setOnClickListener(view -> {
            gotoActivityNow(7);
        });
        flashCard.setOnClickListener(view -> {
            gotoActivityNow(8);
        });
        fakeCard.setOnClickListener(view -> {
            gotoActivityNow(9);
        });
        aboutCard.setOnClickListener(view -> {
//            gotoActivityNow(10);
        });
        helpCard.setOnClickListener(view -> {
//            gotoActivityNow(11);
        });
        privacyCard.setOnClickListener(view -> {
//            gotoActivityNow(12);
        });
        rateCard.setOnClickListener(view -> {
//            intenet to playstore
            // wait for while
        });

    }

    private void gotoActivityNow(int q) {
        Intent intent = new Intent(getContext(), AllsettingsHandler.class);
        intent.putExtra("settingsIntent", q);
        startActivity(intent);
    }


    private void sharedCheck(boolean isDarkMode) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences("theme", MODE_PRIVATE).edit();
        editor.putBoolean("mode", isDarkMode);
        editor.apply();
        StcVariable.isDarkMode(getContext());
    }


}