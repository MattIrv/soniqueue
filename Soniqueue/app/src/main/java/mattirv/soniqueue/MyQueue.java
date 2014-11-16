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

        Thread thread3 = new Thread(new Runnable() {
            public void run() {
                MakeRequest.nowPlayingFromMyQueue(context);
            }
        });
        thread3.start();

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

    public void setNowPlaying(Song nowPlaying){
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
            queuePositionText.setText("You aren't in the queue yet in " + partyName);

    }
}
