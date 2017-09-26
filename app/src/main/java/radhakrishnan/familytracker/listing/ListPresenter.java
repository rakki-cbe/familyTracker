package radhakrishnan.familytracker.listing;

import java.util.Map;

import radhakrishnan.familytracker.UserInfo.model.UserModel;
import radhakrishnan.familytracker.listing.model.UserListDao;
import radhakrishnan.familytracker.listing.view.ListView;

/**
 * Created by radhakrishnan on 21/9/17.
 */

class ListPresenter implements UserListDao.UserListCallBack {
    public ListView view;

    ListPresenter(ListView view) {
        this.view = view;
    }

    void getListOfUsers(int Type) {
        UserListDao.getInstance().getUserList(Type, this);
    }

    @Override
    public void error(String msg) {
        view.showToast(msg);

    }

    @Override
    public void UserListSucess(int type, Map<String, UserModel> data) {
        view.updateData(data);

    }

    void delete(UserModel model) {
        UserListDao.getInstance().delete(model, this);
    }
}
