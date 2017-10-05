package edu.cmu.pocketsphinx.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by bhumihar on 5/10/17.
 */

public class Test_Activity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        String str1 = getIntent().getExtras().getString("test");
        TextView tv = (TextView)findViewById(R.id.test_text);
        tv.setText(str1);
    }
}
