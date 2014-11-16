package mattirv.soniqueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PartyScreen extends Activity {

    String email = null;
    int partyId = -1;
    String partyName = null;
    boolean isLeader = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_screen);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = (String)extras.get("EMAIL");
            partyId = (Integer)extras.get("PARTY_ID");
            partyName = (String)extras.get("PARTY_NAME");
            isLeader = (Boolean)extras.get("IS_LEADER");
        }
        TextView partyNameText = (TextView) findViewById(R.id.textview_party_name);
        partyNameText.setText(partyName);

        Button nowPlayingButton = (Button) findViewById(R.id.btn_now_playing);
        Button myQueueButton = (Button) findViewById(R.id.btn_my_queue);
        Button endButton = (Button) findViewById(R.id.btn_end_party);
        Button leaveButton = (Button) findViewById(R.id.btn_leave_party);
        if (isLeader) {
            leaveButton.setVisibility(View.GONE);
            endButton.setVisibility(View.VISIBLE);
        }
        else {
            endButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
        }

        //Bind the buttons to actions
        nowPlayingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNowPlaying();
            }
        });
        myQueueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyQueue();
            }
        });
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveParty();
            }
        });
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endParty();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.party_screen, menu);
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

    public void showNowPlaying() {
        //TODO: Make API call and transition to now playing view

        //Transition to now playing here
        Intent intent = new Intent(getBaseContext(), NowPlaying.class);
        intent.putExtra("PARTY_NAME", partyName);
        intent.putExtra("PARTY_ID", partyId);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }

    public void showMyQueue() {
        //TODO: Make API call and show own queue


        Intent intent = new Intent(getBaseContext(), MyQueue.class);
        intent.putExtra("PARTY_NAME", partyName);
        intent.putExtra("PARTY_ID", partyId);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }

    public void leaveParty() {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        TextView text = new TextView(this);
        text.setText("Are you sure you want to leave?");
        d.setTitle("Confirm Leaving");
        d.setView(text);
        d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //TODO: Make API call
                Intent intent = new Intent(getBaseContext(), MainMenu.class);
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

    public void endParty() {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        TextView text = new TextView(this);
        text.setText("Are you sure you want to end this party?");
        d.setTitle("Confirm Ending");
        d.setView(text);
        d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //TODO: Make API call
                Intent intent = new Intent(getBaseContext(), MainMenu.class);
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
}
