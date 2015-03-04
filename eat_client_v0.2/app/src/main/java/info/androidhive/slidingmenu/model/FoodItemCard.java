package info.androidhive.slidingmenu.model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.slidingmenu.R;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.ViewToClickToExpand;

/**
 * Created by Dharmanshu on 3/3/15.
 */
public class FoodItemCard extends Card {

    protected String mTitle;
    protected String mPrice;
    protected String mSeller;
    protected Long mId;
    protected Boolean mIsCarted;
    protected Boolean mIsExpanded = false;

    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public FoodItemCard(Context context) {
        this(context, R.layout.food_item_card);
    }

    public void setParams(Long id, String title , String seller , String price , int isCarted) {
        Log.i("carted" , Integer.toString(isCarted));

        this.mId = id;
        this.mTitle = title;
        this.mSeller= seller;
        this.mPrice = price;

        if (isCarted == 0) {
            this.mIsCarted = false;
        } else {
            this.mIsCarted = true;
        }
        Log.i("carted" , Boolean.toString(mIsCarted));
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public FoodItemCard(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    /**
     * Init
     */
    private void init(){

        //No Header

        //Set a OnClickListener listener
//        setOnClickListener(new OnCardClickListener() {
//            @Override
//            public void onClick(Card card, View view) {
//                Toast.makeText(getContext(), "Click Listener card=" + mTitle, Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        ViewToClickToExpand viewToClickToExpand = ViewToClickToExpand.builder().setupView(view);
        this.setViewToClickToExpand(viewToClickToExpand);

        setShadow(true);
        setCardElevation(2);
        TextView sellerView = (TextView) parent.findViewById(R.id.seller);

        if (mTitle!=null)
            sellerView.setText("SELLER :- " + mSeller);

        TextView titleView = (TextView) parent.findViewById(R.id.title);
        titleView.setText(mTitle);

        Button buttonView = (Button) parent.findViewById(R.id.btn_add_to_cart);
        if (mIsCarted == true) {
            buttonView.setBackgroundResource(R.drawable.ok);
        } else {
            buttonView.setBackgroundResource(R.drawable.plus);
        }

        buttonView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsCarted == false) {
                    v.setBackgroundResource(R.drawable.ok);
                    mIsCarted = true;
                } else {
                    v.setBackgroundResource(R.drawable.plus);
                    mIsCarted = false;
                }
            }
        });

//
//        if (mSecondaryTitle!=null)
//            mSecondaryTitle.setText(R.string.demo_custom_card_googleinc);
//
//        if (mRatingBar!=null)
//            mRatingBar.setNumStars(5);
//        mRatingBar.setMax(5);
//        mRatingBar.setRating(4.7f);

    }
}
