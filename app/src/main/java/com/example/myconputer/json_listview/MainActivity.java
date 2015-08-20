package com.example.myconputer.json_listview;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;

    // private static String url = "https://api.foursquare.com/v2/venues/search?intent=global&ll=16.0677948,108.2207947&query=da+nang&oauth_token=ESP5PMNAAH4ZCSMGYWCYHC52L0NDL21A3LHHUIEJY3ML2F5T&v=20141127";

    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_CITY = "city";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_COUNTRY = "country";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";

    // contacts JSONArray
    JSONArray contacts = null;
    JSONObject jsonobject;
    ListView lv;
    private static String url;
    private static String url1 = "https://api.foursquare.com/v2/venues/search?intent=global&ll=16.0677948,108.2207947&query=";
    private static String url3 = "&oauth_token=ESP5PMNAAH4ZCSMGYWCYHC52L0NDL21A3LHHUIEJY3ML2F5T&v=20141127";
    private static String url2;
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView img = (TextView) findViewById(R.id.imgseach);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactList = new ArrayList<HashMap<String, String>>();
                EditText edtSearch = (EditText) findViewById(R.id.edtsearch);
                url2 = edtSearch.getText().toString();
                url = url1 + Uri.encode(url2) + url3;
                new GetContacts().execute();
            }
        });

        ListView lv = getListView();

        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.txtname))
                        .getText().toString();
                String city = ((TextView) view.findViewById(R.id.txtmail))
                        .getText().toString();
                String country = ((TextView) view.findViewById(R.id.txtmobile))
                        .getText().toString();

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SingleContactActivity.class);
                in.putExtra(TAG_NAME, name);
                in.putExtra(TAG_COUNTRY, city);
                in.putExtra(TAG_CITY, country);
                startActivity(in);

            }
        });

        // Calling async task to get json
        //new GetContacts().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    // JSONArray jsonArray = new JSONArray(jsonObject);

                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject jsonObj2 = jsonObj.getJSONObject("response");

                    // Getting JSON Array node
                    contacts = jsonObj2.getJSONArray("venues");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        try {
                            JSONObject c = contacts.getJSONObject(i);

                            String id = c.getString("id");
                            String name = c.getString("name");
//
                            // Phone node is JSON Object
                            JSONObject location = c.getJSONObject("location");
                            String address = location.getString("address");

                            String city = location.getString("city");
                            String country = location.getString("country");
                            city = city + " " + country;
                            // tmp hashmap for single contact
                            HashMap<String, String> contact = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            contact.put(TAG_ID, id);
                            contact.put(TAG_NAME, name);
                            contact.put(TAG_CITY, city);
                            contact.put(TAG_ADDRESS, address);
                            contact.put(TAG_COUNTRY, country);

                            // adding contact to contact list
                            contactList.add(contact);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[]{TAG_NAME, TAG_CITY,
                    TAG_COUNTRY}, new int[]{R.id.txtname,
                    R.id.txtmail, R.id.txtmobile});

            setListAdapter(adapter);
        }

    }

}