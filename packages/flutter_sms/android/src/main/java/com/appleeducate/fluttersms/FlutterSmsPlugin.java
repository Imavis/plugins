package com.appleeducate.fluttersms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterSmsPlugin
 */
public class FlutterSmsPlugin implements MethodCallHandler {
    private Activity activity;

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_sms");
        channel.setMethodCallHandler(new FlutterSmsPlugin(registrar.activity()));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("sendSMS")) {
            String message = call.argument("message");
            ArrayList<String> recipients = call.argument("recipients");
            if (message != null && recipients != null) {
                sendSMS(recipients, message);
                result.success("SMS Sent!");
            } else {
                result.error("Error in sending sms, recipient or message is null", null, null);
            }
        } else {
            result.notImplemented();
        }
    }

    private FlutterSmsPlugin(Activity activity) {
        this.activity = activity;
    }

    private void sendSMS(@NonNull ArrayList<String> phones, @NonNull String message) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("address", phones.get(0));
        sendIntent.putExtra("sms_body", message);
        sendIntent.setData(Uri.parse("smsto:" + phones.get(0)));
        activity.startActivity(sendIntent);
    }
}
