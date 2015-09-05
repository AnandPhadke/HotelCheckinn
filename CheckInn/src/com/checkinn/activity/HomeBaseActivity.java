package com.checkinn.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.checkinn.R;
import com.checkinn.session_manager.UserSession;
import com.checkinn.student.ScheduleActivity;
import com.parse.ParseUser;

public  class HomeBaseActivity extends Activity implements OnClickListener {
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	protected String[] sideMenuTitles;
	private TextView tvLogout;
	private TextView tvAdminStatus;
	private FrameLayout content_frame;
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setHomeButtonEnabled(true);
	        getActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
			}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(R.layout.activity_home);
		content_frame = (FrameLayout)findViewById(R.id.content_frame);
		  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.left_drawer);
	      
	      // sideMenuTitles = getResources().getStringArray(R.array.side_menu);
	        tvLogout =(TextView)findViewById(R.id.tvLogout);
	        tvAdminStatus=(TextView)findViewById(R.id.tvAdminStatus);
	        if(UserSession.getInstance(this).isAdmin())
	        	tvAdminStatus.setText("Welcome admin");
	        else
	        	tvAdminStatus.setText("Welcom user");
	        
	        tvLogout.setOnClickListener(this);
	        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
	                R.layout.drawer_list_item, sideMenuTitles));
	        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
			        mDrawerLayout.closeDrawer(mDrawerList);
			        if(UserSession.getInstance(HomeBaseActivity.this).isAdmin()){
			        	
			        }else{
			        	switch (position) {
						case 0:
							startActivity(new Intent(HomeBaseActivity.this,ScheduleActivity.class));
							break;

						default:
							break;
						}
			        }
			        
				}
			});
	        mDrawerToggle = new ActionBarDrawerToggle(
	                this,                  
	                mDrawerLayout,         
	                R.drawable.logo,  
	                R.string.drawer_open, 
	                R.string.drawer_close 
	                ) {
	            public void onDrawerClosed(View view) {
	                getActionBar().setTitle("Cafeteria");
	            }

	            public void onDrawerOpened(View drawerView) {
	            	getActionBar().setTitle("Cafeteria");
	            }
	        };
	        mDrawerLayout.setDrawerListener(mDrawerToggle);

	        	content_frame.removeAllViews();
	        	View view =View.inflate(this, layoutResID,null);
	        	content_frame.addView(view);
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
		
		
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        
        return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvLogout:
			UserSession.getInstance(this).setRememberme(false);
			UserSession.getInstance(this).setSession(false);
			ParseUser.logOut();
			startActivity(new Intent(this,LoginActivity.class));
			finish();
			break;

		default:
			break;
		}
	}
}
