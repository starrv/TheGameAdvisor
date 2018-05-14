package android.lehman.thegameadvisor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends Activity
{
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    ArrayList<String> list=new ArrayList<String>();
    ListView listView;
    final static String emeraldTitle="How do I defeat Emerald Weapon from ff7";
    final static String rubyTitle="How do I defeat Ruby Weapon from ff7";
    final static String emeraldLink="http://www.wikihow.com/Defeat-Emerald-Weapon-in-Final-Fantasy-7";
    final static String rubyLink="http://www.cavesofnarshe.com/ff7/weaponguide.php#ruby";
    final static String zeldaBossTitle="How do I make boss appear in Gargoyles dungeon in legend of Zelda a link to the past?";
    final static String missTickyTitle="How do I beat Miss Ticky from Diddy Kong Racing to the finish line";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        databaseHelper=new DatabaseHelper(this.getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        Task task=new Task();
        task.execute();
        listView=(ListView)findViewById(R.id.list);
        ArrayAdapter adapter=new ArrayAdapter(this,R.layout.text,list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add)
        {
            Intent intent=new Intent(this,Add.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.delete)
        {
            Intent intent = new Intent(this, Delete.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Task extends AsyncTask
    {
        public void select()
        {
            if(databaseHelper==null)
            {
                databaseHelper=new DatabaseHelper(getApplicationContext());
                db = databaseHelper.getReadableDatabase();
            }
// Define a projection that specifies which columns from the database
// you will actually use after this query.
            String[] projection =
                    {
                            DatabaseHelper.title,
                            DatabaseHelper.advise
                    };

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    DatabaseHelper.id + " ASC";

            Cursor cursor = db.query(
                    DatabaseHelper.tableName,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            if(cursor.getCount()<=0)
            {
                list.add("no game advise to view.  Please check again later for updates.");
                Log.e("MainActivity", "nothing found");
                return;
            }
            Boolean foundTitleEmeraldWeapon=false;
            Boolean foundTitleRubyWeapon=false;
            String title;
            String advise;
            cursor.moveToFirst();
            title=cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.title));
            if(title.equalsIgnoreCase(emeraldTitle))foundTitleEmeraldWeapon=true;
            if(title.equalsIgnoreCase(rubyTitle))foundTitleRubyWeapon=true;
            advise=cursor.getString(
                    cursor.getColumnIndexOrThrow(DatabaseHelper.advise));
            list.add(title+"?");
            list.add(advise);
            list.add("");
            for(int i=0;i<cursor.getCount()-1;i++)
            {
                cursor.moveToNext();
                title=cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.title));
                if(title.equalsIgnoreCase(emeraldTitle))foundTitleEmeraldWeapon=true;
                if(title.equalsIgnoreCase(rubyTitle))foundTitleRubyWeapon=true;
                advise=cursor.getString(
                        cursor.getColumnIndexOrThrow(DatabaseHelper.advise));
                list.add(title+"?");
                list.add(advise);
                list.add("");
            }
            Document doc=null;
            try {
                doc = Jsoup.connect(emeraldLink).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String content="";
            Elements elements=doc.getElementsByTag("p");
            content+=elements.first().text()+"\n"+"\n";
            elements=doc.getElementsByClass("step");
            for (Element element : elements)
            {
                content += element.text() + "\n" + "\n";
            }
            if(foundTitleEmeraldWeapon)
            {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.advise, content);

// Which row to update, based on the ID
                String selection = DatabaseHelper.title + " = ?";
                String[] selectionArgs = { String.valueOf(emeraldTitle) };

                int count = db.update(
                        DatabaseHelper.tableName,
                        values,
                        selection,
                        selectionArgs);
            }
            else
            {
                // Gets the data repository in write mode
                db =databaseHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.title,emeraldTitle );
                values.put(DatabaseHelper.advise, content);

// Insert the new row, returning the primary key value of the new row
                long newRowId;
                newRowId = db.insert(
                        DatabaseHelper.tableName,
                        DatabaseHelper.advise,
                        values);
            }
            try {
                doc = Jsoup.connect(rubyLink).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            content="";
            elements=doc.getElementsByTag("p");
            for (Element element : elements)
            {
                content += element.text() + "\n" + "\n";
            }
            if(foundTitleRubyWeapon)
            {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.advise, content);

// Which row to update, based on the ID
                String selection = DatabaseHelper.title + " = ?";
                String[] selectionArgs = { String.valueOf(rubyTitle) };

                int count = db.update(
                        DatabaseHelper.tableName,
                        values,
                        selection,
                        selectionArgs);
            }
            else
            {
                // Gets the data repository in write mode
                db =databaseHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.title,rubyTitle );
                values.put(DatabaseHelper.advise, content);

// Insert the new row, returning the primary key value of the new row
                long newRowId;
                newRowId = db.insert(
                        DatabaseHelper.tableName,
                        DatabaseHelper.advise,
                        values);
            }
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            select();
            return null;
        }
    }
}
