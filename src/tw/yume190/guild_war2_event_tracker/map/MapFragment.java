package tw.yume190.guild_war2_event_tracker.map;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.yume190.guild_war2_event_tracker.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MapFragment extends Fragment {
	
	private final int mapStart = 0;
	private final int mapFinish = 1000;
	
	String world_id;
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		world_id = getArguments().getString("world_id");

		View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
		
		//now you must initialize your list view
        ListView mapListView = (ListView)rootView.findViewById(R.id.list);
        
        HashMap<String, String> map = setGw2HashMap(loadJSONFromRaw());
        ArrayList<String> map_name = new ArrayList<String>();
        ArrayList<String> map_id = new ArrayList<String>();
        for(int key = mapStart;key < mapFinish;key++){
        	String sKey = String.valueOf(key);
        	if(map.containsKey(sKey)){
        		map_name.add(map.get(sKey));
        		map_id.add(sKey);
        	}
        }
        
        MapAdapter adapter = new MapAdapter(mapListView.getContext(),
        		map_name.toArray(new String[0]),
        		map_id.toArray(new String[0]));
	    mapListView.setAdapter(adapter);
	    
	    mapListView.setOnItemClickListener(new OnItemClickListener() {
	    	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String map_id = ((TextView) view.findViewById(R.id.map_id)).getText().toString();
                String map_name = ((TextView) view.findViewById(R.id.map_name)).getText().toString();
                 
            	Intent in = new Intent(getActivity(), MapEventsActivity.class);
                //Intent in = new Intent(getActivity(), TestActivity.class);
            	in.putExtra("world_id", world_id);
            	in.putExtra("map_id", map_id);
            	in.putExtra("map_name", map_name);

                startActivity(in);
            }
        });
	    
		return rootView;
	}
	
	//InputStream ins = getResources().openRawResource( getResources().getIdentifier("raw/FILENAME_WITHOUT_EXTENSION","raw", getPackageName()) );
	public String loadJSONFromRaw() {
        String json = null;
        try {
        	InputStream is = this.getResources().openRawResource(R.raw.map);
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
