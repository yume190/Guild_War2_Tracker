package tw.yume190.guild_war2_event_tracker.karka2;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import tw.yume190.guild_war2_event_tracker.R;
import tw.yume190.guild_war2_event_tracker.tool.HTTPSGetJson;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class KarkaService extends Service{
	
	HashMap<String,HashMap<String,String>> datas = new HashMap<String,HashMap<String,String>>();
	HashMap<String, String> worldHashMap = new HashMap<String, String>();
	HashMap<String, String> interestEvent = new HashMap<String, String>();
	
	String url = "https://api.guildwars2.com/v1/events.json?map_id=873";
	HTTPSGetJson httpsGetJson = new HTTPSGetJson(url);
	
	String region;
	boolean isUpdate = false;

	@Override
	public void onCreate() {
		super.onCreate();
		
		/*if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}*/
		
		setInterestEvent();
		worldHashMap = getHashMap(loadJSONFromRaw());
		
		updateDatas();
	}
	
	private void setInterestEvent() {
		interestEvent.put("8985FAD1-5B30-4BB9-9F23-C80F73BB3295", "Defend the settlement from crazed wildlife.-1");
		interestEvent.put("1A07A31D-90B6-4C94-B6FA-3B27504B188D", "Defend the settlement from crazed wildlife.-2");
		interestEvent.put("C47E8F27-B9DF-4297-8802-1EC1A088D054", "Recapture the settlement from crazed wildlife.");
		
		interestEvent.put("42B3E918-03F3-4070-8115-902773C13C0D", "Defend the settlement from rampaging, crazed wildlife.");
		interestEvent.put("129A1429-361A-4115-94DB-322659A0ABEF", "Defend the repair worker.");
		
		interestEvent.put("F6563016-3832-4EBA-82F1-535BFF96422D", "Defend Camp Karka from karka invasion.");
		interestEvent.put("B5021202-8B05-40AF-97C6-9C623EAB2956", "Clear the karka and their eggs to rebuild Camp Karka.");

		interestEvent.put("8C512F55-CF6F-4DDD-A80B-7DCBAD0F104C", "Protect the settlement's vital storage facility.");
		interestEvent.put("10F87D71-E2DA-4EA2-981D-15181CE0D349", "Reclaim the settlement by burning out the karka nests.");
		
		interestEvent.put("E1CC6E63-EFFE-4986-A321-95C89EA58C07", "Defeat the Karka Queen threatening the settlements.-1");
		interestEvent.put("4CF7AA6E-4D84-48A6-A3D1-A91B94CCAD56", "Defeat the Karka Queen threatening the settlements.-2");
//		interestEvent.put("F479B4CF-2E11-457A-B279-90822511BB53", "Defeat the Karka Queen threatening the settlements.-2");
//		interestEvent.put("5282B66A-126F-4DA4-8E9D-0D9802227B6D", "Defeat the Karka Queen threatening the settlements.-3");
	}
	
//	"8985FAD1-5B30-4BB9-9F23-C80F73BB3295":{"name":"Defend the settlement from crazed wildlife.","level":80,"map_id":873,"flags":["group_event"],"location":{"type":"sphere","center":[18918.9,3647.46,-1157.79],"radius":5950.35,"rotation":0}}                                                                                                                                                 -1157.79],"radius":5950.35,"rotation":0}}
//	"1A07A31D-90B6-4C94-B6FA-3B27504B188D":{"name":"Defend the settlement from crazed wildlife.","level":80,"map_id":873,"flags":["group_event"],"location":{"type":"sphere","center":[25215.9,13173.8,-13.4226],"radius":3711.52,"rotation":0}}
	/*
"F479B4CF-2E11-457A-B279-90822511B53B":{"name":"Defeat the Karka Queen threatening the settlements.","level":80,"map_id":873,"flags":["group_event","map_wide"],"location":{"type":"sphere","center":[18452.6,5688.58,-1114.78],"radius":3961.2,"rotation":0}}
"5282B66A-126F-4DA4-8E9D-0D9802227B6D":{"name":"Defeat the Karka Queen threatening the settlements.","level":80,"map_id":873,"flags":["group_event","map_wide"],"location":{"type":"sphere","center":[22574.1,8370.16,-170.891],"radius":3961.2,"rotation":0}}
"E1CC6E63-EFFE-4986-A321-95C89EA58C07":{"name":"Defeat the Karka Queen threatening the settlements.","level":80,"map_id":873,"flags":["group_event","map_wide"],"location":{"type":"sphere","center":[1866.51,-517.085,-1569.04],"radius":3961.2,"rotation":0}}
"4CF7AA6E-4D84-48A6-A3D1-A91B94CCAD56":{"name":"Defeat the Karka Queen threatening the settlements.","level":80,"map_id":873,"flags":["group_event","map_wide"],"location":{"type":"sphere","center":[-1555.73,-10355.9,-1238.77],"radius":3961.2,"rotation":0}}
	*/
	private void flushDatas(){
		datas.clear();
		
		for(String worldId : worldHashMap.keySet()){
			if(worldId.substring(0, 1).equals(region)){
				HashMap<String,String> world = new HashMap<String,String>();
				String worldName = worldHashMap.get(worldId);
				datas.put(worldName, world);
			}
		}
	}
	
	private void updateDatas(){
		new Thread(runnable).start();
	}
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			//Toast.makeText(getApplication(), "update datas start",Toast.LENGTH_SHORT).show();
			try {
				JSONObject jsonObject = httpsGetJson.ProcessResponseObject();
				JSONArray jsonArray = jsonObject.getJSONArray("events");
				
				flushDatas();
				
				for(int i = 0; i < jsonArray.length();i++){
					JSONObject gw2Event = jsonArray.getJSONObject(i);
					
					String world_id = gw2Event.getString("world_id");
					String event_id = gw2Event.getString("event_id");
					String state = gw2Event.getString("state");
				
					if(interestEvent.containsKey(event_id)){
						String worldName = worldHashMap.get(world_id);
						String eventName = interestEvent.get(event_id);
						
						if(!datas.containsKey(worldName))
							continue;
						
						HashMap<String,String> server = datas.get(worldName);
						server.put(eventName, state);
					}
				}
				delWithZeroCapture(datas);				
			}catch (Exception e) {
				//Toast.makeText(getApplication(), e.toString(),Toast.LENGTH_SHORT).show();
				Log.e("yume e", e.toString());
			}	
			isUpdate = false;
		}
		
		void delWithZeroCapture(HashMap<String,HashMap<String,String>> datas){
			Karka karka = new Karka();
			String[] serverNames = new String[datas.size()];
			int index = 0;
			for(String serverName : datas.keySet()){
				serverNames[index++] = serverName;
			}
			
			for(String serverName : serverNames){
				int sum = 0;
				sum += karka.getSuccess(karka.stateMap.get(datas.get(serverName).get(karka.mission1)));
				sum += karka.getSuccess(karka.stateMap.get(datas.get(serverName).get(karka.mission2)));
				sum += karka.getSuccess(karka.stateMap.get(datas.get(serverName).get(karka.mission3)));
				sum += karka.getSuccess(karka.stateMap.get(datas.get(serverName).get(karka.mission4)));
				
				if(sum == 0){
					datas.remove(serverName);
				}
			}
		}
	};
	
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
	
	@Override
	public IBinder onBind(Intent intent) {
		
		region = intent.getExtras().getString("region");
		//Toast.makeText(getApplication(), region, Toast.LENGTH_SHORT).show();
		
		return mBinder;
	}
	

    @Override
	public void onDestroy() {
    	//Toast.makeText(getApplication(), "Karka service on destroy", Toast.LENGTH_SHORT).show();
		super.onDestroy();
	}

	private final KarkaAIDL.Stub mBinder = new KarkaAIDL.Stub() {
 
        @Override
        public HashMap<String,HashMap<String,String>> get() throws RemoteException{
            return datas;
        }

		@Override
		public void update() throws RemoteException {
			isUpdate = true;
			updateDatas();
		}

		@Override
		public boolean getUpdateState() throws RemoteException {
			return isUpdate;
		}

    };
}
