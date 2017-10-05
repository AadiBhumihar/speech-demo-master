package edu.cmu.pocketsphinx.demo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by bhumihar on 5/10/17.
 */

public class Menu extends ListActivity {

    String[] Menu = new String[] {"PocketSphinxActivity","Speech"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> list = new ArrayAdapter<String>(Menu.this ,android.R.layout.simple_list_item_1 ,Menu);
        setListAdapter(list);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String clas = Menu[position];

        try {
            Class New_Class = Class.forName("edu.cmu.pocketsphinx.demo."+clas);
            Intent New_Activity = new Intent(Menu.this ,New_Class);
            startActivity(New_Activity);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
