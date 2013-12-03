package tw.yume190.guild_war2_event_tracker.gw2;

import tw.yume190.guild_war2_event_tracker.karka2.KarkaFragment;
import tw.yume190.guild_war2_event_tracker.map.MapFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Gw2TabsPagerAdapter extends FragmentPagerAdapter {
	
//	private String world_id;
//	private int region;
	
	String world_id;
    String region;
    String world_name;
    int length = 0;
	
	public Gw2TabsPagerAdapter(FragmentManager fm,String id,String name,String region,int length) {
		super(fm);
		world_id = id;
		world_name = name;
		this.region = region;
		this.length = length;
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
		
			// Top Rated fragment activity
			//return new TopRatedFragment();
		case 0:
			// Games fragment activity
			//return new GamesFragment();
			KarkaFragment karkaFragment = new KarkaFragment();
			//TODO
			Bundle bundle1 = new Bundle();
			bundle1.putString("region",region);
			karkaFragment.setArguments(bundle1);
			return karkaFragment;
		case 1:
		case 2:
			// Movies fragment activity
			MapFragment mapFragment = new MapFragment();
			Bundle bundle2 = new Bundle();
			bundle2.putString("world_id", world_id);
			mapFragment.setArguments(bundle2);
			return mapFragment;
			//return new MoviesFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return length;
	}

}
