package tw.yume190.guild_war2_event_tracker.map;

import tw.yume190.guild_war2_event_tracker.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MapAdapter  extends ArrayAdapter<String> {
	private final Context context;
	private final String[] name;
	private final String[] id;
	
	public MapAdapter(Context context, String[] name,String[] id) {
		super(context, R.layout.map_list,name);
		this.context = context;
	    this.name = name;
	    this.id = id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.map_list, parent, false);
		
		TextView textViewName = (TextView) rowView.findViewById(R.id.map_name);
		textViewName.setText(name[position]);
		TextView textViewId = (TextView) rowView.findViewById(R.id.map_id);
		textViewId.setText(id[position]);
   
		return rowView;
	}
} 