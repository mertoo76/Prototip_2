package com.kositv.anigifneko;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class AlwaysOnTopService extends Service implements View.OnTouchListener, View.OnClickListener {
	private GifImageView gifView;
	private final int gifViewId = 1001;
	private int[] gifResources = {
			R.raw.rpgch1,
			R.raw.rpgch2,
			R.raw.rpgch3,
			R.raw.rpgch4,
			R.raw.rpgch5,
			R.raw.rpgch6,
			R.raw.rpgch7,
			R.raw.rpgch8,
			R.raw.rpgch9,
			R.raw.rpgch10,
			R.raw.rpgch11,
			R.raw.rpgch12,
			R.raw.rpgch13,
			R.raw.rpgch14,
			R.raw.rpgch15,
			R.raw.rpgch16,
			R.raw.rpgch17,
			R.raw.rpgch18,
			R.raw.rpgch19,
			R.raw.rpgch20,
			R.raw.rpgch21,
			R.raw.rpgch22,
			R.raw.rpgch23,
			R.raw.rpgch24
	};
	
	private int gifResourcesIndex = 0;
	
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mParams;
	
	private float mTouchStartX, mTouchStartY;
	private int mPrevX, mPrevY;
	private int mScreenWidth, mScreenHeight;
	
	private int defaultWidth = 48;
	private int defaultHeight = 48;
	private int defaultMargin = 0;
	
	private boolean mUpdateViewByTimer = true;
	private Timer mTimer = new Timer();
	private MoveGifViewTimerTask mTimerTask = new MoveGifViewTimerTask();
	private int mTimerMoveX = defaultWidth/16;
	private int mTimerMoveY = defaultHeight/4;
	private int mMoveDirX = 1;
	private boolean mMoveDirXChanged = false;
	private BroadcastReceiver receiver = null;
	public static final String INTENT_BROADCAST_TIMER_TICK = "pe.sbk.alwaysontop.TIMER_TICK";

	private class MoveGifViewTimerTask extends TimerTask {
		public void run() {
			if(!mUpdateViewByTimer) return;
			sendBroadcast(new Intent(INTENT_BROADCAST_TIMER_TICK));
		}
	}

	private void moveGifView() {
		if(mParams.x<=defaultMargin) {
			mParams.x = 0;
			mMoveDirX = 1;
			mMoveDirXChanged = true;
		}
		else if(mParams.x>=mScreenWidth-defaultWidth-defaultMargin) {
			mParams.x = mScreenWidth-defaultWidth-defaultMargin;
			mMoveDirX = -1;
			mMoveDirXChanged = true;
		}
		
		if(mMoveDirXChanged) {
			mMoveDirXChanged = false;
			mParams.y += (int)(mTimerMoveY*Math.signum(Math.random()-0.5));
			changeImage();
		}
		mParams.x += mTimerMoveX*mMoveDirX;
		mWindowManager.updateViewLayout(gifView, mParams);		
	}
		
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mTimer.scheduleAtFixedRate(mTimerTask, 500, 250);
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent arg0) { return null; }
	
	@Override
	public void onCreate() {
		super.onCreate();

		gifView = new GifImageView(this);
		gifView.setImageResource(gifResources[gifResourcesIndex]);
		gifView.setId(gifViewId);
		gifView.setOnClickListener(this);
		gifView.setOnTouchListener(this);
		
		//change values : pixel to dip...
		defaultWidth =  (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultWidth, getResources().getDisplayMetrics());
		defaultHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultHeight, getResources().getDisplayMetrics());
		defaultMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultMargin, getResources().getDisplayMetrics());
		mTimerMoveX = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTimerMoveX, getResources().getDisplayMetrics());
		mTimerMoveY = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTimerMoveY, getResources().getDisplayMetrics());
		
		// set window params for top-most-view
		mParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,				
			WindowManager.LayoutParams.TYPE_PHONE,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
			PixelFormat.TRANSLUCENT);
		
		mParams.gravity = Gravity.TOP|Gravity.LEFT;
		mParams.width = defaultWidth;
		mParams.height = defaultHeight; 
		
		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		// get screen size in pixels
		DisplayMetrics metrics = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		mScreenHeight = metrics.heightPixels;

		mParams.x = (int)(Math.random()*mScreenWidth*0.8);
		mParams.y = (int)(Math.random()*mScreenHeight*0.8);
		mWindowManager.addView(gifView, mParams);
		
		// timer-tick receiver
		IntentFilter filter = new IntentFilter(INTENT_BROADCAST_TIMER_TICK);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(INTENT_BROADCAST_TIMER_TICK)) {
					moveGifView();
				}
				else if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
					mUpdateViewByTimer = true;
				}
				else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
					mUpdateViewByTimer = false;
				}
			}
		};
		registerReceiver(receiver,filter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(gifView != null) {
			mWindowManager.removeView(gifView);
			gifView = null;
		}
		
		if(mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		
		if(null!=receiver) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}
	
	private void changeImage() {
		gifResourcesIndex = (gifResourcesIndex+1)%gifResources.length;
		gifView.setImageResource(gifResources[gifResourcesIndex]);
	}
	
	@Override
	public void onClick(View v) {
		changeImage();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mUpdateViewByTimer = false;
			mTouchStartX = event.getRawX();
			mTouchStartY = event.getRawY();
			mPrevX = mParams.x;
			mPrevY = mParams.y;
			break;
			
		case MotionEvent.ACTION_MOVE:
			int dx = (int)(event.getRawX()-mTouchStartX);
			int dy = (int)(event.getRawY()-mTouchStartY);
			mParams.x = mPrevX + dx;
			mParams.y = mPrevY + dy;
			mWindowManager.updateViewLayout(gifView, mParams);
			break;
			
		case MotionEvent.ACTION_UP:
			mUpdateViewByTimer = true;
			if( Math.abs((int)(event.getRawX()-mTouchStartX)) < defaultWidth/4 &&
				Math.abs((int)(event.getRawY()-mTouchStartY)) < defaultHeight/4 )
				return v.performClick();
		}
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		DisplayMetrics metrics = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.widthPixels;
		mScreenHeight = metrics.heightPixels;
		
		if( mParams.x >= mScreenWidth-defaultWidth-defaultMargin ) mParams.x = mScreenWidth-defaultWidth-defaultMargin*2; 
		if( mParams.y >= mScreenHeight-defaultHeight-defaultMargin ) mParams.y = mScreenHeight-defaultHeight-defaultMargin*2; 
		
		super.onConfigurationChanged(newConfig);
	}
}
