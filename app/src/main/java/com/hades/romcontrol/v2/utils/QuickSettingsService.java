package com.hades.romcontrol.v2.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.service.quicksettings.TileService;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.hades.romcontrol.v2.MyApp;
import com.hades.romcontrol.v2.R;

public class QuickSettingsService extends TileService implements View.OnClickListener {

    private Dialog dialog;

    @Override
    public void onClick() {

        dialog = getRebootMenuDialog();

        dialog.show();

        sendBroadcast(new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        PowerManager powerManager = (PowerManager) MyApp.getContext().getSystemService(Context.POWER_SERVICE);
        switch (id) {
            case R.id.rebootDevice:
                powerManager.reboot(null);
                break;
            case R.id.rebootRecovery:
                powerManager.reboot("recovery");
                break;
            case R.id.rebootUI:
                Utils.killPackage("com.android.systemui");
                break;

            case R.id.protectiveView:
                dialog.dismiss();
                break;

        }
       dialog.dismiss();

    }

    private Dialog getRebootMenuDialog() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(new ContextThemeWrapper(MyApp.getContext(), R.style.AppTheme)).inflate(R.layout.reboot_layout, null);
        view.findViewById(R.id.rebootDevice).setOnClickListener(this);
        view.findViewById(R.id.rebootRecovery).setOnClickListener(this);
        view.findViewById(R.id.rebootUI).setOnClickListener(this);
        view.findViewById(R.id.protectiveView).setOnClickListener(this);
        Dialog dialog = new AlertDialog.Builder(getApplicationContext(), R.style.RebootDialogTheme)
                .setView(view)
                .create();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.protective_semi_transparent_black));
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        return dialog;
    }
}
