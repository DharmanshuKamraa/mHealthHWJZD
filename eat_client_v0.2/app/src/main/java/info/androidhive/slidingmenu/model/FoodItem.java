package info.androidhive.slidingmenu.model;

import android.database.Cursor;

/**
 * Created by Dharmanshu on 2/21/15.
 */
public class FoodItem {
    private long id;

    private String title;
    private String price;
    private String picture_url;
    private String seller;
    private int added_to_cart = 0;

    public FoodItem(long id , String title, String price , String picture_url , String seller) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.picture_url = picture_url;
        this.seller = seller;
    }

    public String getTitle() {return this.title;}

    public long getId() {
        return this.id;
    }
}
