package mattirv.soniqueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MyQueue extends Activity {

    String partyName, email;
    int partyId;
    final MyQueue context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_queue);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            partyId = (Integer) extras.get("PARTY_ID");
            partyName = (String) extras.get("PARTY_NAME");
            email = (String) extras.get("EMAIL");
        }
        TextView queueNameText = (TextView) findViewById(R.id.textview_queue_name);
        queueNameText.setText(email.split("@")[0] + "'s Queue");

        Button addsong = (Button) findViewById(R.id.button_add_song);
        addsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        Button clearqueue = (Button) findViewById(R.id.button_clear_queue);
        clearqueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder d = new AlertDialog.Builder(context);
                TextView text = new TextView(context);
                text.setText("Are you sure you want to clear your queue?");
                d.setTitle("Confirm clear");
                d.setView(text);
                d.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Thread thread = new Thread(new Runnable() {
                            public void run() {
                                MakeRequest.clearQueue(MyUser.userId);
                            }
                        });
                        thread.start();
                        updateSongs(new ArrayList<Song>());
                    }
                });
                d.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled. No need to do anything.
                    }
                });
                d.show();

            }
        });

        Thread thread = new Thread(new Runnable() {
            public void run() {
                MakeRequest.getUserPosition(context, partyId, MyUser.userId);
            }
        });
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            public void run() {
                MakeRequest.getUserQueue(context, MyUser.userId);
            }
        });
        thread2.start();



    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                MakeRequest.getUserQueue(context, MyUser.userId);
            }
        });
        thread.start();
    }

    public void search() {
        final EditText input = new EditText(this);
        input.setText("");
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Add Song");
        d.setMessage("What do you want to add?");
        d.setView(input);
        d.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String searchterm = input.getText().toString();
                Intent intent = new Intent(getBaseContext(), SongSearch.class);
                intent.putExtra("SEARCHTERM", searchterm);
                intent.putExtra("PARTY_ID", partyId);
                intent.putExtra("PARTY_NAME", partyName);
                intent.putExtra("EMAIL", email);
                startActivity(intent);
            }
        });

        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled. No need to do anything.
            }
        });
        d.show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_queue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void updateSongs(List<Song> rgsong) {
        ListView listview = (ListView) findViewById(R.id.listView);
        SongViewButtonAdapter adapter = new SongViewButtonAdapter(this, rgsong);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void updateUserPosition(int user_pos) {
        TextView queuePositionText = (TextView) findViewById(R.id.textview_party_info);
        if(user_pos != -1)
            queuePositionText.setText("You are at spot " + (user_pos + 1) + " in " + partyName);
        else
            queuePositionText.setText("You're in " + partyName);

    }
}
