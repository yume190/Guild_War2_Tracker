package tw.yume190.guild_war2_event_tracker.gw2;

import tw.yume190.guild_war2_event_tracker.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

public class Gw2Activity extends FragmentActivity implements
		ActionBar.TabListener {

	private ViewPager viewPager;
	private Gw2TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	//private String[] tabs = { "Boss", "Dungeon & Temple" , "Map"};
	private String[] tabs = { "Karka Queen" , "Map"};
	
	String world_id;
    String region;
    String world_name;
    
    Menu menu;
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.karka_menu, menu);
//        
//        this.menu = menu;
// 
//        return super.onCreateOptionsMenu(menu);
//    }
//    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId() == R.id.a){
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent it = getIntent();
		world_id = it.getStringExtra("world_id");
		region = it.getStringExtra("region");
		world_name = it.getStringExtra("world_name");
		/*String regionText = "";
		if(Integer.valueOf(region) == 1){
			regionText = "US - ";
		}else if(Integer.valueOf(region) == 2){
			regionText = "EU - ";
		}*/
		
		setTitle(/*regionText + */world_name);
		

		// Initilization
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new Gw2TabsPagerAdapter(getSupportFragmentManager(),world_id,world_name,region,tabs.length);
		
		

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
				
//				menu.clear();
//				if(position == 0){
//					getMenuInflater().inflate(R.menu.karka_menu, menu);
//				}
//				else{
//					getMenuInflater().inflate(R.menu.map_menu, menu);
//				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}
