package radhakrishnan.familytracker.traking.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import radhakrishnan.familytracker.base.BasicDaoInterface;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * Created by radhakrishnan on 7/9/17.
 */

public class TrakerDao {
    private static TrakerDao instance = new TrakerDao();
    private DatabaseReference database;
    private Query query;

    private TrakerDao() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public static TrakerDao getInstance() {
        return instance;
    }

    public void startTracking(final TrakingUser callBack, final String userId) {


        final DatabaseReference trackingTable = database.child("TrakedUsers");
        query = trackingTable.child(PrefrenceHandler.getInstance().gutUserInfo());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                query.removeEventListener(this);
                if (dataSnapshot != null) {
                    TrakingModel model = dataSnapshot.getValue(TrakingModel.class);
                    if (model != null)
                        model.add(userId);
                    else
                        callBack.error("No data found");
                    callBack.addedToTracking();
                    trackingTable.child(PrefrenceHandler.getInstance().gutUserInfo()).setValue(model);
                    return;
                }
                TrakingModel model = new TrakingModel();
                model.add(userId);
                trackingTable.child(PrefrenceHandler.getInstance().gutUserInfo()).setValue(model);
                callBack.addedToTracking();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.error(databaseError.getMessage());
            }
        });


    }

    public void intializeTrakingUsers(final TrakingUser callBack) {


        final DatabaseReference trackingTable = database.child("TrakedUsers");
        query = trackingTable.child(PrefrenceHandler.getInstance().gutUserInfo());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    TrakingModel model = dataSnapshot.getValue(TrakingModel.class);
                    callBack.trackingUserUpdate(model);

                }
             /*   Iterable<DataSnapshot> dataSnapshotsch= dataSnapshot.getChildren();
                for(DataSnapshot data:dataSnapshotsch)
                {
                    if(data!=null)
                    {

                    }
                }*/


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.error(databaseError.getMessage());
            }
        });


    }

    public interface TrakingUser extends BasicDaoInterface {
        void trackingUserUpdate(TrakingModel model);

        void addedToTracking();


    }
}
