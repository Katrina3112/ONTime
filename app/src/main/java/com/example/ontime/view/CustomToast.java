package com.example.ontime.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ontime.R;

public class CustomToast {

    private static Toast toast;


    private static void show(Context context, String message, int resourceImg, int resourceBkg, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(resourceBkg, null); // Avoid casting

        TextView toastText = layout.findViewById(R.id.toast_text);
        ImageView toastImage = layout.findViewById(R.id.toast_image);

        toastText.setText(message);
        toastImage.setImageResource(resourceImg);

        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public static void showSuccess(Context context, String message) {
        show(context, message, R.drawable.baseline_check_box_24, R.layout.custom_success, Toast.LENGTH_SHORT);
    }

    public static void showWarning(Context context, String message) {
        show(context, message, R.drawable.baseline_announcement_24, R.layout.custom_toast_layout, Toast.LENGTH_SHORT);
    }

    public static void showCustom(Context context, String message, int resourceImg, int resourceBkg, int duration) {
        show(context, message, resourceImg, resourceBkg, duration);
    }

    public static void showError(TextView s,String eror){
        s.setError(eror);
        s.requestFocus();
    }
}
