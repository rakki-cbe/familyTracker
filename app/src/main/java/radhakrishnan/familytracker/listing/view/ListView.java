package radhakrishnan.familytracker.listing.view;

import java.util.Map;

import radhakrishnan.familytracker.UserInfo.model.UserModel;
import radhakrishnan.familytracker.base.BaseView;

/**
 * Created by radhakrishnan on 21/9/17.
 */

public interface ListView extends BaseView {
    void updateData(Map<String, UserModel> data);

    void updateData(UserModel item);
}
