package radhakrishnan.familytracker.utill;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by radhakrishnan on 5/9/17.
 */

public class utils {

    public static void logOut(Activity context) {
        PrefrenceHandler.getInstance().putUserInfo("");

    }

    public static void copyText(String code, Context context) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("code", code);
        clipboard.setPrimaryClip(clip);
    }
}
