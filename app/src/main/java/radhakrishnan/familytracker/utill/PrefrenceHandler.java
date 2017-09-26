package radhakrishnan.familytracker.utill;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by radhakrishnan on 29/8/17.
 */

public class PrefrenceHandler {
    private static PrefrenceHandler instance;
    private SharedPreferences sharedPref;

    private PrefrenceHandler(Context context) {
        sharedPref = context.getSharedPreferences("radhakrishnan.familytracker.utill.user", Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        instance = new PrefrenceHandler(context);
    }

    public static PrefrenceHandler getInstance() {
        return instance;
    }

    public void putUserInfo(String userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UserId", userId);
        editor.apply();

    }

    public String gutUserInfo() {
        return sharedPref.getString("UserId", "");

    }

    /**
     * Its for unit test purpose dont remove it
     *
     * @param sharedPref Shared pref
     */
    public void setSharedPref(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
    }
}
