package info.androidhive.slidingmenu;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import info.androidhive.slidingmenu.api.FoodItemConnect;
import info.androidhive.slidingmenu.interfaces.FoodItemAsyncResponse;
import info.androidhive.slidingmenu.model.FoodItemCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyCartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MyCartFragment extends Fragment implements FoodItemAsyncResponse {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_food_item, container, false);
        setInitialAdapter();
        return rootView;
    }

    public void setInitialAdapter() {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        f.execute("GET" , "items/" , "carted=1");
    }

    public void makeFilterCall(String q) {
        FoodItemConnect f = new FoodItemConnect();
        f.delegate = this;
        f.activity = getActivity();
        if (q.isEmpty()) {
            f.execute("GET" , "items/" , "carted=1");
        } else {
            f.execute("GET" , "items/" , "carted=1&q="+q);
        }
    }

    public void processItemListFetched(String s) {
//        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();
        ArrayList<Card> cards = new ArrayList<Card>();
        try {
            JSONArray jsonListItems = new JSONArray(s);
            for (int i = 0; i < jsonListItems.length(); i++) {
                JSONObject jsonObject = jsonListItems.getJSONObject(i);
                FoodItemCard card = new FoodItemCard(getActivity());
                card.setParams(
                        jsonObject.getLong("id") ,
                        jsonObject.getString("name") ,
                        "Co Op Hanover." ,
                        jsonObject.getString("price") ,
                        jsonObject.getInt("check_carted_by_user")
                );
                cards.add(card);
            }

            final CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);

            CardListView listView = (CardListView) getActivity().findViewById(R.id.myCardList);
            listView.setAdapter(mCardArrayAdapter);

            EditText search_view = (EditText) getActivity().findViewById(R.id.search_filter);
            search_view.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    makeFilterCall(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            Log.i("ISSUE" , e.toString());
        }
    }

    public void processFailed(String s) {

    }

}
