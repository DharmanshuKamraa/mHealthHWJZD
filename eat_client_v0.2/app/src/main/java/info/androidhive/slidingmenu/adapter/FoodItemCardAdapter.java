package info.androidhive.slidingmenu.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;

/**
 * Created by Dharmanshu on 3/4/15.
 */
class FoodItemCardAdapter extends CardArrayAdapter implements Filterable{
    public FoodItemCardAdapter(Context c , List<Card> cards) {
        super(c , cards);
    }

//    public Filter getFilter() {
//
//    }
//
//    private class ValueFilter extends Filter() {
//
//    }
}
