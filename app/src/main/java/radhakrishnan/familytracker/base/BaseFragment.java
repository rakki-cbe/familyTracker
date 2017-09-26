package radhakrishnan.familytracker.base;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by radhakrishnan on 16/9/17.
 */

public abstract class BaseFragment extends Fragment {
    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    public String getStringFromInt(int id) {
        return getResources().getString(id);
    }
}
