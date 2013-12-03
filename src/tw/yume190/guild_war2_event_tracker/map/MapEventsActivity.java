package tw.yume190.guild_war2_event_tracker.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.yume190.guild_war2_event_tracker.R;
import tw.yume190.guild_war2_event_tracker.tool.HTTPSGetJson;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

// source : http://stackoverflow.com/questions/3028306/download-a-file-with-android-and-showing-the-progress-in-a-progressdialog

public class MapEventsActivity extends ListActivity {

	private static String url = "https://api.guildwars2.com/v1/events.json?world_id=1001";

	private static final String TAG_EVENT_ID = "event_id";
	private static final String TAG_EVENT_Name = "event_name";
	private static final String TAG_STATE = "state";

	private static final String TAG = "events";

	ArrayList<HashMap<String, String>> contactList;
	HTTPSGetJson httpsJson;
	
	// declare the dialog as a member field of your activity
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_events_listview);

		/*if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}*/
		
		Intent intent = getIntent();
		url = String.format("https://api.guildwars2.com/v1/events.json?world_id=%s&map_id=%s",
						intent.getStringExtra("world_id"),
						intent.getStringExtra("map_id"));

		// instantiate it within the onCreate method
		mProgressDialog = new ProgressDialog(MapEventsActivity.this);
		mProgressDialog.setMessage("Download ...");
		mProgressDialog.setIndeterminate(true);
		//mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);

		// execute this when the downloader must be fired
		final DownloadTask downloadTask = new DownloadTask(MapEventsActivity.this);
		downloadTask.execute(url);

		mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
		    @Override
		    public void onCancel(DialogInterface dialog) {
		        downloadTask.cancel(true);
		    }
		});
	}

	private class DownloadTask extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>>{
		
		Context context;
		
		public DownloadTask(Context ct) {
			context = ct;
		}
		

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
        	String url = params[0];
        	
        	contactList = new ArrayList<HashMap<String, String>>();
    		httpsJson = new HTTPSGetJson(url,mProgressDialog);
    		HashMap<String, String> eventHashMap = setGw2HashMap(loadJSONFromRaw());
    		
    		try {
    			JSONObject gw2JsonEventsObject = httpsJson.ProcessResponseObject();
    			JSONArray gw2JsonEventsArray = gw2JsonEventsObject.getJSONArray(TAG);

    			for (int i = 0; i < gw2JsonEventsArray.length(); i++) {
    				JSONObject gw2Event = gw2JsonEventsArray.getJSONObject(i);

    				String eventId = gw2Event.getString(TAG_EVENT_ID);
    				String eventName = eventHashMap.get(eventId);
    				String state = gw2Event.getString(TAG_STATE);

    				HashMap<String, String> hashMap = new HashMap<String, String>();

    				hashMap.put(TAG_EVENT_ID, eventId);
    				hashMap.put(TAG_EVENT_Name, eventName);
    				hashMap.put(TAG_STATE, state);

    				contactList.add(hashMap);
    			}
    		} catch (Exception e) {
    			// Log.v("Exception JSON","Exception:" + e.getMessage());
    			//((TextView) findViewById(R.id.output)).setText(e.toString());
    		}
            
            return contactList;            
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
            mProgressDialog.dismiss();
//            if (result != null)
//                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
//            else
//                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
            
    		ListAdapter adapter = new SimpleAdapter(context, contactList,
				R.layout.map_events_list,
				new String[] { TAG_EVENT_Name, TAG_EVENT_ID, TAG_STATE }, 
				new int[] { R.id.event_name, R.id.event_id, R.id.state });
    		setListAdapter(adapter);
            
        }
        
    }
	
	public String loadJSONFromRaw() {
        String json = null;
        try {
        	InputStream is = this.getResources().openRawResource(R.raw.event);
            //InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
	
	public HashMap<String, String> setGw2HashMap(String stream){
    	try {
    		JSONArray jsonArray = new JSONArray(stream);
    		HashMap<String, String> hashMap = new HashMap<String, String>();
			for(int i = 0; i < jsonArray.length();i++){
        		JSONObject gw2Event = jsonArray.getJSONObject(i);
        		
        		String id = gw2Event.getString("id");
        		String name = gw2Event.getString("name");
                
                hashMap.put(id, name);
        	}
			return hashMap;
		}catch (Exception e) {
            //Log.v("Exception JSON","Exception:" + e.getMessage());
			return null;
        }
    }
}
