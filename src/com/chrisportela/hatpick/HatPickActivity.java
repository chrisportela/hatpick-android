package com.chrisportela.hatpick;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HatPickActivity extends Activity {
	ArrayList<String> listItems = new ArrayList<String>();
	Button addItemBtn;
	Button pickItemBtn;
	ListView itemList;
	EditText itemEditText;
	ArrayAdapter<String> listAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		addItemBtn = (Button) findViewById(R.id.addItemBtn);
		pickItemBtn = (Button) findViewById(R.id.pickItemBtn);
		itemList = (ListView) findViewById(R.id.itemList);
		itemEditText = (EditText) findViewById(R.id.itemEditText);

		listAdapter = new ArrayAdapter<String>(HatPickActivity.this,
				android.R.layout.simple_list_item_1, listItems);
		listAdapter.setNotifyOnChange(true);
		itemList.setAdapter(listAdapter);

		addItemBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listItems.add(itemEditText.getText().toString());
				listAdapter.notifyDataSetChanged();
				/*
				 * Toast.makeText(getApplicationContext(), "Added Item: " +
				 * itemEditText.getText().toString(),
				 * Toast.LENGTH_SHORT).show();
				 */
				// ensure the text box is cleared afterwards
				itemEditText.setText("");
				// call refresh of list
			}
		});

		itemList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				final int pos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(HatPickActivity.this);
				builder.setMessage("Do you want to delete this item?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										HatPickActivity.this.listItems.remove(pos);
									}
								})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

		pickItemBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pickItem();
			}
		});

	}
	
	//I am really hoping this will keep the data safe when changing orientations. However, it would suck if now it didn't do anything. The super method should handle all of that though.
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);

		//checking orientation
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();

		} else if( newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

	public void pickItem() {
		int item = -1;
		item = (int) ((double) Math.random() * (double) listItems.size());
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(listItems.get(item))
				.setTitle("Picking out of the hat...")
				.setCancelable(false)
				.setPositiveButton("Alright",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								HatPickActivity.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
		listAdapter.notifyDataSetChanged();
	}
	
	//Override the method call for when there is a pause. I think that is what the system calls when it goes to switch the screen. I need to pass an internal intent with data for this app so it can maintain the data in the list on switch.
}
