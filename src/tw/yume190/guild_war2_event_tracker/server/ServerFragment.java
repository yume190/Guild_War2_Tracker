package tw.yume190.guild_war2_event_tracker.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.yume190.guild_war2_event_tracker.R;
import tw.yume190.guild_war2_event_tracker.gw2.Gw2Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ServerFragment extends Fragment {
	
	private int regionStart;
	private int regionFinish;
	
	private void setRegion(){
		int region = getArguments().getInt("region");
		regionStart = region * 1000;
		regionFinish = (region + 1) * 1000;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setRegion();

		View rootView = inflater.inflate(R.layout.fragment_servers, container, false);
		
		//now you must initialize your list view
        ListView serverListView = (ListView)rootView.findViewById(R.id.list);
        
        HashMap<String, String> world = setGw2HashMap(loadJSONFromRaw());
        ArrayList<String> world_name = new ArrayList<String>();
        ArrayList<String> world_id = new ArrayList<String>();
        for(int key = regionStart;key < regionFinish;key++){
        	String sKey = String.valueOf(key);
        	if(world.containsKey(sKey)){
        		world_name.add(world.get(sKey));
        		world_id.add(sKey);
        	}
        }
        
        ServerAdapter adapter = new ServerAdapter(serverListView.getContext(),
        		world_name.toArray(new String[0]),
        		world_id.toArray(new String[0]));
	    serverListView.setAdapter(adapter);
	    
	    serverListView.setOnItemClickListener(new OnItemClickListener() {
	    	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String world_id = ((TextView) view.findViewById(R.id.server_id)).getText().toString();
                String region = world_id.substring(0, 1);
                String world_name = ((TextView) view.findViewById(R.id.server_name)).getText().toString();
                 
                Intent in = new Intent(getActivity(), Gw2Activity.class);
            	in.putExtra("world_id", world_id);
            	in.putExtra("region", region);
            	in.putExtra("world_name", world_name);

                startActivity(in);
            }
        });
	    
		return rootView;
	}
	
	//InputStream ins = getResources().openRawResource( getResources().getIdentifier("raw/FILENAME_WITHOUT_EXTENSION","raw", getPackageName()) );
	public String loadJSONFromRaw() {
        String json = null;
        try {
        	InputStream is = this.getResources().openRawResource(R.raw.world);
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
