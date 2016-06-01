package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout b = null;
        b = (FrameLayout) findViewById(R.id.main_start);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, create_or_join.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
