package com.mert.transparent;


import java.util.Timer;
import java.util.TimerTask;
import pl.droidsonroids.gif.GifImageView;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

//burdan itibaren
public class AlwaysOnTopService extends Service implements View.OnTouchListener, View.OnClickListener {
	private GifImageView gifView;
  int tmp =0;
	private SeekBar seekBar;
	int dpx,dpy;


	String ıp;


	private final int gifViewId = 1001;
	private  int imgresources= R.drawable.ik;
	private int[] gifResources = {
			R.raw.rpgch1,
			R.raw.giphy

	};
	private ImageView img;
	private int gifResourcesIndex = 0;
	private WindowManager mWindowManager;
	RelativeLayout.LayoutParams relaparams,relaparams1;
	private WindowManager.LayoutParams mParams,mParams1,mParams2;
	private int seekbarkontrol=0;
    private float ekranx,ekrany;
	private int Position_X;
	private int Position_Y;
	private float mTouchStartX, mTouchStartY,mTouchStartX1,mTouchStartY1;
	private int mPrevX, mPrevY, mPrevX1,mPrevY1,mprevwidth,mprevheight,mPrevX2,mPrevY2;
	private int mScreenWidth, mScreenHeight;
	private RelativeLayout relativeLayout;
	private int ekranxbuyuk,ekranxkucuk,ekranybuyuk,ekranykucuk;
	private int defaultWidth = 48;
	private int defaultHeight = 48;
	private int defaultMargin = 0;
	private int scrWidth,scrHeight;
	private boolean mUpdateViewByTimer = true;
	private boolean mUpdateViewByTimer1= true;
	private WebView videoView;
	private Timer mTimer = new Timer();
	private MoveGifViewTimerTask mTimerTask = new MoveGifViewTimerTask();
	private MoveGifViewTimerTask mTimerTask1 = new MoveGifViewTimerTask();
	private int mTimerMoveX = defaultWidth/16;
	private int mTimerMoveY = defaultHeight/4;
	private int mMoveDirX = 1;
	private boolean mMoveDirXChanged = false;
	private BroadcastReceiver receiver = null;
	public static final String INTENT_BROADCAST_TIMER_TICK = "pe.sbk.alwaysontop.TIMER_TICK";

	private class MoveGifViewTimerTask extends TimerTask {
		public void run() {
			if(!mUpdateViewByTimer) return;
			if (!mUpdateViewByTimer1)return;
			sendBroadcast(new Intent(INTENT_BROADCAST_TIMER_TICK));
		}
	}

	public int onStartCommand (Intent intent, int flags, int startId) {
		ıp = intent.getStringExtra("test");
		//ıp=(String) intent.getExtras().get("test");
		Toast.makeText(this, "Starting..", Toast.LENGTH_SHORT).show();
		Log.d("test:",intent.getStringExtra("test"));
		WebSettings webSettings = videoView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setDomStorageEnabled(true);
		videoView.loadUrl("http://"+ıp+":8081/video");
		return START_STICKY;
	}


	@Override
	public IBinder onBind(Intent arg0) { return null; }
	@Override
	public void onCreate() {
		super.onCreate();

		gifView = new GifImageView(this);
		gifView.setImageResource(gifResources[1]);

		//btn1 = new Button(this);
		//btn2 = new Button(this);
	//	btn1.setText("stop");
		//btn2.setText("Start");
	//	btn2.setId(1005);
		//btn1.setId(1000);

		gifView.setId(gifViewId);
		relativeLayout = new RelativeLayout(this);
		videoView = new WebView(this);
		//videoView.setVideoPath("/sdcard/VID_20161028_132502");
		//Uri adres = Uri.parse("android.resource://" + getPackageName()+ "/"+ R.raw.ornek);







		//videoView.setVideoURI(adres);
		/*String videourl = "http://10.0.3.15:8080/";
		Uri uri = Uri.parse(videourl);
		videoView.setVideoURI(uri);*/



		videoView.setOnClickListener(this);
		videoView.setOnTouchListener(this);
	//	gifView.setScaleType(ImageView.ScaleType.FIT_XY);

		img = new ImageView(this);
		img.setImageResource(imgresources);
		img.setOnClickListener(this);
		//img.setBackgroundColor(Color.WHITE);
		seekBar = new SeekBar(this);
		seekBar.setId(200);
		seekBar.setProgress(40);
		relativeLayout.setBackgroundColor(Color.BLACK);

		//	btn1.setOnTouchListener(this);
		//btn1.setOnClickListener(this);

		 relaparams =
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);

