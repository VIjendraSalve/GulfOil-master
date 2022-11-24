package com.taraba.gulfoilapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.contentproviders.UserTableDatasource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android3 on 1/1/16.
 */
public class BarcodeListAdapter extends ArrayAdapter<BarcodeHistoryModel> {

    BarcodeListAdapter adapter;
    private final List<BarcodeHistoryModel> values;
    private final Context context;
    private ArrayList<BarcodeHistoryModel> circularList;
    int strParticipnt_login_id;
    ListView lstHisty;
    int pos;
    final UserTableDatasource ctdUser;


    public BarcodeListAdapter(Context context, List<BarcodeHistoryModel> values) {
        super(context, R.layout.history_list_item, values);
        this.values = values;
        this.context = context;
        ctdUser = new UserTableDatasource(context);
    }

    @Override
    public int getPosition(BarcodeHistoryModel item) {
        return super.getPosition(item);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_list_item, parent,
                    false);
        }

        TextView txtID = (TextView) convertView
                .findViewById(R.id.history_title);
        TextView txtMobNo = (TextView) convertView
                .findViewById(R.id.history_detail);


       /* ImageView imgDelete1=(ImageView) convertView.findViewById(R.id.history_delete);

        imgDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                circularList = ctdUser.getAllBarcode(String.valueOf(strParticipnt_login_id));
                Toast.makeText(context, "in image click", Toast.LENGTH_LONG).show();
                //pos = (Integer) v.getTag();
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.app_name))
                        .setMessage(context.getString(R.string.delete_msg))
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                BarcodeHistoryModel barhm = circularList.get(position);
                                ctdUser.removeBarCodeFromList(barhm.getId());
                                dialog.dismiss();
                                //reloadHistoryItems();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
*/

        txtID.setText("Text : " + values.get(position).getText());
        txtMobNo.setText("Format :" + values.get(position).getFormat());

        return convertView;

    }


    public void alertDialog(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(Html.fromHtml("" + message)).setCancelable(false)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        reloadHistoryItems();
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void reloadHistoryItems() {
        adapter.clear();
        circularList = new ArrayList<BarcodeHistoryModel>();
        final UserTableDatasource ctdUser = new UserTableDatasource(context);
        try {
            ctdUser.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        circularList = ctdUser.getAllBarcode(String.valueOf(strParticipnt_login_id));
        Log.e("adapter circularList", "circularList :" + circularList);
        adapter = new BarcodeListAdapter(context, circularList);
        lstHisty.setAdapter(adapter);


    }
}