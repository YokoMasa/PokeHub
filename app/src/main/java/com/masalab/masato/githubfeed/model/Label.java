package com.masalab.masato.githubfeed.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Created by Masato on 2018/03/06.
 */

public class Label extends BaseModel {

    public String name;
    public String colorString;
    public static Parcelable.Creator<Label> CREATOR = new Parcelable.Creator<Label>() {
        @Override
        public Label createFromParcel(Parcel parcel) {
            Label label = new Label();
            label.name = parcel.readString();
            label.colorString = parcel.readString();
            return label;
        }

        @Override
        public Label[] newArray(int i) {
            return new Label[i];
        }
    };

    public Spanned getSpanned() {
        Spannable spannable = new SpannableString(name);
        String notColorRegex = "[^0-9a-fA-F]";
        colorString = colorString.replaceAll(notColorRegex, "");
        if (colorString.length() == 6) {
            int color = Color.parseColor("#" + colorString);
            BackgroundColorSpan background = new BackgroundColorSpan(color);
            spannable.setSpan(background, 0, spannable.length(), Spanned.SPAN_COMPOSING);
            if (shouldTextBeWhite(color)) {
                ForegroundColorSpan foreground = new ForegroundColorSpan(Color.WHITE);
                spannable.setSpan(foreground, 0, spannable.length(), Spanned.SPAN_COMPOSING);
            }
        }
        return spannable;
    }

    private boolean shouldTextBeWhite(int backgroundColor) {
        int r = Color.red(backgroundColor);
        int g = Color.green(backgroundColor);
        int b = Color.blue(backgroundColor);
        int brightness = (int) (r * 299 + g * 587 + b * 114) / 1000;
        return brightness < 125;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(colorString);
    }
}