		relaparams1 =
				new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT
				);
		relaparams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		relaparams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		relaparams1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		relaparams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

		
		//change values : pixel to dip...
		defaultWidth =  (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultWidth, getResources().getDisplayMetrics());
		defaultHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultHeight, getResources().getDisplayMetrics());
		defaultMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, defaultMargin, getResources().getDisplayMetrics());
		mTimerMoveX = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTimerMoveX, getResources().getDisplayMetrics());
		mTimerMoveY = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTimerMoveY, getResources().getDisplayMetrics());
		// burdan itibaren


		// set window params for top-most-view
		mParams = new WindowManager.LayoutParams(
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.WRAP_CONTENT,
			WindowManager.LayoutParams.TYPE_PHONE,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
			PixelFormat.TRANSLUCENT);

		//mParams.gravity = Gravity.TOP|Gravity.LEFT;




		mParams2 = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
				PixelFormat.TRANSLUCENT);





		mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);


		// get screen size in pixels
		 scrWidth  = mWindowManager.getDefaultDisplay().getWidth();
		 scrHeight = mWindowManager.getDefaultDisplay().getHeight();
		//
		 // new zero olan yana
		 dpx =scrWidth/(int)getResources().getDisplayMetrics().density ;
		 dpy = scrHeight/(int)getResources().getDisplayMetrics().density ;
		//
		//eski
		DisplayMetrics metrics = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(metrics);
		 ekranx=metrics.xdpi;
		 ekrany=metrics.ydpi;
		//


		mParams.width = scrWidth*40/100;
		int puki = 40*9/16;
		mParams.height = scrHeight*puki/100;
		relaparams1.width = mParams.width/4;
		relaparams1.height = mParams.height/4;
		relaparams.width = mParams.width;
		relaparams.height = mParams.height;
		relativeLayout.addView(videoView,relaparams);
		relativeLayout.addView(img,relaparams1);


	    //mParams.x = (int) scrWidth/6;
		//mParams.y=  (int) scrHeight/4;
		//mParams.x=(int)ekranx/8;
		//mParams.y=(int)ekrany;
		ekranxbuyuk=dpx-mParams.width/2;
		ekranxkucuk=-dpx+mParams.width/2;
		ekranybuyuk=dpy-mParams.height/2;
		ekranykucuk=-dpy+mParams.height/2;
		mParams.x= (int) dpx/8;
		mParams.y= (int) dpy/8;



		//mParams1.x = (int) mParams.x+ mParams.width/2-mParams1.width/2;
		//mParams1.y= (int)   mParams.y + mParams.height/2 - mParams1.height/2;

		//videoView.start();
		mWindowManager.addView(relativeLayout, mParams);



		// timer-tick receiver
		IntentFilter filter = new IntentFilter(INTENT_BROADCAST_TIMER_TICK);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(INTENT_BROADCAST_TIMER_TICK)) {
					//moveGifView();
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





		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


			public void onStopTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
				tmp=seekBar.getProgress();



			}

			public void onStartTrackingTouch(SeekBar seekBar) {
// TODO Auto-generated method stub
				tmp=seekBar.getProgress();

			}

			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
// TODO Auto-generated method stub
				if (progress<40){
					progress=40;
					seekBar.setProgress(40);
				}

		int hei = progress*9/16;
				mParams.width=scrWidth*progress/100;
				mParams.height=scrHeight*hei/100;
				//mParams1.x = (int) mParams.x+ mParams.width/2-mParams1.width/2;
			//	mParams1.y= (int)   mParams.y + mParams.height/2 - mParams1.height/2;
				mParams2.x=mParams.x;
				mParams2.y= mParams.y-(mParams.height/2)-25;
				relaparams.width = mParams.width;
				relaparams.height = mParams.height;
				relativeLayout.updateViewLayout(videoView,relaparams);
				mWindowManager.updateViewLayout(relativeLayout,mParams);
//				mWindowManager.updateViewLayout(img,mParams1);
				mWindowManager.updateViewLayout(seekBar,mParams2);

