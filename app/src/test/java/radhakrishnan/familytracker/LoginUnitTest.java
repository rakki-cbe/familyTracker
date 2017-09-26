package radhakrishnan.familytracker;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import radhakrishnan.familytracker.UserInfo.LoginPresenter;
import radhakrishnan.familytracker.UserInfo.LoginView;
import radhakrishnan.familytracker.UserInfo.model.UserModel;
import radhakrishnan.familytracker.UserInfo.model.Userdao;
import radhakrishnan.familytracker.utill.PrefrenceHandler;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {
    @Mock
    Context context;
    @Mock
    SharedPreferences preferences;
    @Mock
    LoginView view;
    @Mock
    Userdao database;
    @Mock
    Userdao.userCallback callback;
    @Mock
    UserModel user;
    private LoginPresenter presenter;
    @Captor
    private ArgumentCaptor<Userdao.userCallback> callBackArgumentCaptor;

    @Before
    public void initialize() {
        PrefrenceHandler.init(context);
        PrefrenceHandler.getInstance().setSharedPref(preferences);
        presenter = new LoginPresenter(view, database);
    }

    @Test
    public void testcheckUserAlreadyPresentInOurDb() {
        //We are mocking the dao because we dont have firebase in JVM and set to presenter
        presenter.setUserDataManager(database);
        //we trigger callback with object when it try to call firebase
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                user = new UserModel();
                user.setEmail("rakki.bsc2009@gmail.com");
                user.setName("test");
                user.setId("addakldaskljdkl");
                callback = (Userdao.userCallback) invocation.getArguments()[0];
                callback.UserCheckStatus(user, false);
                return null;
            }
        }).when(database).checkUser(any(Userdao.userCallback.class), anyString());
        presenter.checkUserAlreadyPresentInOurDb("rakki.bs");
        verify(database, times(1)).creatUser(any(UserModel.class), any(Userdao.userCallback.class));


    }

    @Test
    public void userCheckWithEmpty() {
        //We are mocking the dao because we dont have firebase in JVM and set to presenter
        presenter.setUserDataManager(database);
        //we trigger callback with object when it try to call firebase
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                callback = (Userdao.userCallback) invocation.getArguments()[0];
                callback.UserCheckStatus(user, true);
                return null;
            }
        }).when(database).checkUser(any(Userdao.userCallback.class), anyString());
        presenter.checkUserAlreadyPresentInOurDb("rakki.bs");
        verify(view, times(1)).setVisibilityName(true);


    }


    @Test
    public void CheckEmptyLogin() throws Exception {
        // initialize();
        when(PrefrenceHandler.getInstance().gutUserInfo()).thenReturn("");
        assertEquals(false, presenter.checkUserLoggedIn());

    }

    @Test
    public void CheckLoginWithValues() throws Exception {
        // initialize();
        when(PrefrenceHandler.getInstance().gutUserInfo()).thenReturn("rakki.bsc@2009@gmail.com");
        assertEquals(true, presenter.checkUserLoggedIn());

    }
}