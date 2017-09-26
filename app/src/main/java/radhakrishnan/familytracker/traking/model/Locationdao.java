package radhakrishnan.familytracker.traking.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import radhakrishnan.familytracker.UserInfo.model.Userdao;
import radhakrishnan.familytracker.base.BasicDaoInterface;
import radhakrishnan.familytracker.base.DaoBase;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * Created by radhakrishnan on 29/8/17.
 */

public class Locationdao extends DaoBase {
    private static Locationdao instance = new Locationdao();

    private Locationdao() {
        super();
    }

    public static Locationdao getInstance() {
        return instance;
    }

    public boolean setUserInfo(Userdao.userCallback callBack, double lat, double longt) {
        DatabaseReference locationTable = database.child("Location");
        Location loc = new Location();
        loc.setLat(lat);
        loc.setLongt(longt);
        loc.setUserId(PrefrenceHandler.getInstance().gutUserInfo());
        Calendar c = Calendar.getInstance();
        loc.setDate(c.getTime());
        locationTable.child(PrefrenceHandler.getInstance().gutUserInfo()).setValue(loc);
        return false;
    }


    public void watchUsersLocation(UserLocation callBack, TrakingModel model) {

        DatabaseReference userTable = database.child("Location");
        UdateListener listener = new UdateListener(callBack);
        for (String id : model.getUserId()) {
            userTable.child(id).addValueEventListener(listener);
        }


    }

    private void getUserInfoOfTheUser(UserLocation CallBack, Location loc) {
        Userdao.getInstance().getUserInfo(CallBack, loc);
    }

    public interface UserLocation extends BasicDaoInterface {
        void updateLocation(String userId, Location location);
    }

    private class UdateListener implements ValueEventListener {
        UserLocation LocationCallBack;

        UdateListener(UserLocation locationCallBack) {
            LocationCallBack = locationCallBack;
        }


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            updateLocation(dataSnapshot);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        void updateLocation(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null) {
                Location loc = dataSnapshot.getValue(Location.class);
                getUserInfoOfTheUser(LocationCallBack, loc);

            }
        }

        private void removeChild(DataSnapshot dataSnapshot) {
        }

    }


}
