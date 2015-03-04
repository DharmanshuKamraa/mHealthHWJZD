package info.androidhive.slidingmenu;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class ProfileFragment extends PreferenceFragment {

    private Context myContext;
    private boolean mListStyled;
    private ActionBar mActionBar;
    private float mActionBarHeight;
    private int overallXScrol = 0;

    //	public ProfileFragment(){}
//
//
//	@Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//
//        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
//
//        return rootView;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        mActionBar = getActivity().getActionBar();
//        mActionBar.hide();
    }
}
