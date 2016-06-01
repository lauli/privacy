package at.fhooe.mc.android;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class client_lobby extends Activity implements View.OnClickListener{

    private ArrayList<String> listItems = new ArrayList<String>();
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_lobby);


        list = null;
        list = (ListView) findViewById(R.id.client_lobby_players_list);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listItems));
        addItem();
        addItem();
        addItem();
        addItem();

        Button b = null;
        b = (Button) findViewById(R.id.client_lobby_continue);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, client_register.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void addItem(){
        listItems.add("playername");
        list.invalidate();
    }
}

