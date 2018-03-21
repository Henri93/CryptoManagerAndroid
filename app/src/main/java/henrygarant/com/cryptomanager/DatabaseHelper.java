package henrygarant.com.cryptomanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by henry on 3/20/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String TAG = "DatabaseHelper";
    public static final String TABLE_NAME = "liked_assets";
    public static final String COL1 = "tkr";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL1 + " TEXT)";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String upgrade = "DROP IF TABLE EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(upgrade);
    }

    public boolean addData(String tkr){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, tkr);

        Log.d(TAG, "Liked " + tkr);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }

        return true;
    }

    public boolean checkIfLiked(String tkr) {
        SQLiteDatabase sqldb = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COL1 + " = '" + tkr +"'";
        Cursor cursor = sqldb.rawQuery(Query, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    public boolean removeLike(String tkr)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL1 + "='" + tkr+"'", null) > 0;
    }

}
