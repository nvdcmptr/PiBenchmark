package com.nvd.pibenchmark;

import java.util.Formatter;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Activity calculate Pi to score CPU performance
 * 
 * @author NVD
 * @since Minimum required API 8
 * @version 93.0414.8
 */
public class Activity_Main extends Activity {

	// <Global Variable>
	int deciSec = 0, sec = 0, min = 0;
	boolean flagBreak = false;
	TextView lblTime;
	TextView lblStatus;
	TextView lblScore;
	ProgressBar progressBar;
	double tr1Result = 0, tr2Result = 0, tr3Result = 0, tr4Result = 0;
	long cycle;
	int completedThrdCnt = 0;
	long tr1Beg = 1, tr2Beg = 3, tr3Beg = 5, tr4Beg = 7, tr1End = tr1Beg, tr2End = tr2Beg, tr3End = tr3Beg, tr4End = tr4Beg, tr1Index = 0, tr2Index = 0, tr3Index = 0, tr4Index = 0;
	int versionNumber = 0;
	String versionName = "";
	Button btnStart;
	SeekBar sbAcc;
	Context context;

	// </Global Variable>

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity__main);
		PackageInfo pinfo;
		try {
			pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionNumber = pinfo.versionCode;
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			Toast.makeText(context, "Can not access version", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		// <Define Variable>
		Typeface fontDroid = Typeface.createFromAsset(context.getAssets(), "DroidLogo.ttf");
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setTypeface(fontDroid);
		Button btnAbort = (Button) findViewById(R.id.btnAbort);
		btnAbort.setTypeface(fontDroid);
		sbAcc = (SeekBar) findViewById(R.id.sbAcc);
		sbAcc.setProgress(5);
		cycle = 500000000;// 10^8
		sbAcc.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				cycle = (sbAcc.getProgress() + 1) * 100000000; // 10^8

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// do nothing

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// do nothing

			}
		});
		lblTime = (TextView) findViewById(R.id.lblTimeValue);
		lblTime.setTypeface(fontDroid);
		lblScore = (TextView) findViewById(R.id.lblScoreValue);
		lblScore.setTypeface(fontDroid);
		lblStatus = (TextView) findViewById(R.id.lblStatus);
		lblStatus.setTypeface(fontDroid);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		// label
		TextView tagAcc = (TextView) findViewById(R.id.lblAcc);
		tagAcc.setTypeface(fontDroid);
		TextView tagTime = (TextView) findViewById(R.id.lblTime);
		tagTime.setTypeface(fontDroid);
		TextView tagScore = (TextView) findViewById(R.id.lblScore);
		tagScore.setTypeface(fontDroid);
		// </Define Variable>

		lblStatus.setText(getString(R.string.lblStatus_ready));
		btnStart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// <initialize>
				flagBreak = false;
				lblStatus.setText(getString(R.string.lblStatus_calc));
				min = 0;
				sec = 0;
				deciSec = 0;
				completedThrdCnt = 0;
				tr1Index=0;
				tr2Index=0;
				tr3Index=0;
				tr4Index=0;
				tr1Result = 0;
				tr2Result = 0;
				tr3Result = 0;
				tr4Result = 0;
				// </initialize>
				// <Thread Define>
				final Thread tr1 = new Thread(new Runnable() {

					@Override
					public void run() {
						for (tr1Index = tr1Beg; (tr1Index <= tr1End) && !flagBreak; tr1Index += 8)
							tr1Result += (double) (+4.0 / tr1Index);
						completedThrdCnt++;
					}
				});

				final Thread tr2 = new Thread(new Runnable() {

					@Override
					public void run() {
						for (tr2Index = tr2Beg; (tr2Index <= tr2End) && !flagBreak; tr2Index += 8)
							tr2Result += (double) (-4.0 / tr2Index);
						completedThrdCnt++;

					}
				});
				final Thread tr3 = new Thread(new Runnable() {

					@Override
					public void run() {
						for (tr3Index = tr3Beg; (tr3Index <= tr3End) && !flagBreak; tr3Index += 8)
							tr3Result += (double) (+4.0 / tr3Index);
						completedThrdCnt++;
					}
				});

				final Thread tr4 = new Thread(new Runnable() {

					@Override
					public void run() {
						for (tr4Index = tr4Beg; (tr4Index <= tr4End) && !flagBreak; tr4Index += 8)
							tr4Result += (double) (-4.0 / tr4Index);
						completedThrdCnt++;
					}
				});
				// </Thread Define>
				if (cycle % 4 != 0)
					cycle += (4 - cycle % 4); // fix cycle divide able to 4
				long zarib = cycle / 4 - 1;
				tr1End = zarib * 8 + tr1Beg;
				tr2End = zarib * 8 + tr2Beg;
				tr3End = zarib * 8 + tr3Beg;
				tr4End = zarib * 8 + tr4Beg;
				// Start timer
				timeHandler.postDelayed(timeRun, 100);
				// Start ProgressBar
				prcbarHandler.postDelayed(prcbarRun, 1);
				// <Start Threads>
				tr1.start();
				tr2.start();
				tr3.start();
				tr4.start();
				// disable start button && SeekBar
				btnStart.setEnabled(false);
				sbAcc.setEnabled(false);
			}
		});// end btnStart.setOnCliclListener
		btnAbort.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				flagBreak = true;

			}
		});

	}// end onCreate

	// Define ProcessBar handler
	final Handler prcbarHandler = new Handler();
	final Runnable prcbarRun = new Runnable() {

		@Override
		public void run() {
			int percent = (int) ((tr4Index * 100) / tr4End);
			progressBar.setProgress(percent);
			if (percent != 100)
				prcbarHandler.postDelayed(prcbarRun, 10);
		}
	};
	// Define time handler
	final Handler timeHandler = new Handler();
	final Runnable timeRun = new Runnable() {

		@Override
		public void run() {
			// set minutes second and decisecond
			if (deciSec == 10) {
				sec++;
				deciSec = 0;
			}
			if (sec == 60) {
				min++;
				sec = 0;
			}

			lblTime.setText(min + " : " + sec + " : " + deciSec);
			deciSec++;
			if (completedThrdCnt != 4)
				timeHandler.postDelayed(timeRun, 100);
			else { // calculating Finished!

				CalcFinished();
			}
		}
	};// end runObj

	/**
	 * this method called when completedThrdCnt==4 so all Thread work finished
	 */
	public void CalcFinished() {
		if (flagBreak)
			lblStatus.setText(getString(R.string.lblStatus_abort));
		else
			lblStatus.setText(getString(R.string.lblStatus_ready));
		double time = min * 60 + sec + (deciSec / 10.0);
		if (time == 0.0) // prevent devision by ZERO
			time = 0.1;
		double score = (double) (cycle / time);
		// convert score unit to Mega (cycle per second)
		score /= 1000000;
		// format score to view only 3 digit after point
		Formatter scoreView = new Formatter();
		scoreView.format("%.3f", score);
		// check if program not aborted
		if (!flagBreak) {
			lblScore.setText(scoreView.toString());
			scoreView.close();
		} else
			// if we break calculation we should set score to null value
			lblScore.setText(getResources().getString(R.string.lblnull));
		// show Pi number
		AlertDialog alert = new AlertDialog.Builder(context).create();
		double result = tr1Result + tr2Result + tr3Result + tr4Result;
		alert.setMessage("Π ≈ " + result);
		alert.show();
		// enable start button && SeekBar
		btnStart.setEnabled(true);
		sbAcc.setEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity__main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menuShareScore:
			// Share the text
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, "I am using PiBenchmark software to Benchmark my device CPU\n" + "My score: " + lblScore.getText());
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
			break;
		case R.id.menuAbout:
			AlertDialog about = new AlertDialog.Builder(context).create();
			about.setMessage("* Software name: PiBenchmark\n" + "* Version: " + versionName + "\n" + "* Copyright 2014 Navid Norouzi \n* GNU General Public License\n" + "* Company: { nvd };\n" + "* Code: https://github.com/nvdcmptr/PiBenchmark");
			about.show();
			break;
		case R.id.menuExit:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * Problems: 1- man thread ha ro too on create tarif karde boodam va dar
	 * onclick btnStart unaro start mikardam ama ye moshkel be vojood umad ke
	 * barname faghat dafe aval dorost kar mikar ke be komak stackoverflow
	 * fahmidam bayad dobare thread haro tarif konam va man baraye hale in
	 * moshkel tarife thread haro be dakhele onclick dokme btnStart avordam va
	 * darnahayat problem solved!!!
	 */
	
	//  93.0411.7 : moshkele reset nashodane progressbar hal shod
	//  93.0414.8 : bug disable nashodane seekbar (ke mitavanest dar mohasebe score eshkal ijad konad) hal shod.
}
