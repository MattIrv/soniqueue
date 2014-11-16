package mattirv.soniqueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyQueue extends Activity {

    String partyName, email;
    int partyId;

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
        queueNameText.setText(email.split("@")[0]+"'s Queue");

        Button addsong = (Button) findViewById(R.id.button_add_song);
        addsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        //TODO: MJK6ZT: nth needs to be changed to actual queue position

        TextView queuePositionText = (TextView) findViewById(R.id.textview_party_info);
        queuePositionText.setText("nth in "+partyName);

        //TODO: MJK6ZT: how can we put something more complicated than strings into the list view?

        ListView listview = (ListView) findViewById(R.id.listView);
        ArrayList<Song> songList = new ArrayList<Song>();
        Song song1 = new Song("", "Everytime We Touch", "Cascada", "Some Cascada Album", "mji7wb");
        Song song2 = new Song("", "Crash Into Me", "Dave Matthews Band", "Crash", "mji7wb");
        Song song3 = new Song("", "Lips of an Angel", "Hinder", "idk", "mji7wb");
        songList.add(song1);
        songList.add(song2);
        songList.add(song3);


        SongViewButtonAdapter adapter = new SongViewButtonAdapter(this, songList);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

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
                //TODO: Send API call and advance to next screen
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
}
