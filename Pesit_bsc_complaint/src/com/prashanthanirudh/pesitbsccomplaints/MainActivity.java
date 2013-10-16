package com.prashanthanirudh.pesitbsccomplaints;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static String url_create_product = "http://192.168.1.38/sqladd.php";
	EditText tv_rn, tv_sname,tv_text;
	Spinner spin;
	String[] values = { "Geyser", "Electricity", "Carpentry", "Plumbing " };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tv_rn = (EditText) findViewById(R.id.rnin);
		tv_sname = (EditText) findViewById(R.id.snamein);
		tv_text = (EditText) findViewById(R.id.et1);
		Button b = (Button) findViewById(R.id.b1);
		Button b1 = (Button) findViewById(R.id.b2);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, values);
		spin = (Spinner) findViewById(R.id.spinner1);
		spin.setAdapter(adapter);

		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(check()==false)
				{
					Dialog d = new Dialog(MainActivity.this);
					d.setTitle("Error");
					TextView t = new TextView(MainActivity.this);
					t.setText("Enter Valid RoomNumber");
					d.setContentView(t);
					d.show();
				}
				else
				{
				Toast.makeText(getBaseContext(), "Sending", Toast.LENGTH_SHORT)
						.show();
				new CreateNewProduct().execute();
				}
			}
		});
		
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 tv_rn.setText(null);
				 tv_sname.setText(null);
				 tv_text.setText(null);
				 spin.setSelection(0);
			}
		});

	}
	
	private boolean check()
	{
		int n = Integer.parseInt(tv_rn.getText().toString());
		int x = n/100;
		if( n%100 <17 && n%100 >=0 && x>0 && x<10)
		{
			return true;
		}
		
		return false;	
	}

	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Creating Product..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String n1 = tv_rn.getText().toString();
			String n2 = tv_sname.getText().toString();
			String n4 = tv_text.getText().toString();
			String n3;
			int n = spin.getSelectedItemPosition();
			n3 = values[n];
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("no", n1));
			params.add(new BasicNameValuePair("name", n2));
			params.add(new BasicNameValuePair("table", n3));
			params.add(new BasicNameValuePair("text", n4));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);

			// check for success tag
			try {
				int success = json.getInt("success");
				if (success == 1) {
					// successfully created product

				} else {
					// failed to create product

				}
			} catch (JSONException e) {
				e.printStackTrace();

			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}

}
