package com.masalab.masato.githubfeed.util;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import org.xml.sax.XMLReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Masato on 2018/02/27.
 */

public class EventUtil {

    public static CharSequence parseEventContent(String content) {
        return Html.fromHtml(content, null, new MTagHandler());
    }

    private static class MTagHandler implements Html.TagHandler {

        private Map<String, List<Integer>> startedTags = new HashMap<>();

        @Override
        public void handleTag(boolean b, String s, Editable editable, XMLReader xmlReader) {
            if (b) {
                handleStartTag(s, editable);
            } else {
                handleEndTag(s, editable);
            }
        }

        private void handleStartTag(String tag, Editable editable) {
            List<Integer> positions = null;
            if (startedTags.containsKey(tag)) {
                positions = startedTags.get(tag);
            } else {
                positions = new ArrayList<Integer>();
                startedTags.put(tag, positions);
            }

            if (positions != null) {
                positions.add(editable.length());
            }
        }

        private void handleEndTag(String tag, Editable editable) {
            List<Integer> positions = startedTags.get(tag);
            if (positions == null) {
                return;
            }

            int lastIndex = positions.size() - 1;
            int from = positions.get(lastIndex);
            int to = editable.length();
            processTag(tag, editable, from, to);
            positions.remove(lastIndex);
        }

        private void processTag(String tag, Editable editable, int from, int to) {
            if (tag.startsWith("color")) {
                processColorTag(tag, editable, from, to);
            }
        }

        private void processColorTag(String tag, Editable editable, int from, int to) {
            String colorString = tag.substring(4);
            String notColorRegex = "[^0-9a-fA-F]";
            colorString = colorString.replaceAll(notColorRegex, "");
            if (colorString.length() == 6) {
                int color = Color.parseColor("#" + colorString);
                BackgroundColorSpan background = new BackgroundColorSpan(color);
                editable.setSpan(background, from, to, Spanned.SPAN_COMPOSING);
                if (shouldTextBeWhite(color)) {
                    ForegroundColorSpan foreground = new ForegroundColorSpan(Color.WHITE);
                    editable.setSpan(foreground, from, to, Spanned.SPAN_COMPOSING);
                }
            }
        }

        private boolean shouldTextBeWhite(int backgroundColor) {
            int r = Color.red(backgroundColor);
            int g = Color.green(backgroundColor);
            int b = Color.blue(backgroundColor);
            int brightness = (int) (r * 299 + g * 587 + b * 114) / 1000;
            return brightness < 125;
        }

    }

}
