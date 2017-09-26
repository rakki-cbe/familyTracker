package radhakrishnan.familytracker.traking.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by radhakrishnan on 7/9/17.
 */

public class TrakingModel {
    private List<String> userId = new ArrayList<>();

    public TrakingModel() {
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public boolean add(String user) {
        if (!userId.contains(user)) {
            userId.add(user);
            return true;
        }
        return false;
    }

    public boolean remove(String user) {
        if (userId.contains(user)) {
            userId.remove(user);
            return true;
        }
        return false;
    }

}
