package at.fhooe.mc.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class create_or_join extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_or_join);
        Button b = null;
        b = (Button) findViewById(R.id.create_or_join_create);
        b.setOnClickListener(this);
        b = (Button) findViewById(R.id.create_or_join_join);
        b.setOnClickListener(this);
    }

    @Override
    public void onClick(View _v) {
        switch(_v.getId()){
            case R.id.create_or_join_create : {
//                Intent i = new Intent(this, host_register.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
            }break;
            case R.id.create_or_join_join : {
//                Intent i = new Intent(this, host_register.class);
                Intent i = new Intent(this, client_register.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }break;
            default :
        }

    }
}
