package radhakrishnan.familytracker.listing;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import radhakrishnan.familytracker.R;
import radhakrishnan.familytracker.UserInfo.UserListAdapter;
import radhakrishnan.familytracker.UserInfo.model.UserModel;
import radhakrishnan.familytracker.base.BaseFragment;
import radhakrishnan.familytracker.listing.view.ListView;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends BaseFragment implements ListView, UserListAdapter.ItemActionListener {

    // TODO: Customize parameter argument names
    private static final String ARG_TYPE = "type";
    private static final int TYPE_MY_TRACKING = 1;
    private static final int TYPE_TRACKERS_OF_ME = 2;
    ListPresenter presenter;
    // TODO: Customize parameters
    private int type = 1;
    private OnListFragmentInteractionListener mListener;
    private UserListAdapter userListAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Fragment newInstanceMyTracking() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, TYPE_MY_TRACKING);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstanceTrackedByOthers() {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, TYPE_TRACKERS_OF_ME);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        presenter = new ListPresenter(this);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            userListAdapter = new UserListAdapter(this);

            recyclerView.setAdapter(userListAdapter);
            presenter.getListOfUsers(type);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateData(Map<String, UserModel> data) {
        userListAdapter.setList(data);

    }

    @Override
    public void updateData(UserModel item) {

    }

    @Override
    public void removeItem(UserModel model) {
        presenter.delete(model);
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(UserModel item);

    }
}
