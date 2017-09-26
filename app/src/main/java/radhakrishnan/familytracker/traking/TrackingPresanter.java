package radhakrishnan.familytracker.traking;

import radhakrishnan.familytracker.traking.model.CodeDao;
import radhakrishnan.familytracker.traking.model.Location;
import radhakrishnan.familytracker.traking.model.Locationdao;
import radhakrishnan.familytracker.traking.model.TrakerDao;
import radhakrishnan.familytracker.traking.model.TrakingModel;

/**
 * Created by radhakrishnan on 7/9/17.
 */

class TrackingPresanter implements CodeDao.CallBack, CodeDao.checkValidation, TrakerDao.TrakingUser, Locationdao.UserLocation {
    private TrackingView view;
    private TrakingModel users = new TrakingModel();


    TrackingPresanter(TrackingView view) {
        this.view = view;
        TrakerDao.getInstance().intializeTrakingUsers(this);
    }

    void getMyCode() {
        CodeDao.getInstance().getCode(this);

    }

    void startTrackMyFriend(String code) {

        CodeDao.getInstance().checkCode(this, code);
    }

    @Override
    public void success(String code) {
        view.showMyTrackCode(code);
    }

    @Override
    public void codeValid(String userId) {
        TrakerDao.getInstance().startTracking(this, userId);

    }

    @Override
    public void trackingUserUpdate(TrakingModel model) {
        users = model;
        if (model != null)
            Locationdao.getInstance().watchUsersLocation(this, model);
    }

    @Override
    public void addedToTracking() {

    }

    @Override
    public void error(String errorMsg) {
        view.errorCreatingCode(0);
    }

    @Override
    public void updateLocation(String userId, Location location) {
        view.updateUsersLocation(location);
    }
}
