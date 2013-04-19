package com.therealjoshua.essentialsloggersample1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);
		
		Button btn;
		
		for (int i=1; i<8; i++) {
			btn = new Button(this);
			btn.setText("Sample " + i);
			btn.setId(i);
			btn.setOnClickListener(clickListener);
			ll.addView(btn);
		}
	}
	
	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case 1: startActivity(new Intent(MainActivity.this, Example1.class)); break;
				case 2: startActivity(new Intent(MainActivity.this, Example2.class)); break;
				case 3: startActivity(new Intent(MainActivity.this, Example3.class)); break;
				case 4: startActivity(new Intent(MainActivity.this, Example4.class)); break;
				case 5: startActivity(new Intent(MainActivity.this, Example5.class)); break;
				case 6: startActivity(new Intent(MainActivity.this, Example6.class)); break;
				case 7: startActivity(new Intent(MainActivity.this, Example7.class)); break;
			}
		}
	};

}
