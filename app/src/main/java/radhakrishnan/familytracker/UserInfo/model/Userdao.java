package radhakrishnan.familytracker.UserInfo.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import radhakrishnan.familytracker.base.BasicDaoInterface;
import radhakrishnan.familytracker.traking.model.Location;
import radhakrishnan.familytracker.traking.model.Locationdao;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * Created by radhakrishnan on 29/8/17.
 */

public class Userdao {
    private static Userdao instance;
    private DatabaseReference database;

    private Userdao() {

    }

    public static Userdao getInstance() {
        if (instance == null) {

            instance = new Userdao();
            instance.init();
        }
        return instance;
    }

    private void init() {
        database = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public boolean setUserInfo(userCallback callBack, UserModel user) {

        if (user.getId() != null && !user.getId().equals("")) {
            database.child(user.getId()).setValue(user);
            callBack.UserLoggedIn(user);
        } else
            getUserInfo(callBack, user);
        return false;
    }

    private boolean getUserInfo(userCallback callBack, UserModel user) {
        Query query = database.orderByKey().equalTo(user.getId());

        query.addListenerForSingleValueEvent(new GetUserInfo(callBack, user));
        return true;
    }

    public void getUserInfo(Locationdao.UserLocation callBack, Location loc) {
        // DatabaseReference userTable = database.child("Users");
        Query query = database.orderByKey().equalTo(loc.getUserId());
        query.addListenerForSingleValueEvent(new SingleValueUpdateListener(callBack, loc));

    }

    public void checkUser(final userCallback callBack, String email) {
        UserModel user = new UserModel();
        user.setEmail(email);
        Query query = database.orderByChild("email").equalTo(email);

        query.addListenerForSingleValueEvent(new UserCreateIfNotExit(callBack, user));
    }

    public void getUserInfo(ValueEventListener usersCallback, String id) {
        Query query = database.child(id);
        query.addListenerForSingleValueEvent(usersCallback);
    }

    public void creatUser(UserModel user, userCallback callback) {
        String userId = database.push().getKey();
        user.setId(userId);
        database.child(userId).setValue(user);
        callback.UserCreated(user);
    }

    public interface userCallback extends BasicDaoInterface {
        void UserLoggedIn(UserModel user);

        void UserCheckStatus(UserModel user, boolean isExistingUser);

        void UserCreated(UserModel user);

    }

    private class UserCreateIfNotExit implements ValueEventListener {
        UserModel user;
        userCallback callBack;

        UserCreateIfNotExit(userCallback callBack, UserModel user) {
            this.user = user;
            this.callBack = callBack;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterable<DataSnapshot> dataSnapshotsch = dataSnapshot.getChildren();
            UserModel user = null;
            for (DataSnapshot data : dataSnapshotsch) {
                user = data.getValue(UserModel.class);
                callBack.UserCheckStatus(user, true);
            }
            if (user == null) {
                callBack.UserCheckStatus(this.user, false);

            }


        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            callBack.error("Somthing wrong");
            Log.w("DAO", "Failed to read value.", error.toException());
        }
    }

    private class GetUserInfo implements ValueEventListener {
        UserModel user;
        userCallback callBack;

        GetUserInfo(userCallback callBack, UserModel user) {
            this.user = user;
            this.callBack = callBack;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Iterable<DataSnapshot> dataSnapshotsch = dataSnapshot.getChildren();
            UserModel user = null;
            for (DataSnapshot data : dataSnapshotsch) {
                user = data.getValue(UserModel.class);
                if (user != null) {
                    user.setName(this.user.getName());
                    PrefrenceHandler.getInstance().putUserInfo(user.getId());
                    database.child(user.getId()).setValue(user);
                    callBack.UserLoggedIn(user);
                } else
                    callBack.error("Something wrong ");

            }
            if (user == null) {
                user = new UserModel();
                user.setEmail(this.user.getEmail());
                String userId = database.push().getKey();
                user.setId(userId);
                database.child(userId).setValue(user);

            }


        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
            callBack.error("Somthing wrong");
            Log.w("DAO", "Failed to read value.", error.toException());
        }
    }

    private class SingleValueUpdateListener implements ValueEventListener {
        Locationdao.UserLocation callBack;
        Location loc;

        SingleValueUpdateListener(Locationdao.UserLocation callBack, Location loc) {
            this.callBack = callBack;
            this.loc = loc;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
                UserModel user = dataSnapshot.child(loc.getUserId()).getValue(UserModel.class);
                // Users user = dataSnapshot.getValue();
                loc.setUser(user);
                callBack.updateLocation(loc.getUserId(), loc);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