//mik
			}
		});

	}




	@Override
	public void onDestroy() {
		super.onDestroy();
		if(relativeLayout != null) {
			mWindowManager.removeView(relativeLayout);

		if (seekbarkontrol==1) {
			mWindowManager.removeView(seekBar);
		}

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
	/*private void changeImage() {
		gifResourcesIndex = (gifResourcesIndex+1)%gifResources.length;
		gifView.setImageResource(gifResources[gifResourcesIndex]);
	}*/
	int i =0;
	@Override
	public void onClick(View v) {
		//changeImage();
if(v.getId()==videoView.getId()){
			i++;
			Handler handler = new Handler();
			Runnable r = new Runnable() {

				@Override
				public void run() {
					i = 0;
				}
			};

		if (i == 1) {
				//Single click
				handler.postDelayed(r, 250);

			/*if (seekbarkontrol==1){
				seekbarkontrol=0;

				mWindowManager.removeView(seekBar);

			}*/

			} else if (i == 2) {
				//Double click
				i = 0;
			stopService(new Intent(this, AlwaysOnTopService.class));

			Intent dialogIntent = new Intent(this, AlwaysOnTopActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			//btn2 = null;
			startActivity(dialogIntent);




		}}

		if (v.getId()==img.getId()){
			if (seekbarkontrol==0) {
				mParams2.height = scrHeight/14;
				mParams2.width = scrWidth;
				mParams2.x=mParams.x;
				mParams2.y= mParams.y-(mParams.height/2)-25;
				seekbarkontrol = 1;
				mWindowManager.addView(seekBar, mParams2);
				//mWindowManager.removeView(img);

			}
			else {
				mWindowManager.removeView(seekBar);
				seekbarkontrol = 0;
			}
		}
/*
		if (v.getId()==btn1.getId()){
			stopService(new Intent(this, AlwaysOnTopService.class));
		}

		if (v.getId()==btn2.getId()){

			Intent dialogIntent = new Intent(this, AlwaysOnTopActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mWindowManager.removeView(btn2);
			//btn2 = null;
			startActivity(dialogIntent);





		}*/

		/*
		service içinde intent baaşlatmak için
		Intent dialogIntent = new Intent(this, AlwaysOnTopActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mWindowManager.removeView(btn2);
			//btn2 = null;
			startActivity(dialogIntent);
		 */
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int pointerCount = event.getPointerCount();

		// pointercount = el sayısı

		if (v.getId()==videoView.getId()){
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					mUpdateViewByTimer = false;
					mTouchStartX = event.getRawX();
					mTouchStartY = event.getRawY();
				/*	WindowManager.LayoutParams params=(WindowManager.LayoutParams)v.getLayoutParams();
					Position_X = (int) (X - params.horizontalMargin);
					Position_Y = (int) (Y - params.verticalMargin);*/

					mPrevX = mParams.x;
					mPrevY = mParams.y;
					mprevwidth=mParams.width;
					mprevheight=mParams.height;

					mPrevX2=mParams2.x;
					mPrevY2=mParams2.y;

					break;

				case MotionEvent.ACTION_MOVE:

						if (pointerCount==1) {

						int dx = (int) (event.getRawX() - mTouchStartX);
						int dy = (int) (event.getRawY() - mTouchStartY);

							if (mPrevX+dx<=dpx && mPrevX+dx>=-dpx) {
								mParams.x = mPrevX + dx;

								mParams2.x = mPrevX2 + dx;
							}
						if (mPrevY+dy>=-dpy && mPrevY+dy<=dpy ) {
							mParams.y = mPrevY + dy;

							mParams2.y = mPrevY2 + dy;
						}

							// ekranxbuyuk ekranykucuk ekranybuyuk ekranxkucuk


							mWindowManager.updateViewLayout(relativeLayout, mParams);
//
						if (seekbarkontrol==1)
							mWindowManager.updateViewLayout(seekBar,mParams2);


						}

					break;
//miki

				case MotionEvent.ACTION_UP:
					mUpdateViewByTimer = true;
					if (Math.abs((int) (event.getRawX() - mTouchStartX)) < defaultWidth / 4 &&
							Math.abs((int) (event.getRawY() - mTouchStartY)) < defaultHeight / 4)
						return v.performClick();
			}




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
		if( mParams2.x >= mScreenWidth-defaultWidth-defaultMargin ) mParams2.x = mScreenWidth-defaultWidth-defaultMargin*2;
		if( mParams2.y >= mScreenHeight-defaultHeight-defaultMargin ) mParams2.y = mScreenHeight-defaultHeight-defaultMargin*2;

		
		super.onConfigurationChanged(newConfig);
	}
}
