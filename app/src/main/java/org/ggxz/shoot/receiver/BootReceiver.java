package org.ggxz.shoot.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.core.content.ContextCompat;

import org.ggxz.shoot.mvp.view.activity.ConfigActivity;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        if (TextUtils.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            Intent newIntent = new Intent(context, ConfigActivity.class);
            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ContextCompat.startActivity(context, newIntent, null);
        }

    }
}
