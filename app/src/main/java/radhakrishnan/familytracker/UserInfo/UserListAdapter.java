package radhakrishnan.familytracker.UserInfo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import radhakrishnan.familytracker.R;
import radhakrishnan.familytracker.UserInfo.model.UserModel;
import radhakrishnan.familytracker.listing.ListFragment.OnListFragmentInteractionListener;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private static final UserModel[] userModelsTemp = new UserModel[]{};
    private final ItemActionListener mListener;
    private UserModel[] mValues;

    public UserListAdapter(ItemActionListener listener) {
        mListener = listener;
    }

    public void setList(Map<String, UserModel> mValues) {
        this.mValues = mValues.values().toArray(userModelsTemp);
        notifyDataSetChanged();
    }
    /*public void updateList(UserModel model,int position)
    {
        mValues.add(position,model);
        notifyItemChanged(position);
    }*/

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues[position];
        holder.setValues();
    }

    @Override
    public int getItemCount() {

        return (mValues != null) ? mValues.length : 0;
    }

    public interface ItemActionListener {
        void removeItem(UserModel model);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mContentView;
        UserModel mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        void setValues() {
            mContentView.setText(mItem.getName());

        }
    }
}
