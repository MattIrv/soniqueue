package mattirv.soniqueue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NowPlaying extends Activity {

    String partyName, email;
    int partyId;

    final NowPlaying context = this;

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

        Thread thread = new Thread(new Runnable() {
            public void run() {
                MakeRequest.getPartyQueue(context);
            }
        });
        thread.start();

        Thread thread3 = new Thread(new Runnable() {
            public void run() {
                MakeRequest.nowPlayingFromMyQueue(context);
            }
        });
        thread3.start();
    }

    public void setNowPlaying(Song nowPlaying){

        if(nowPlaying == null){
            TextView songNameText = (TextView) findViewById(R.id.textview_song_name);
            songNameText.setText("No song is currently playing");
            return;
        }

        //Log.d("MyQueue", String.format("Song name: %s, Artist: %s, Album: %s, Queued By: %s", nowPlaying.songName, nowPlaying.artist, nowPlaying.album, nowPlaying.queuedBy));
        TextView songNameText = (TextView) findViewById(R.id.textview_song_name);
        songNameText.setText(nowPlaying.songName);
        TextView artistText = (TextView) findViewById(R.id.textview_artist_name);
        artistText.setText(nowPlaying.artist);
        TextView albumText = (TextView) findViewById(R.id.textview_album_name);
        albumText.setText(nowPlaying.album);
        TextView queuedByText = (TextView) findViewById(R.id.textview_queued_by);
        queuedByText.setText("Queued by " + nowPlaying.queuedBy);
        ImageView albumArtView = (ImageView) findViewById(R.id.imageview_album_art);
        if (nowPlaying.image != null) {
            albumArtView.setImageBitmap(nowPlaying.image);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
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

    public void updateSongs(List<Song> rgsong) {
        ListView listview = (ListView) findViewById(R.id.listView);
        SongViewAdapter adapter = new SongViewAdapter(this, rgsong, false);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
