package radhakrishnan.familytracker.UserInfo;

import radhakrishnan.familytracker.R;
import radhakrishnan.familytracker.UserInfo.model.UserModel;
import radhakrishnan.familytracker.UserInfo.model.Userdao;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * Created by radhakrishnan on 19/9/17.
 */

public class LoginPresenter implements Userdao.userCallback {
    private LoginView view;
    private Userdao userDataManager;
    private UserModel user = new UserModel();

    LoginPresenter(LoginView view) {
        this.view = view;
        userDataManager = Userdao.getInstance();
    }

    /**
     * This will used when we do unittest
     *
     * @param view            view
     * @param userDataManager datamanager
     */
    public LoginPresenter(LoginView view, Userdao userDataManager) {
        this.view = view;
        this.userDataManager = userDataManager;
    }

    public boolean checkUserLoggedIn() {
        return !PrefrenceHandler.getInstance().gutUserInfo().equals("");
    }

    void checkLoginProcess() {
        String name;
        name = view.getName();
        if (name != null && !name.trim().equals("") && user.getEmail() != null && !user.getEmail().trim().equals("")) {

            user.setName(name);
            userDataManager.setUserInfo(this, user);
        } else if (name == null || name.trim().equals("")) {
            view.setErrorName(R.string.NameShouldNotBeEmpty);
        }


    }

    public void setUserDataManager(Userdao userDataManager) {
        this.userDataManager = userDataManager;
    }

    @Override
    public void UserLoggedIn(UserModel user) {
        this.user = user;
        PrefrenceHandler.getInstance().putUserInfo(user.getId());
        view.setVisibilityName(true);
        view.logInSuccess();
    }

    @Override
    public void UserCheckStatus(UserModel user, boolean isExistingUser) {
        if (isExistingUser) {
            this.user = user;
            view.setName(user.getName());
            view.setVisibilityName(true);
        } else {
            userDataManager.creatUser(user, this);
        }
    }

    @Override
    public void UserCreated(UserModel user) {
        this.user = user;
        view.setVisibilityName(true);
    }


    public void checkUserAlreadyPresentInOurDb(String emails) {
        userDataManager.checkUser(this, emails);
    }

    void setEmailAddress(String emailAddress) {
        user.setEmail(emailAddress);

    }

    @Override
    public void error(String msg) {
        view.showToast(view.getStringFromInt(R.string.SomeThingWrongContactAdmin));
        view.setVisibilityName(false);
    }
}
