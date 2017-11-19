/*
 * Copyright 2014 Thomas Hoffmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

//		((TextView) d.findViewById(R.id.record)).setText(
//                StepCounterFragment.formatter.format(record.second) + " @ "
//				+ java.text.DateFormat.getDateInstance().format(record.first));

//		((TextView) d.findViewById(R.id.totalthisweek)).setText(StepCounterFragment.formatter.format(thisWeek));
//		((TextView) d.findViewById(R.id.totalthismonth)).setText(StepCounterFragment.formatter.format(thisMonth));

//		((TextView) d.findViewById(R.id.averagethisweek)).setText(StepCounterFragment.formatter.format(thisWeek / 7));
//		((TextView) d.findViewById(R.id.averagethismonth)).setText(StepCounterFragment.formatter.format(thisMonth / daysThisMonth));

        db.close();

        return d;
    }

}
