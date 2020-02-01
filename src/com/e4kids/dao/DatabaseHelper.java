package com.e4kids.dao;



import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
	//logcat tag
	private static final String LOG = "DatabaseHelper";
	//database version
	private static final int DATABASE_VERSION = 1;
	// Database Name
    private static final String DATABASE_NAME = "e4kidsDatabase";
 
    // Table Names
    public static final String TABLE_TOPIC = "topic";
    public static final String TABLE_WORD = "word";
    public static final String TABLE_LINK = "link";
 
    // word column names
    public static final String COLUMN_TOPIC_ID = "maTp";
    public static final String COLUMN_TOPIC_NAME= "topic";
 
    // word - column nmaes
    public static final String COLUMN_WORD_ID = "maW";
    public static final String COLUMN_WORD = "word";
 
    // link Table - column names
    public static final String COLUMN_LINK_ID = "maL";
    public static final String COLUMN_LINK = "url";
    public static final String COLUMN_LINK_CONTENT = "content";
    public static final String COLUMN_LINK_TYPE ="type";
 
    // Table Create Statements
    // Topic table create statement
    private static final String CREATE_TABLE_TOPIC = "CREATE TABLE " + TABLE_TOPIC + "(" 
    		+ COLUMN_TOPIC_ID + " INTEGER PRIMARY KEY ,"
            + COLUMN_TOPIC_NAME  + " TEXT NOT NULL "
    		+ ");";
 
    // word table create statement
    private static final String CREATE_TABLE_WORD = "CREATE TABLE " + TABLE_WORD + "(" 
            + COLUMN_WORD_ID + " INTEGER PRIMARY KEY ," 
    		+ COLUMN_TOPIC_ID+" INTEGER, "
            + COLUMN_WORD+" TEXT NOT NULL " 
    		+ ");";
 
    // link table create statement
    private static final String CREATE_TABLE_LINK = "CREATE TABLE " + TABLE_LINK+ "(" 
            + COLUMN_LINK_ID + " INTEGER PRIMARY KEY ,"
            + COLUMN_LINK + " TEXT NOT NULL ," 
            + COLUMN_LINK_CONTENT + " TEXT NOT NULL ," 
            + COLUMN_LINK_TYPE + " INTEGER "
            + ");";
 	
    
    public static final String CREATE_TABLE_WORDINFO = "create table "
			+ WordInfoDB.DATABASE_TABLE + " (" + WordInfoDB.ROW_ID
			+ " integer primary key autoincrement, " + WordInfoDB.UNLOCKED
			+ " INTEGER," + WordInfoDB.SOLVED + " INTEGER," + WordInfoDB.SCORE
			+ " INTEGER," + WordInfoDB.WORD + " TEXT," + WordInfoDB.LETTERS
			+ " TEXT," + WordInfoDB.IMAGE + " TEXT," + WordInfoDB.SUGGESTION
			+ " TEXT);";
    
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_TOPIC);
		db.execSQL(CREATE_TABLE_WORD);
		db.execSQL(CREATE_TABLE_LINK);
		db.execSQL(CREATE_TABLE_WORDINFO);
		initTopicDatabase(db);
		initWordDatabase(db);
		initLinkDatabase(db);
		initWordInfoDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPIC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LINK);
        db.execSQL("DROP TABLE IF EXISTS " + WordInfoDB.DATABASE_TABLE);
 
        // create new tables
        onCreate(db);
	}
	// closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public void initTopicDatabase(SQLiteDatabase db){   	

    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(0,\"alphabet\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(1,\"numbers\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(2,\"animals\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(3,\"fruits\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(4,\"colors\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(5,\"food\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(6,\"transport\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(7,\"vegetatbles\");");
    	db.execSQL("INSERT INTO " + TABLE_TOPIC+ " values(8,\"weathers\");");
    
    }
    
    public void initWordDatabase(SQLiteDatabase db){
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(0,3,\"banana\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(1,3,\"apple\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(2,3,\"mango\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(3,3,\"orange\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(4,3,\"papaya\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(5,3,\"avocado\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(6,3,\"rumbutan\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(7,3,\"grape\");");
    	db.execSQL("INSERT INTO " + TABLE_WORD+ " values(8,3,\"pear\");");
    }
    public void initLinkDatabase(SQLiteDatabase db){
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(0,\"AA5hOCxlRaI\",\"Good morning. How are you?\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(1,\"zqAcFdsmtbk\",\"Good afternoon. Nice to meet you\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(2,\"eUazfCkQq84\",\"What's this? What's that?\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(3,\"XmR7IYeBe5g\",\"Who is he? - Who is she?\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(4,\"nsqfrAF7OLI\",\"Whose bike is this? It's mine\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(5,\"w9DxgFO5i3M\",\"Happy birthday! Can you swim?\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(6,\"yWaVltUyDZM\",\"How old are you? I'm five years old\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(7,\"xYGlhOR86mM\",\"I like soccer. Let's play soccer\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(8,\"S6pRqjxD-fQ\",\"Do you like cheese? Yes or No\",1);");
    	db.execSQL("INSERT INTO " + TABLE_LINK+ " values(9,\"UFy02dqJCUs\",\"Do you have crayon? Do you have paper?\",1);");
    }

    public void initWordInfoDatabase(SQLiteDatabase db){
    	db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(1,1,0,0,\"CAT\",\"CUTODKEADUECSKD\",\"pics/cat.jpg\",\"ANIMAL\");");
    	db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(2,1,0,0,'DOG','GIEUCOLKVNEEDSD','pics/dog.jpg','ANIMAL');");
    	//db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(3,\"XmR7IYeBe5g\",\"Who is he? - Who is she?\",1);");
    	//db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(4,\"nsqfrAF7OLI\",\"Whose bike is this? It's mine\",1);");
    	//db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(5,\"w9DxgFO5i3M\",\"Happy birthday! Can you swim?\",1);");
    	//db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(6,\"yWaVltUyDZM\",\"How old are you? I'm five years old\",1);");
    	//db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(7,\"xYGlhOR86mM\",\"I like soccer. Let's play soccer\",1);");
    	//db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(8,\"S6pRqjxD-fQ\",\"Do you like cheese? Yes or No\",1);");
    	//db.execSQL("INSERT INTO " + WordInfoDB.DATABASE_TABLE+ " values(9,\"UFy02dqJCUs\",\"Do you have crayon? Do you have paper?\",1);");
    }
}


