package radhakrishnan.familytracker.UserInfo;

import radhakrishnan.familytracker.base.BaseView;

/**
 * Created by radhakrishnan on 19/9/17.
 */

public interface LoginView extends BaseView {
    void setVisibilityName(boolean visibility);

    void initSharedPred();

    String getName();

    void setName(String s);

    void logInSuccess();

    void setErrorName(int nameShouldNotBeEmpty);
}
