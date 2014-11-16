package mattirv.soniqueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;


public class MainMenu extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    static String email = null;
    static String displayname = null;
    static Player player = null;
    static Integer partyID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Unsafe
            if (extras.containsKey("EMAIL")) {
                email = (String)extras.get("EMAIL");
                displayname = email.split("@")[0];
                TextView emailText = (TextView) findViewById(R.id.text_loggedin);
                emailText.setText("Logged in as: " + displayname);
            }
        }
        Button startParty = (Button) findViewById(R.id.btn_start_party);
        startParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToSpotify();
            }
        });
        Button joinParty = (Button) findViewById(R.id.btn_join_party);
        joinParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPartyList();
            }
        });
        Button logout = (Button) findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), StartScreen.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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

    private static final String CLIENT_ID = "1196278c937a4daa8dc9d658d6d627fc";
    private static final String REDIRECT_URI = "soniqueue-redirect://callback";

    private Player mPlayer;

    public void loginToSpotify() {
        //SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI + "?email=" + email,
        //        new String[]{"streaming"}, null, this);
        Log.d("loginToSpotify", "Logging into Spotify...");
        SpotifyAuthentication.openAuthWindow(CLIENT_ID, "token", REDIRECT_URI,
                new String[]{"streaming"}, null, this);
    }

    public void startParty() {
        Log.d("MainMenu", "Starting a party");
        final EditText input = new EditText(this);
        input.setText(displayname + "'s Party");
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Set Name");
        d.setMessage("Choose your party's name.");
        d.setView(input);
        d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String partyName = input.getText().toString();
                Thread thread = new Thread (new Runnable() {
                    @Override
                    public void run() {
                        partyID = MakeRequest.createParty(MyUser.userId, partyName, "");
                        MakeRequest.joinParty(MyUser.userId, partyID);
                        Intent intent = new Intent(getBaseContext(), PartyScreen.class);
                        intent.putExtra("EMAIL", email);
                        intent.putExtra("PARTY_ID", partyID);
                        intent.putExtra("PARTY_NAME", partyName);
                        intent.putExtra("IS_LEADER", true);
                        startActivity(intent);
                        Log.d("MainMenu", "Trying to play next song");
                        MusicPlayer.getInstance().playNextSong();
                    }
                });
                thread.start();
            }
        });

        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled. No need to do anything.
            }
        });
        d.show();
    }

    public void showPartyList() {
        Intent intent = new Intent(getBaseContext(), PartyList.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            AuthenticationResponse response = SpotifyAuthentication.parseOauthResponse(uri);
            Spotify spotify = new Spotify(response.getAccessToken());
            mPlayer = spotify.getPlayer(this, "Soniqueue", this, new Player.InitializationObserver() {
                @Override
                public void onInitialized() {
                    Log.d("MainMenu", "Starting the Music Player service");
                    Intent intent1 = new Intent(getBaseContext(), MusicPlayer.class);
                    startService(intent1);
                    mPlayer.addConnectionStateCallback(MainMenu.this);
                    mPlayer.addPlayerNotificationCallback(MainMenu.this);
                    player = mPlayer;
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("Could not initialize player: " + throwable.getMessage());
                }
            });
            Log.d("MainMenu", "Calling startParty");
            startParty();
        }
    }

    @Override
    public void onLoggedIn() {

    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Throwable throwable) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onNewCredentials(String s) {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainMenu", "Playback event " + eventType.toString());
        if (eventType == EventType.TRACK_END) {
            Log.d("MainMenu", "Trying to play next song");
            MusicPlayer.nowPlaying = null;
            MusicPlayer.getInstance().playNextSong();
        }
        else
            Log.d("MainMenu", "Not trying to play next song");
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

    }

    @Override
    public void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }
}
