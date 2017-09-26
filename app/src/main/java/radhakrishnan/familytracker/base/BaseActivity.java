package radhakrishnan.familytracker.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by radhakrishnan on 16/9/17.
 */

public abstract class BaseActivity extends AppCompatActivity {
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public String getStringFromInt(int id) {
        return getResources().getString(id);
    }

}
