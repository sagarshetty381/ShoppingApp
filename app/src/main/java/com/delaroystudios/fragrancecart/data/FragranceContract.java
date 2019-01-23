package com.delaroystudios.fragrancecart.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sagar G Shetty on 02-09-2017.
 */

public class FragranceContract {

    private FragranceContract() {}


    public static final String CONTENT_AUTHORITY = "com.delaroystudios.fragrancecart";


    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final String PATH_FRAGRANCE = "fragrance-path";

    public static final String PATH_CART= "cart-path";



    public static final class FragranceEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FRAGRANCE);

        public static final Uri CONTENT_URI_CART = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CART);


        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRAGRANCE;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FRAGRANCE;

         public final static String TABLE_NAME = "fragrances";

         public final static String CART_TABLE = "cart";


        public final static String _ID = BaseColumns._ID;

        public final static String _CARTID = BaseColumns._ID;


        public final static String COLUMN_NAME = "fragrancename";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_IMAGE = "imageurl";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_USERRATING = "userrating";


        public final static String COLUMN_CART_NAME = "cartfragrancename";
        public final static String COLUMN_CART_IMAGE = "cartimageurl";
        public final static String COLUMN_CART_QUANTITY = "cartquantity";
        public final static String COLUMN_CART_TOTAL_PRICE = "carttotalprice";

    }

}

