package android.lehman.thegameadvisor;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Owner on 5/12/2015.
 */
public class Add extends Activity
{
    Button add;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Activity act=this;
        DatabaseHelper databaseHelper=new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        setContentView(R.layout.add);
        add=(Button)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Task task=new Task();
                task.execute();
            }
        });
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
        if (id == R.id.home)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.add)
        {
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

    // Create a new map of values, where column names are the keys
    private class Task extends AsyncTask
    {
        String title, advise;
        long rows=-1;
        public Task()
        {
            super();
            EditText data=(EditText)findViewById(R.id.title);
            StringBuilder name1=new StringBuilder(data.getText());
            this.title=name1.toString();
            data=(EditText)findViewById(R.id.advise);
            name1=new StringBuilder(data.getText());
            this.advise=name1.toString();
        }
        private boolean isNumeric(String str)
        {
            try
            {
                double d = Double.parseDouble(str);
            }
            catch(NumberFormatException nfe)
            {
                return false;
            }
            return true;
        }
        public void insert()
        {
            if (this.advise.equalsIgnoreCase("") || this.title.equalsIgnoreCase(""))
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "No data to insert.", Toast.LENGTH_LONG).show();
                    }
                });
                return;
            }
            if(databaseHelper==null)
            {
                databaseHelper=new DatabaseHelper(getApplicationContext());
                db = databaseHelper.getWritableDatabase();
            }
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.title, title);
            values.put(DatabaseHelper.advise, advise);

            // Insert the new row, returning the primary key value of the new row
            rows = db.insert(
                    DatabaseHelper.tableName,
                    null,
                    values);
            if (rows != -1)
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), title + " added.", Toast.LENGTH_LONG).show();
                        rows = -1;
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else
            {
                if (!title.equalsIgnoreCase("") || !advise.equalsIgnoreCase(""))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "Insertion failed.  Please check your entries, must supply valid email.  Or try again, or report error.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

        protected Object doInBackground(Object[] params)
        {
            insert();
            return null;
        }

    }
}
