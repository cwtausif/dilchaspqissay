package com.glowingsoft.dilchaspqissay.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.glowingsoft.dilchaspqissay.model.CustomModel;

import java.util.ArrayList;

/**
 * Created by Tausif on 5/19/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "dilchasp";
    // Story table name
    private static final String TABLE_STORY = "story";

    //Detail Table name
    private static final String TABLE_DETAIL = "detail";

    // Story Table Columns names
    private String KEY_ID = "id";
    private String KEY_STORY_ID ="story_id";
    private String KEY_Title = "title";
    private String KEY_IMAGE = "image";
    private String KEY_CATEGORY = "category";

    //Detail Table Columns names
    private String KEY_DETAIL_ID="detail_id";
    private String KEY_CONTENT ="content";


    private String[] STORYCOLUMNS = {KEY_ID,KEY_STORY_ID,KEY_Title,KEY_IMAGE,KEY_CATEGORY};
    private String[] DETAILSCOLUMNS = {KEY_ID,KEY_DETAIL_ID,KEY_CONTENT,KEY_IMAGE,KEY_STORY_ID};

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("OnCreate DbHandler: ","oncreaete");
        // SQL statement to create story table
        String CREATE_STORY_TABLE = "CREATE TABLE IF NOT EXISTS story ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "story_id TEXT, "+
                "title TEXT, "+
                "image TEXT, "+
                "category INTEGER)";
        // create books table
        Log.d("story table created: ",CREATE_STORY_TABLE);
         db.execSQL(CREATE_STORY_TABLE);

        String CREATE_DETAIL_TABLE = "CREATE TABLE IF NOT EXISTS detail ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "detail_id TEXT, "+
                "content TEXT, "+
                "image TEXT, "+
                "story_id TEXT)";
        // create books table
        Log.d("detail table created: ",CREATE_DETAIL_TABLE);
        db.execSQL(CREATE_DETAIL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("OnUpgrade DbHandler: ","onupgrade");
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_STORY);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_DETAIL);
        onCreate(db);
    }

    public void addStory(CustomModel customModel){
        //for logging
         Log.d("addstory", customModel.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_STORY_ID,customModel.getTitle_id());
        values.put(KEY_Title,customModel.getTitle());
        values.put(KEY_CATEGORY,customModel.getCategory());
        // 3. insert
        db.insert(TABLE_STORY, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
    }


    public void adddetail(CustomModel customModel){
        //for logging
        Log.d("adddetail ", customModel.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_DETAIL_ID,customModel.getContent_id());
        values.put(KEY_CONTENT,customModel.getContent());
        values.put(KEY_STORY_ID,customModel.getTitle_id());
        // 3. insert
        db.insert(TABLE_DETAIL, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
        // 4. close
        db.close();
    }



    public ArrayList<CustomModel> getAllStories(int category) {
        ArrayList<CustomModel> stories = new ArrayList<>();

        // 1. build the query
       // String query = "SELECT  * FROM " + TABLE_STORY +" where category = '1'";

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery(query, null);

        Cursor cursor =
                db.query(TABLE_STORY, // a. table
                        STORYCOLUMNS, // b. column names
                        KEY_CATEGORY +" = ?", // c. selections
                        new String[] {String.valueOf(category)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        CustomModel customModel = null;
        if (cursor.moveToFirst()) {
            do {
                customModel = new CustomModel();
                customModel.setId(Integer.parseInt(cursor.getString(0)));
                customModel.setTitle_id(Integer.parseInt(cursor.getString(1)));
                customModel.setTitle(cursor.getString(2));
                customModel.setTitleimage(cursor.getString(3));
                customModel.setCategory(Integer.parseInt(cursor.getString(4)));

                // Add book to story title
                stories.add(customModel);
            } while (cursor.moveToNext());
        }

        Log.d("getAllStories(): ","Category: "+category+ stories.toString());

        // return story title
        return stories;
    }

    public CustomModel getStoryContent(int story_id) {
        CustomModel customModel = new CustomModel();
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_DETAIL, // a. table
                        DETAILSCOLUMNS, // b. column names
                        " story_id = ?", // c. selections
                        new String[] { String.valueOf(story_id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            customModel.setContent(cursor.getString(2));
        }
        // 4. build namaz object

        return customModel;
    }

}
