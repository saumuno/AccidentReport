package com.example.accidentreport.report;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.accidentreport.R;
import com.example.accidentreport.database.DbHelper;
import com.example.accidentreport.domain.User;
import com.example.accidentreport.start.MainActivity;
import com.example.accidentreport.utils.AccidentReportContract;


public class AccidentReportsListFragment extends ListFragment {

    private SimpleCursorAdapter accidentReportAdapter;

    private User userLogged;

    private static final String[] FROM = {AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT,
            AccidentReportContract.TableAccidentReportColumns.LOCATION,
            AccidentReportContract.TableAccidentReportColumns.IMAGE};
    private static final int[] TO = {R.id.list_item_text_reason,
            R.id.list_item_text_location, R.id.list_item_text_image};



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.not_reports));

        try {
            userLogged = ((MainActivity) getActivity()).getUserLogged();
        }catch (Exception e){
            userLogged = ((MyReportsActivity) getActivity()).getUserLogged();
        }


        Cursor cursor = getCursor(userLogged.getUsername());

        accidentReportAdapter = new AccidentReportCursorAdapter(getActivity(), R.layout.item_list_accident_reports,
                cursor, FROM, TO);

        setListAdapter(accidentReportAdapter);

        accidentReportAdapter.swapCursor(cursor);

    }

    private Cursor getCursor(String username) {
        DbHelper dbHelper = new DbHelper(this.getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] accidentReportList = {
                AccidentReportContract.TableAccidentReportColumns.ID,
                AccidentReportContract.TableAccidentReportColumns.USERNAME_PART,
                AccidentReportContract.TableAccidentReportColumns.REASON_ACCIDENT,
                AccidentReportContract.TableAccidentReportColumns.LOCATION,
                AccidentReportContract.TableAccidentReportColumns.IMAGE,
                AccidentReportContract.TableAccidentReportColumns.SURNAMES_A,
                AccidentReportContract.TableAccidentReportColumns.NAME_A,
                AccidentReportContract.TableAccidentReportColumns.PHONE_A,
                AccidentReportContract.TableAccidentReportColumns.DNI_A,
                AccidentReportContract.TableAccidentReportColumns.REGISTRATION_A,
                AccidentReportContract.TableAccidentReportColumns.SURNAMES_B,
                AccidentReportContract.TableAccidentReportColumns.NAME_B,
                AccidentReportContract.TableAccidentReportColumns.PHONE_B,
                AccidentReportContract.TableAccidentReportColumns.DNI_B,
                AccidentReportContract.TableAccidentReportColumns.REGISTRATION_B
        };

        String selection = AccidentReportContract.TableAccidentReportColumns.USERNAME_PART + " = ?";
        String[] selectionArgs = {username};

        String sortOrder = AccidentReportContract.DEFAULT_SORT_ACCIDENT_REPORT;

        Cursor cursor = db.query(
                AccidentReportContract.TABLE_ACCIDENT_REPORT,
                accidentReportList,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return cursor;
    }
}
