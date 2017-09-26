package radhakrishnan.familytracker.traking.model;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import radhakrishnan.familytracker.base.DaoBase;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * Created by radhakrishnan on 5/9/17.
 */

public class CodeDao extends DaoBase {
    private static CodeDao codeDao = new CodeDao();
    private Query query;

    private CodeDao() {
        super();
    }

    public static CodeDao getInstance() {
        return codeDao;
    }

    public void getCode(final @Nullable CallBack call) {
        DatabaseReference trakingTable = database.child("TrackingCode");
        query = trakingTable.orderByChild("id").equalTo(PrefrenceHandler.getInstance().gutUserInfo());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshotsch = dataSnapshot.getChildren();
                query.removeEventListener(this);
                for (DataSnapshot data : dataSnapshotsch) {
                    if (data != null) {
                        data.getRef().removeValue();

                    }

                }
                String code = setValue();
                if (call != null)
                    call.success(code);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                if (call != null)
                    call.error("Something went wrong");
                else
                    throw new RuntimeException("Something wrong");

            }
        });


    }

    private String setValue() {
        DatabaseReference trakingTable = database.child("TrackingCode");
        final int code = getRandomSixDigitNumber();
        TrackingCode Tcode = new TrackingCode(PrefrenceHandler.getInstance().gutUserInfo(), code + "");
        trakingTable.child(code + "").setValue(Tcode);
        return code + "";
    }

    private int getRandomSixDigitNumber() {
        Random rand = new Random();

        return rand.nextInt(100000) + 800000;
    }

    public void checkCode(final checkValidation call, String code) {
        DatabaseReference trakingTable = database.child("TrackingCode");
        query = trakingTable.child(code);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                query.removeEventListener(this);

                if (dataSnapshot != null) {
                    TrackingCode code = dataSnapshot.getValue(TrackingCode.class);
                    if (code != null)
                        call.codeValid(code.getId());
                    else
                        call.error("Something wrongs");
                    return;
                }
                if (call != null)
                    call.error("invalid code");


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                call.error("Something went wrong");
                Log.w("DAO", "Failed to read value.", error.toException());
            }
        });

    }

    public interface CallBack {
        void success(String code);

        void error(String errorMsg);

    }

    public interface checkValidation {
        void codeValid(String userId);

        void error(String errorMsg);

    }
}
