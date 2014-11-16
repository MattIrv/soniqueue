package mattirv.soniqueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import java.util.ArrayList;


public class SongSearch extends Activity {

    String searchterm;
    int partyId;
    String partyName;
    String email;

    final SongSearch context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            partyId = (Integer) extras.get("PARTY_ID");
            partyName = (String) extras.get("PARTY_NAME");
            searchterm = (String) extras.get("SEARCHTERM");
            email = (String) extras.get("EMAIL");
        }

        Thread thread = new Thread(new Runnable() {
            public void run() {
                MakeRequest.makeSearch(context, searchterm, email.split("@")[0]);
            }
        });
        thread.start();

        TextView searchText = (TextView) findViewById(R.id.textview_search_term);
        searchText.setText("Loading results for " + searchterm + "...");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.song_search, menu);
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

    public void updateSongs(final List<Song> songs) {
        TextView searchText = (TextView) findViewById(R.id.textview_search_term);
        searchText.setText("Showing results for " + searchterm);
        ListView listview = (ListView) findViewById(R.id.listView);
        SongViewAdapter adapter = new SongViewAdapter(this, songs, false);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                final String spotify_id = songs.get(index).spotify_id;
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        MakeRequest.addSong( MyUser.userId, spotify_id );
                    }
                });
                thread.start();
                Intent intent = new Intent(getBaseContext(), MyQueue.class);
                intent.putExtra("EMAIL", email);
                intent.putExtra("PARTY_NAME", partyName);
                intent.putExtra("PARTY_ID", partyId);
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();
    }
}
