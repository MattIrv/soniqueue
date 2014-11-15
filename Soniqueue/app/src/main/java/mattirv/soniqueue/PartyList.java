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

import java.util.ArrayList;


public class PartyList extends Activity {

    String email = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = (String)extras.get("EMAIL");
        }
        ListView listview = (ListView) findViewById(R.id.listView);
        ArrayList<String> myStringArray1 = new ArrayList<String>();
        myStringArray1.add("Placeholder Party 1");
        myStringArray1.add("Placeholder Party 2");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray1);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                //TODO: Work with lists. For now we just treat the index as the party id.
                Intent intent = new Intent(getBaseContext(), PartyScreen.class);
                intent.putExtra("EMAIL", email);
                intent.putExtra("PARTY_NAME", (String)(adapterView.getItemAtPosition(index)));
                intent.putExtra("PARTY_ID", 5);
                intent.putExtra("IS_LEADER", false);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.party_list, menu);
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