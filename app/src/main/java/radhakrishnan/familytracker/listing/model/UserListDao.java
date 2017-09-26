package radhakrishnan.familytracker.listing.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import radhakrishnan.familytracker.UserInfo.model.UserModel;
import radhakrishnan.familytracker.UserInfo.model.Userdao;
import radhakrishnan.familytracker.base.BasicDaoInterface;
import radhakrishnan.familytracker.base.DaoBase;
import radhakrishnan.familytracker.traking.model.TrakingModel;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * Created by radhakrishnan on 21/9/17.
 */

public class UserListDao extends DaoBase {
    private static UserListDao instance;
    private String table = "TrakedUsers";
    private DatabaseReference database;
    private Map<String, UserModel> users = new HashMap<>();
    private SingleUserInfo usersCallback = new SingleUserInfo();

    private UserListDao() {
        database = FirebaseDatabase.getInstance().getReference().child(table);
    }

    public static UserListDao getInstance() {
        if (instance == null)
            instance = new UserListDao();
        return instance;
    }

    public void getUserList(int type, UserListCallBack callBack) {
        usersCallback.setListCallBack(callBack);
        Query query = database.child(PrefrenceHandler.getInstance().gutUserInfo());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    TrakingModel users = dataSnapshot.getValue(TrakingModel.class);
                    if (users != null && users.getUserId().size() > 0) {
                        for (String id : users.getUserId()) {
                            Userdao.getInstance().getUserInfo(usersCallback, id);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void delete(UserModel model, UserListCallBack listPresenter) {
        Query query = database.child(PrefrenceHandler.getInstance().gutUserInfo());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    TrakingModel users = dataSnapshot.getValue(TrakingModel.class);
                    if (users != null && users.getUserId().size() > 0) {
                        for (String id : users.getUserId()) {
                            Userdao.getInstance().getUserInfo(usersCallback, id);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public interface UserListCallBack extends BasicDaoInterface {


        void UserListSucess(int type, Map<String, UserModel> data);

    }

    private class SingleUserInfo implements ValueEventListener {
        UserListCallBack listCallBack;
        int type;

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot != null && dataSnapshot.exists()) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (user != null)
                    users.put(user.getId(), user);
                else
                    listCallBack.error("Something wrong");
            }
            listCallBack.UserListSucess(type, users);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        void setListCallBack(UserListCallBack listCallBack) {
            this.listCallBack = listCallBack;
        }
    }
}
