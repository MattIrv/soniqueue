package mattirv.soniqueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainMenu extends Activity {

    String email = null;
    String displayname = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            // Unsafe
            email = (String)extras.get("EMAIL");
            displayname = email.split("@")[0];
            TextView emailText = (TextView) findViewById(R.id.text_loggedin);
            emailText.setText("Logged in as: " + displayname);
        }
        Button startParty = (Button) findViewById(R.id.btn_start_party);
        startParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startParty();
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

    public void startParty() {
        final EditText input = new EditText(this);
        input.setText(displayname + "'s Party");
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle("Set Name");
        d.setMessage("Choose your party's name.");
        d.setView(input);
        d.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String partyName = input.getText().toString();
                //TODO: Send API call and advance to next screen
                int id = 5; // We will get this from the api call
                Intent intent = new Intent(getBaseContext(), PartyScreen.class);
                intent.putExtra("EMAIL", email);
                intent.putExtra("PARTY_ID", id);
                intent.putExtra("PARTY_NAME", partyName);
                intent.putExtra("IS_LEADER", true);
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

    public void showPartyList() {
        Intent intent = new Intent(getBaseContext(), PartyList.class);
        intent.putExtra("EMAIL", email);
        startActivity(intent);
    }
}
