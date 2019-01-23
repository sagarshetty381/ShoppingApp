package com.delaroystudios.fragrancecart.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Sagar G Shetty on 02-09-2017.
 */

public class FragranceProvider extends ContentProvider {

    public static final String LOG_TAG = FragranceProvider.class.getSimpleName();

    /** URI matcher code for the content URI for the fragrance table */
    private static final int FRAGRANCES = 100;

    /** URI matcher code for the content URI for a single fragrance in the fragrance table */
    private static final int FRAGRANCE_ID = 101;

    /** URI matcher code for the content URI for the cart table */
    private static final int CART = 102;

    /** URI matcher code for the content URI for a single cart in the cart table */
    private static final int CART_ID = 103;

     private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        static {
         sUriMatcher.addURI(FragranceContract.CONTENT_AUTHORITY, FragranceContract.PATH_FRAGRANCE, FRAGRANCES);

        sUriMatcher.addURI(FragranceContract.CONTENT_AUTHORITY, FragranceContract.PATH_CART, CART);

        sUriMatcher.addURI(FragranceContract.CONTENT_AUTHORITY, FragranceContract.PATH_FRAGRANCE + "/#", FRAGRANCE_ID);
        sUriMatcher.addURI(FragranceContract.CONTENT_AUTHORITY, FragranceContract.PATH_CART + "/#", CART_ID);

    }

    private FragranceDbHelper fragranceDbHelper;

    @Override
    public boolean onCreate() {
        fragranceDbHelper = new FragranceDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = fragranceDbHelper.getReadableDatabase();

        Cursor cursor;

          int match = sUriMatcher.match(uri);
        switch (match) {
            case FRAGRANCES:
                cursor = database.query(FragranceContract.FragranceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CART:
                cursor = database.query(FragranceContract.FragranceEntry.CART_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CART_ID:
                selection = FragranceContract.FragranceEntry._CARTID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(FragranceContract.FragranceEntry.CART_TABLE, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case FRAGRANCE_ID:
                 selection = FragranceContract.FragranceEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                 cursor = database.query(FragranceContract.FragranceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FRAGRANCES:
                return insertCart(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertCart(Uri uri, ContentValues values) {

        SQLiteDatabase database = fragranceDbHelper.getWritableDatabase();

        long id = database.insert(FragranceContract.FragranceEntry.CART_TABLE, null, values);
          if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

          getContext().getContentResolver().notifyChange(uri, null);

         return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = fragranceDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int cartDeleted;

        switch (match) {
            case CART_ID:
                String id = uri.getPathSegments().get(1);

                cartDeleted = db.delete(FragranceContract.FragranceEntry.CART_TABLE, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (cartDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

         return cartDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
