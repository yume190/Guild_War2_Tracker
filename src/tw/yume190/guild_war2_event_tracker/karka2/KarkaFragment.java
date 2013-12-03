package tw.yume190.guild_war2_event_tracker.karka2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import tw.yume190.guild_war2_event_tracker.R;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.view.animation.*;

public class KarkaFragment extends Fragment{

	KarkaAdapter listAdapter;
	ListView listView;
	
	ArrayList<HashMap<String, String>> link = new ArrayList<HashMap<String,String>>();

	ServiceConnection mServiceConnection;
	protected KarkaAIDL mService;

	String region;
	Menu menu;
	
	static final int DO_UPDATE = 0;
	
	
	class MyHandler extends Handler{
		
		MenuItem mItem;
		
		public MyHandler(MenuItem item) {
			mItem = item;
		}
		
		public void handleMessage(Message msg) {   
            switch (msg.what) {   
                 case DO_UPDATE:   
                	mItem.getActionView().clearAnimation();
                	mItem.collapseActionView();
 				    mItem.setActionView(null);
                    break;   
            }   
            super.handleMessage(msg);   
       }   
	}
	MyHandler myHandler;

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.map_menu, menu);
		this.menu = menu;
		
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_refresh){
			try {
				if(mService != null && !mService.getUpdateState()){
					updateListView();
					
//					item.setActionView(R.layout.action_progressbar);
//				    item.expandActionView();
//				    
//				    item.collapseActionView();
//				    item.setActionView(null);
				    
					Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.karka_anim);
					item.setActionView(R.layout.action_karka);
					item.getActionView().startAnimation(anim);
					
					myHandler = new MyHandler(item);
					
					new Thread(new Runnable() {
						@Override
						public void run() {
							while(true){
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									//e.printStackTrace();
								}
								try {
									if(mService != null && !mService.getUpdateState()){
										Message message = new Message();   
					                    message.what = DO_UPDATE;   
					                    myHandler.sendMessage(message);
					                    break;
									}
								} catch (RemoteException e) {
									//e.printStackTrace();
								}
							}
						}
					}).start();
					
				}
			} catch (RemoteException e) {
				//e.printStackTrace();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	 @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setHasOptionsMenu(true);
     }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		region = getArguments().getString("region");

		View rootView = inflater.inflate(R.layout.fragment_karka2, container,false);
		listView = (ListView) rootView.findViewById(R.id.list);
		
//		Button bt1 = (Button) rootView.findViewById(R.id.button1);
//		bt1.setOnClickListener(new OnClickListener() {
//			
//			public void onClick(View v) {
//				try {
//					//Toast.makeText(getActivity(), mService.get().toString(), Toast.LENGTH_SHORT).show();
//					prepareListData();
//					listAdapter.notifyDataSetChanged();
//				} catch (Exception e) {
//					//Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
		
		initConnection();
		listAdapter = new KarkaAdapter(listView.getContext(),link);
		listView.setAdapter(listAdapter);
		
		//new KarkaThread().start();
		final Handler handler = new Handler();
		handler.postDelayed( new Runnable() {

		    @Override
		    public void run() {
		    	updateListView();
		        handler.postDelayed( this, 10 * 1000 );
		    }
		}, 10 * 1000 );

		return rootView;
	}
	
	void updateListView(){
		if(mService != null){
			prepareListData();
			listAdapter.notifyDataSetChanged();
			try {
				mService.update();
			} catch (RemoteException e) {
				//e.printStackTrace();
			}
		}
	}

	private void prepareListData() {
		try {
			link.clear();
			@SuppressWarnings("unchecked")
			HashMap<String,HashMap<String,String>> datas = (HashMap<String, HashMap<String, String>>) mService.get();
			for(String data : datas.keySet()){
				datas.get(data).put("server", data);
				link.add(datas.get(data));
			}
			Collections.sort(link, new Comparator<HashMap<String,String>>() {

				@Override
				public int compare(HashMap<String,String> lhs, HashMap<String,String> rhs) {
					Karka karka = new Karka();
					
					int sumLeft = 0;
					int sumRight = 0;
					
					sumLeft += karka.getSuccess(karka.stateMap.get(lhs.get(karka.mission1)));
					sumLeft += karka.getSuccess(karka.stateMap.get(lhs.get(karka.mission2)));
					sumLeft += karka.getSuccess(karka.stateMap.get(lhs.get(karka.mission3)));
					sumLeft += karka.getSuccess(karka.stateMap.get(lhs.get(karka.mission4)));
					
					sumRight += karka.getSuccess(karka.stateMap.get(rhs.get(karka.mission1)));
					sumRight += karka.getSuccess(karka.stateMap.get(rhs.get(karka.mission2)));
					sumRight += karka.getSuccess(karka.stateMap.get(rhs.get(karka.mission3)));
					sumRight += karka.getSuccess(karka.stateMap.get(rhs.get(karka.mission4)));
					
					return sumRight - sumLeft;
				}
			});
				
		} catch (Exception e) {
			//Toast.makeText(getActivity(), e.toString() + " in kaka frag",Toast.LENGTH_LONG).show();
		}
	}

	void initConnection() {
		mServiceConnection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				mService = null;
//				Toast.makeText(getActivity(), "Karka service disconnect", Toast.LENGTH_SHORT).show();
//				Log.d("IRemote", "Binding - Service disconnected");
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mService = KarkaAIDL.Stub.asInterface((IBinder) service);
				// Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_SHORT).show();
//				Toast.makeText(getActivity(), "Karka service connect", Toast.LENGTH_SHORT).show();
//				Log.d("IRemote", "Binding is done - Service connected");
			}
		};
		if (mService == null) {
			Intent it = new Intent(getActivity(),KarkaService.class);
			it.putExtra("region", region);
			//it.setAction("com.yume190.karka");
			getActivity().bindService(it, mServiceConnection,Service.BIND_AUTO_CREATE);
		}
	}
}
