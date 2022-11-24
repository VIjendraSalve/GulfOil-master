package com.taraba.gulfoilapp.royalty_user_view.how_to_use_rewards;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.taraba.gulfoilapp.R;

import org.apache.commons.net.ftp.FTPReply;

public class HowToUseRewardsFragment extends Fragment {
    ImageView iv_product_image;
    WebView mywebview;
    String product_description;
    String product_image;
    String product_name;
    String reward_code;
    String reward_value;
    TextView tv_description;
    TextView tv_points;
    TextView tv_product_code;
    TextView tv_product_name;
    View view;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        this.product_name = arguments.getString("product_name", "");
        this.product_image = arguments.getString("product_image", "");
        this.reward_code = arguments.getString("reward_code", "");
        this.reward_value = arguments.getString("reward_value", "");
        this.product_description = arguments.getString("product_description", "");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.view = layoutInflater.inflate(R.layout.fragment_how_to_use_rewards, viewGroup, false);
        initView();
        return this.view;
    }

    public void initView() {
        this.tv_product_code = (TextView) this.view.findViewById(R.id.tv_product_code);
        this.tv_product_name = (TextView) this.view.findViewById(R.id.tv_product_name);
        this.tv_points = (TextView) this.view.findViewById(R.id.tv_points);
        this.tv_description = (TextView) this.view.findViewById(R.id.tv_description);
        this.iv_product_image = (ImageView) this.view.findViewById(R.id.iv_product_image);
        this.mywebview = (WebView) this.view.findViewById(R.id.mywebview);
        this.tv_product_code.setText(this.reward_code);
        this.tv_product_name.setText(this.product_name);
        this.tv_points.setText(this.reward_value);
        this.tv_description.setText(Html.fromHtml(this.product_description));
        Picasso.with(getActivity()).load(this.product_image).placeholder(getResources().getDrawable(R.drawable.loading)).error(getResources().getDrawable(R.drawable.about1)).resize(FTPReply.FILE_STATUS_OK, FTPReply.FILE_STATUS_OK).into(this.iv_product_image);
    }
}
