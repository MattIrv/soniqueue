package mattirv.soniqueue;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class NowPlaying extends Activity {

    String partyName, email;
    int partyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            partyId = (Integer) extras.get("PARTY_ID");
            partyName = (String) extras.get("PARTY_NAME");
            email = (String) extras.get("EMAIL");
        }
        TextView partyNameText = (TextView) findViewById(R.id.textview_party_name);
        partyNameText.setText("Now Playing in " + partyName);

        ListView listview = (ListView) findViewById(R.id.listView);
        ArrayList<Song> songList = new ArrayList<Song>();
        //TODO: Don't use mock data
        Song song1 = new Song("", "Everytime We Touch", "Cascada", "Some Cascada Album", "mjk6zt");
        Song song2 = new Song("", "Crash Into Me", "Dave Matthews Band", "Crash", "mji7wb");
        Song song3 = new Song("", "Lips of an Angel", "Hinder", "idk", "mjk6zt");
        songList.add(song1);
        songList.add(song2);
        songList.add(song3);
        SongViewAdapter adapter = new SongViewAdapter(this, songList);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.now_playing, menu);
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
