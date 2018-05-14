package android.lehman.thegameadvisor;

/**
 * Created by Owner on 5/12/2015.
 */
import android.app.Activity;
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
 * Created by Owner on 5/9/2015.
 */
public class Delete extends Activity
{
    Button delete;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final Activity act=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.del);

        databaseHelper=new DatabaseHelper(getApplicationContext());
        db=databaseHelper.getWritableDatabase();
        delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
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
            Intent intent=new Intent(this,Add.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.delete)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Task extends AsyncTask
    {
        String title;
        long rows=0;
        public Task()
        {
            super();
            EditText data=(EditText)findViewById(R.id.title);
            StringBuilder name1=new StringBuilder(data.getText());
            this.title=name1.toString();
        }

        public void delete()
        {
            if(title.equalsIgnoreCase(""))
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        Toast.makeText(getApplicationContext(), "Deletion failed.  Please check your entries, must supply valid title.", Toast.LENGTH_LONG).show();
                    }
                });
                return;
            }
            if(title.equalsIgnoreCase(MainActivity.rubyTitle) || title.equalsIgnoreCase(MainActivity.emeraldTitle)
                    || title.equalsIgnoreCase(MainActivity.missTickyTitle) || title.equalsIgnoreCase(MainActivity.zeldaBossTitle))
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "This is a reserved title.  Can't delete", Toast.LENGTH_LONG).show();
                    }
                });
                return;
            }
            if(databaseHelper==null)
            {
                databaseHelper=new DatabaseHelper(getApplicationContext());
                db = databaseHelper.getWritableDatabase();
            }
            String table_name=DatabaseHelper.tableName;
            // Define 'where' part of query.
            String selection = DatabaseHelper.title+ " = ?";
// Specify arguments in placeholder order.
            String[] selectionArgs = { String.valueOf(title) };
// Issue SQL statement.
            rows=db.delete(table_name, selection, selectionArgs);
            if (rows>0)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), title+ " deleted", Toast.LENGTH_LONG).show();
                        rows = 0;
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
            else
            {
                if (!title.equalsIgnoreCase(""))
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(), "Deletion failed.  Please check your entries, must supply valid title.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            delete();
            return null;
        }
    }
}
