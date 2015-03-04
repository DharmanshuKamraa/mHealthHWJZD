package info.androidhive.slidingmenu.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.androidhive.slidingmenu.R;
import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Created by Dharmanshu on 3/4/15.
 */
public class CustomCardExapander extends CardExpand {

    public CustomCardExapander (Context context) {
        super(context, R.layout.custom_inner_base_expand);
    }

    public void setupInnerViewElements(ViewGroup parent, View view) {

        if (view == null) return;

        //Retrieve TextView elements
//        TextView tx1 = (TextView) view.findViewById(R.id.carddemo_expand_text1);
//        TextView tx2 = (TextView) view.findViewById(R.id.carddemo_expand_text2);
//        TextView tx3 = (TextView) view.findViewById(R.id.carddemo_expand_text3);
//        TextView tx4 = (TextView) view.findViewById(R.id.carddemo_expand_text4);
//
//        //Set value in text views
//        if (tx1 != null) {
//            tx1.setText(getContext().getString(R.string.demo_expand_customtitle1));
//        }
//
//        if (tx2 != null) {
//            tx2.setText(getContext().getString(R.string.demo_expand_customtitle2));
//        }
    }
}
