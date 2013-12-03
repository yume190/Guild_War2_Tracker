package tw.yume190.guild_war2_event_tracker.karka2;

import java.util.ArrayList;
import java.util.HashMap;

import tw.yume190.guild_war2_event_tracker.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

//Important
// http://theopentutorials.com/tutorials/android/listview/android-expandable-list-view-example/
public class KarkaAdapter extends BaseAdapter {
	
	Context _context;
	ArrayList<HashMap<String, String>> _link;
	Karka karka = new Karka();

	public KarkaAdapter(Context context,ArrayList<HashMap<String, String>> link) {
		_context = context;
	    _link = link;
	}
	
	@Override
	public int getCount() {
		return _link.size();
	}

	@Override
	public Object getItem(int arg0) {
		return _link.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return (long)position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) _context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		//rootView = inflater.inflate(R.layout.karka_list, parent, false);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.karka_list, null);
		}
			
		HashMap<String, String> data = _link.get(position);
		
		if((position % 2) == 0){
			((LinearLayout)convertView.findViewById(R.id.listKarka)).setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
		}else{
			((LinearLayout)convertView.findViewById(R.id.listKarka)).setBackgroundColor(Color.rgb(0xfe, 0xff, 0xcd));
		}
		
		((TextView)convertView.findViewById(R.id.server_name)).setText(data.get("server"));
		
		((ProgressBar)convertView.findViewById(R.id.progressBarKarka)).setProgress(getSuccessAmount(data));
		TextView textViewBar = ((TextView)convertView.findViewById(R.id.progressKarka));
		textViewBar.setText(getSuccessAmount(data) + "/4");
		TextView textViewKarka = ((TextView)convertView.findViewById(R.id.stateKarka));
		String karkaString = "Prepare.";
		if(karka.stateMap.get(data.get("Defeat the Karka Queen threatening the settlements.-1")) == 0){
			karkaString = "Karka Queen appear at 1";
		}else if(karka.stateMap.get(data.get("Defeat the Karka Queen threatening the settlements.-2")) == 0){
			karkaString = "Karka Queen appear at 2";
		}
		textViewKarka.setText(karkaString);
		
		setSettlements(convertView,data);
   
		return convertView;
	}
	
	int getSuccessAmount(HashMap<String, String> data){
		int success = 0;
		
		int successPride = karka.stateMap.get(data.get(karka.mission1));
		int successKiel = karka.stateMap.get(data.get(karka.mission2));
		int successCamp = karka.stateMap.get(data.get(karka.mission3));
		int successSteampipe = karka.stateMap.get(data.get(karka.mission4));
		
		success = karka.getSuccess(successPride) + 
				karka.getSuccess(successKiel) + 
				karka.getSuccess(successCamp) + 
				karka.getSuccess(successSteampipe);
		
		return success;
	}
		
	void setSettlements(View convertView,HashMap<String, String> data){
		setSettlement(convertView,R.id.layout_pride,	R.id.imageViewPride,	R.id.textViewPride,		data.get("Recapture the settlement from crazed wildlife."));
		setSettlement(convertView,R.id.layout_kiel,		R.id.imageViewKiel,		R.id.textViewKiel,		data.get("Defend the repair worker."));
		setSettlement(convertView,R.id.layout_camp,		R.id.imageViewCamp,		R.id.textViewCamp,		data.get("Clear the karka and their eggs to rebuild Camp Karka."));
		setSettlement(convertView,R.id.layout_steampipe,R.id.imageViewSteampipe,R.id.textViewSteampipe,	data.get("Reclaim the settlement by burning out the karka nests."));
	}
	
	void setSettlement(View rootView,int layoutId,int imageId,int stateId,String state){
//		LinearLayout layout = (LinearLayout) rootView.findViewById(layoutId);
		ImageView iv = (ImageView) rootView.findViewById(imageId);
		TextView tv = (TextView) rootView.findViewById(stateId);
		int stateNumber = karka.stateMap.get(state);
		
		tv.setText(state);
		
		if(karka.getSuccess(stateNumber) == 1){
			iv.setImageResource(R.drawable.map_waypoint);
		}else{
			iv.setImageResource(R.drawable.map_waypoint_contested);
		}
		
		/*
		Active The event is running now
		Inactive The event is not running now
		Success The event has succeeded
		Fail The event has failed
		Warmup The event is waiting for certain criteria to be met before activating
		Preparation The criteria for the event to start have been met, but certain activities (such as an NPC dialogue) have not completed yet. After the activites have been completed, the event will become Active.
		*/
		/*switch(stateNumber){
		case 0:
			layout.setBackgroundColor(Color.RED);
			break;
		case 1:
			layout.setBackgroundColor(Color.WHITE);
			break;
		case 2:
			layout.setBackgroundColor(Color.CYAN);
			break;
		case 3:
			layout.setBackgroundColor(Color.LTGRAY);
			break;
		case 4:
			layout.setBackgroundColor(Color.YELLOW);
			break;
		case 5:
			layout.setBackgroundColor(Color.MAGENTA);
			break;
		default:
			layout.setBackgroundColor(Color.DKGRAY);
			break;
		}*/
	}
}
