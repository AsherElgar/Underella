
package com.asherelgar.myfinalproject.fragments;

import android.app.Dialog;
import android.content.Context;
import android.util.Pair;
import android.view.Window;

import com.asherelgar.myfinalproject.Util;
import com.asherelgar.myfinalproject.models.Database;

import java.util.Calendar;
import java.util.Date;


abstract class Dialog_Statistics {

    public static Dialog getDialog(final Context c, int since_boot) {
        final Dialog d = new Dialog(c);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		d.setContentView(R.layout.statistics);
//		d.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				d.dismiss();
//			}
//		});
        Database db = Database.getInstance(c);

        Pair<Date, Integer> record = db.getRecordData();

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(Util.getToday());
        int daysThisMonth = date.get(Calendar.DAY_OF_MONTH);

        date.add(Calendar.DATE, -6);

        int thisWeek = db.getSteps(date.getTimeInMillis(), System.currentTimeMillis()) + since_boot;

        date.setTimeInMillis(Util.getToday());
        date.set(Calendar.DAY_OF_MONTH, 1);
        int thisMonth = db.getSteps(date.getTimeInMillis(), System.currentTimeMillis()) + since_boot;



        db.close();

        return d;
    }

}
