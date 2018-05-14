package android.lehman.thegameadvisor;


        import android.content.Context;
        import android.database.DatabaseErrorHandler;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Owner on 5/9/2015.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    static int databaseVersion=1;
    static String databaseName="GameAdvisor";
    static String tableName="GameAdvisor";
    static String id="id";
    static String title="title";
    static String advise="advise";

    static String typeId="INTEGER";
    static String typeTitle="TEXT";
    static String typeAdvise="TEXT";

    static String queryCreateDB="Create database if  not exists "+databaseName;
    static String queryCreateTable="Create table if not exists "+tableName+"("+id+" "+typeId+"primary key,"+title+" "+typeTitle+","+advise+" "+typeAdvise+")";
    static String queryDropTable="Drop table if exists "+tableName;



    public DatabaseHelper(Context context)
    {
        super(context, databaseName, null, databaseVersion);
        // getWritableDatabase().execSQL(queryDropTable);
        // getWritableDatabase().execSQL(queryCreateTable);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //   db.execSQL(queryCreateDB);
        //  db.execSQL(queryDropTable);
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(queryDropTable);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db,oldVersion, newVersion);
    }
}
