package radhakrishnan.familytracker.base;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by radhakrishnan on 5/9/17.
 */

public class DaoBase {
    public DatabaseReference database;

    public DaoBase() {
        this.database = FirebaseDatabase.getInstance().getReference();
    }

}
