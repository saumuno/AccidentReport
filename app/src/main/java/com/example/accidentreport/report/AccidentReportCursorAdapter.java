package com.example.accidentreport.report;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.accidentreport.R;
import com.example.accidentreport.domain.User;
import com.example.accidentreport.utils.AccidentReportContract;

import static androidx.core.content.ContextCompat.startActivity;

public class AccidentReportCursorAdapter extends SimpleCursorAdapter {

    private Cursor c;
    private Context context;

    public AccidentReportCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.c = c;
        this.context = context;
    }

    public View getView(int pos, View inView, ViewGroup parent) {
        View v = inView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_list_accident_reports, null);
        }
        this.c.moveToPosition(pos);
        String location = this.c.getString(this.c.getColumnIndex(AccidentReportContract.TableAccidentReportColumns.LOCATION));
        final String reason = this.c.getString(this.c.getColumnIndex(AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT));
        final String id = this.c.getString(this.c.getColumnIndex(AccidentReportContract.TableAccidentReportColumns.ID));
        byte[] image = this.c.getBlob(this.c.getColumnIndex(AccidentReportContract.TableAccidentReportColumns.IMAGE));
        ImageView iv = (ImageView) v.findViewById(R.id.list_item_text_image);
        if (image != null) {
            iv.setVisibility(View.VISIBLE);
            if (image.length > 3) {
                iv.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
            } else {
                iv.setImageResource(R.drawable.ic_profile);
            }
        }
        TextView locationView = (TextView) v.findViewById(R.id.list_item_text_location);
        locationView.setText(location);

        TextView reasonView = (TextView) v.findViewById(R.id.list_item_text_reason);
        reasonView.setText(reason);

        TextView idView = (TextView) v.findViewById(R.id.list_item_text_id);
        idView.setText(id);

        v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView idView = (TextView) v.findViewById(R.id.list_item_text_id);
                String id = idView.getText().toString();
                Intent intentMyLastReport = new Intent(context, AccidentReportActivity.class);
                intentMyLastReport.putExtra("userLogged", new User());
                intentMyLastReport.putExtra("newPart", "false");
                intentMyLastReport.putExtra("reportId", id);
                startActivity(context, intentMyLastReport, null);
            }
        });

        return (v);

    }
}
