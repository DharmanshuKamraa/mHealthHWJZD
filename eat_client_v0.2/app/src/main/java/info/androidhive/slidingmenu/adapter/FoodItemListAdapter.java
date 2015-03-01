package info.androidhive.slidingmenu.adapter;

import info.androidhive.slidingmenu.R;
import info.androidhive.slidingmenu.api.ServerConnect;
import info.androidhive.slidingmenu.interfaces.ApiAsyncResponse;
import info.androidhive.slidingmenu.model.FoodItem;
import info.androidhive.slidingmenu.model.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Dharmanshu on 2/21/15.
 */

public class FoodItemListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<FoodItem> foodItems;

    public FoodItemListAdapter(Context context , ArrayList<FoodItem> foodItems) {
        this.context = context;
        this.foodItems = foodItems;
    }

    public Object getItem(int position) {
        return foodItems.get(position);
    }

    public long getItemId(int position) {
        return foodItems.get(position).getId();
    }

    public int getCount() {
        return foodItems.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)
        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.food_item, null);

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView label = (TextView) convertView.findViewById(R.id.label);
        imgIcon.setImageResource(R.drawable.ic_launcher);
        label.setText(foodItems.get(position).getTitle());

        Button btn = (Button) convertView.findViewById(R.id.btn_add_to_cart);
        btn.setOnClickListener(new FoodItemAddToCartListener(foodItems.get(position).getId() , btn , context));

        return convertView;
    }

    public class FoodItemAddToCartListener implements OnClickListener,ApiAsyncResponse{
        long itemId;
        Button btn;
        Context c;

        public FoodItemAddToCartListener(long id , Button btn , Context c) {
            this.itemId = id;
            this.btn = btn;
            this.c = c;
        }

        public FoodItemAddToCartListener(long id) {
           this.itemId = id;
        }

        public void onClick(View v) {
//            Log.i("TAG" , Objects.toString(itemId));
            ServerConnect s = new ServerConnect();
            s.delegate = this;
            s.activity = (Activity) context;
            s.execute("POST" , "mark_item_carted/" + Long.toString(itemId) , "");
        }

        public void processFinished(String s) {
            this.btn.setText("CARTED");
            this.btn.setBackgroundResource(R.color.app_green);
        }

        public void processFailed(String s) {

        }
    }
}
