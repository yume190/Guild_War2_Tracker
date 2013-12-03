package tw.yume190.guild_war2_event_tracker.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class GW2 extends Service{

//	private InputStream mapRaw ;
//	private InputStream worldRaw;
//	private InputStream eventRaw;
	
	private String eventUrl = "https://api.guildwars2.com/v1/event_names.json";
	private String mapUrl = "https://api.guildwars2.com/v1/map_names.json";
	private String worldUrl = "https://api.guildwars2.com/v1/world_names.json";
	private String buildUrl = "https://api.guildwars2.com/v1/build.json";

	private String eventFileName = "event.json";
	private String mapFileName = "map.json";
	private String worldFileName = "world.json";
	private String buildFileName = "build.json";
	
//	private static final String EVENT_ID = "event_id";
//	private static final String MAP_ID = "map_id";
//	private static final String WORLD_ID = "world_id";
	private static final String BUILD_ID = "build_id";
	
	private HashMap<String, String> event;// = new HashMap<String, String>();
	private HashMap<String, String> map;// = new HashMap<String, String>();
	private HashMap<String, String> world;// = new HashMap<String, String>();
	//private HashMap<String, String> build;
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	
	private void init() {
		if(isBuildLatest()){
			Toast.makeText(this, "update ...", Toast.LENGTH_SHORT).show();
			Log.v("GW2 init","update ...");
			updateFiles();
			Toast.makeText(this, "update file done", Toast.LENGTH_SHORT).show();
			Log.v("GW2 init","update file done");
		}
		parseDatas();
		Toast.makeText(this, "parse data done", Toast.LENGTH_SHORT).show();
		Log.v("GW2 init","parse data done");
	}

	private void parseDatas() {
		parseData(map, mapFileName);
		parseData(world, worldFileName);
		parseData(event, eventFileName);
	}

	private void parseData(HashMap<String,String> map,String fileName) {
		map = getHashMap(readFile(fileName));
	}
	
	public HashMap<String, String> getHashMap(String stream){
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

	private void updateFiles() {
		updateFile(buildUrl, buildFileName);
		updateFile(eventUrl, eventFileName);
		updateFile(mapUrl, mapFileName);
		updateFile(worldUrl, worldFileName);
	}

	private void updateFile(String url,String fileName){
		String contentFromHttps;
		try {
			contentFromHttps = new HTTPSGetJson(url).SearchRequest("");
			writeFile(fileName, contentFromHttps);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isBuildLatest() {
		HTTPSGetJson buildHttps = new HTTPSGetJson(buildUrl);
		try {
			int versionHttps = buildHttps.ProcessResponseObject().getInt(BUILD_ID);
			
			String  fileContent = readFile(buildFileName);
			int versionFile = new JSONObject(fileContent).getInt(BUILD_ID);
			
			if(versionHttps == versionFile)
				return true;
			else
				return false;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		} 
		/*catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	private String readFile(String fileName){
		FileInputStream fis;
		try {
			fis = openFileInput(fileName);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);
						
			String sLine = null;
			String sOutput = "";
			while ((sLine = br.readLine())!=null)
				sOutput += sLine;
			return sOutput;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private void writeFile(String fileName,String content){
		FileOutputStream fos;
		try {
			fos = openFileOutput(fileName, MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
//	public String loadJSONFromRaw(InputStream is) {
//        String json = null;
//        try {
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }
//	
}
