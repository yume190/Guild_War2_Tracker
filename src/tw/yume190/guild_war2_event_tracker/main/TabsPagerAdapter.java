package tw.yume190.guild_war2_event_tracker.main;

import tw.yume190.guild_war2_event_tracker.server.ServerFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		ServerFragment serverFragment = new ServerFragment();
		Bundle bundle = new Bundle();
		switch (index) {
		case 0:
			bundle.putInt("region",1 );
			serverFragment.setArguments(bundle);
			return serverFragment;
		case 1:
			bundle.putInt("region",2 );
			serverFragment.setArguments(bundle);
			return serverFragment;
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 2;
	}

}
