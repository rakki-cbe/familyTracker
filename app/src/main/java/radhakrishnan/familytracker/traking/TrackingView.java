package radhakrishnan.familytracker.traking;

import radhakrishnan.familytracker.traking.model.Location;

/**
 * Created by radhakrishnan on 7/9/17.
 */

interface TrackingView {
    void showMyTrackCode(String code);

    void updateUsersLocation(Location location);

    void errorCreatingCode(int error_Code);


}
