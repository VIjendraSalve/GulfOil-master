package com.taraba.gulfoilapp.ui.fls.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taraba.gulfoilapp.AppConfig;
import com.taraba.gulfoilapp.FlsDashboardActivity;
import com.taraba.gulfoilapp.HelpActivity;
import com.taraba.gulfoilapp.LoginActivity;
import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.adapter.DashboardMenuAdapter;
import com.taraba.gulfoilapp.adapter.DividerItemDecorator;
import com.taraba.gulfoilapp.adapter.RecyclerViewOnClickListener;
import com.taraba.gulfoilapp.dialog.GulfUnnatiDialog;
import com.taraba.gulfoilapp.dialog.ImageSelectionOptionDialog;
import com.taraba.gulfoilapp.model.DashboardMenuModel;
import com.taraba.gulfoilapp.model.FLSProfileUpdateResponse;
import com.taraba.gulfoilapp.model.MenuTag;
import com.taraba.gulfoilapp.network.GulfService;
import com.taraba.gulfoilapp.network.RetrofitUtils;
import com.taraba.gulfoilapp.network.ServiceBuilder;
import com.taraba.gulfoilapp.util.ConnectionDetector;
import com.taraba.gulfoilapp.util.DataProvider;
import com.taraba.gulfoilapp.util.GulfOilUtils;
import com.taraba.gulfoilapp.util.ProgressDialogHelper;
import com.taraba.gulfoilapp.util.camera.CameraHelper;
import com.taraba.gulfoilapp.view.FullScreenImageActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/* renamed from: com.taraba.gulfoilapp.ui.fls.account.FlsAccountFragment */
public class FlsAccountFragment extends Fragment implements RecyclerViewOnClickListener,
        View.OnClickListener, CameraHelper.AsyncResponse {
    private static final int PROFILE_IMG_REQUEST_CODE = 101;
    private CameraHelper cameraHelper;
    private ImageView ivProfileImage;
    private ImageView ivUpdateProfileImage;
    private DashboardMenuAdapter menuAdapter;
    private List<DashboardMenuModel> menuList;
    private String profileImgLocalPath;
    private RecyclerView rvAccountMenu;
    private TextView tvEmailId;
    private TextView tvEmployeeCode;
    private TextView tvPhoneNo;
    private TextView tvUserName;
    private SharedPreferences userPref;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_account_fls, viewGroup, false);
        if (getActivity() instanceof FlsDashboardActivity) {
            ((FlsDashboardActivity) getActivity()).hideShowView(false);
        }
        init(inflate);
        setListeners();
        setMenuAdapter();
        return inflate;
    }

    private void init(View view) {
        this.cameraHelper = new CameraHelper(getActivity(), this);
        this.userPref = getActivity().getSharedPreferences("signupdetails", 0);
        this.tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        this.rvAccountMenu = (RecyclerView) view.findViewById(R.id.rvAccountMenu);
        this.ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
        this.ivUpdateProfileImage = (ImageView) view.findViewById(R.id.ivUpdateProfileImage);
        this.tvEmployeeCode = (TextView) view.findViewById(R.id.tvEmployeeCode);
        this.tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);
        this.tvPhoneNo = (TextView) view.findViewById(R.id.tvPhoneNo);
        this.tvUserName.setText(this.userPref.getString("fullname", "Not Available"));
        ((RequestBuilder) ((RequestBuilder) ((RequestBuilder) Glide.with((Fragment) this).load(this.userPref.getString("profile_image", "")).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).placeholder((int) R.drawable.ic_default_user_avatar)).into(this.ivProfileImage);
        this.tvEmployeeCode.setText(this.userPref.getString("username", ""));
        this.tvEmailId.setText(this.userPref.getString("email", ""));
        this.tvPhoneNo.setText(this.userPref.getString("mobile", ""));
        this.menuList = new DataProvider().getFlsAccountMenuList();
        this.rvAccountMenu.addItemDecoration(new DividerItemDecorator(getActivity().getResources().getDrawable(R.drawable.divider), 1));
    }

    private void setListeners() {
        this.ivUpdateProfileImage.setOnClickListener(this);
        this.ivProfileImage.setOnClickListener(this);
    }

    private void setMenuAdapter() {
        DashboardMenuAdapter dashboardMenuAdapter = new DashboardMenuAdapter(getActivity(), this.menuList);
        this.menuAdapter = dashboardMenuAdapter;
        dashboardMenuAdapter.setOnClickListener(this);
        this.rvAccountMenu.setAdapter(this.menuAdapter);
    }

    public void onRecyclerViewItemClick(View view, int i) {
        String menuTag = this.menuList.get(i).getMenuTag();
        menuTag.hashCode();
        char c = 65535;
        switch (menuTag.hashCode()) {
            case -1845462454:
                if (menuTag.equals(MenuTag.CHANGE_PASSWORD)) {
                    c = 0;
                    break;
                }
                break;
            case 2213697:
                if (menuTag.equals(MenuTag.HELP)) {
                    c = 1;
                    break;
                }
                break;
            case 1095242156:
                if (menuTag.equals(MenuTag.SIGN_OUT)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                Navigation.findNavController(view).navigate((int) R.id.newChangePasswordFragmentFLS);
                return;
            case 1:
                startActivity(new Intent(getActivity(), HelpActivity.class));
                return;
            case 2:
                new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle(getString(R.string.str_confirmation)).setDescription(getString(R.string.str_logout_msg)).setPosButtonText(getString(R.string.str_yes), new GulfUnnatiDialog.OnPositiveClickListener() {
                    public final void onClick(GulfUnnatiDialog gulfUnnatiDialog) {
                        FlsAccountFragment.this.lambda$onRecyclerViewItemClick$0$FlsAccountFragment(gulfUnnatiDialog);
                    }
                }).setNegButtonText(getString(R.string.str_no), (GulfUnnatiDialog.OnNegetiveClickListener) null).show();
                return;
            default:
                return;
        }
    }

    public /* synthetic */ void lambda$onRecyclerViewItemClick$0$FlsAccountFragment(GulfUnnatiDialog gulfUnnatiDialog) {
        logout();
    }

    private void logout() {
        AppConfig.isSplashPopUpSessionActive = true;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("userinfo", 0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("old_usertrno", "" + sharedPreferences.getString("usertrno", ""));
        edit.putString("username", "");
        edit.putString("userimage", "");
        edit.putString("usertrno", "");
        edit.putString("status", "");
        edit.commit();
        getActivity().finish();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void comingSoonDialog() {
        new GulfUnnatiDialog(getActivity(), new GulfOilUtils().getUserType()).setDescription("Coming soon...").setPosButtonText(getString(R.string.str_ok), (GulfUnnatiDialog.OnPositiveClickListener) null).show();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.ivProfileImage) {
            if (id == R.id.ivUpdateProfileImage) {
                ImgSelectionDialog(101, this.ivProfileImage);
            }
        } else if (!TextUtils.isEmpty(this.profileImgLocalPath)) {
            showFullScreenImage(this.profileImgLocalPath, "Profile Image", true);
        } else if (!TextUtils.isEmpty(this.userPref.getString("profile_image", ""))) {
            showFullScreenImage(this.userPref.getString("profile_image", ""), "Profile Image", false);
        }
    }

    private void ImgSelectionDialog(int i, ImageView imageView) {
        new ImageSelectionOptionDialog(getActivity(), new GulfOilUtils().getUserType()).setTitle("Select Image")
                .setOnCameraClickListener(dialog -> {
                    dialog.dismiss();
                    cameraHelper.selectImage(imageView, i, CameraHelper.CAMERA_INTENT);
                })
                .setOnGalleryClickListener(dialog -> {
                    dialog.dismiss();
                    cameraHelper.selectImage(imageView, i, CameraHelper.GALLERY_INTENT);
                }).show();
    }


    private void showFullScreenImage(String str, String str2, boolean z) {
        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
        intent.putExtra("IMG_URL", str);
        intent.putExtra("Title", str2);
        intent.putExtra("isLocalImg", z);
        startActivity(intent);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.cameraHelper.myActivityResult(i, i2, intent);
    }

    public void processFinish(String str, int i) {
        if (i == 101) {
            this.profileImgLocalPath = str;
            updateFLSProfileAPI();
        }
    }

    private void updateFLSProfileAPI() {
        if (ConnectionDetector.isNetworkAvailable(getActivity())) {
            ProgressDialogHelper.showProgressDialog(getActivity(), "Please wait...");
            Map<String, okhttp3.RequestBody> hashMap = new HashMap<>();
            hashMap.put("login_id", RetrofitUtils.createPartFromString(this.userPref.getString("usertrno", "")));
            Disposable disposable = ServiceBuilder.getRetrofit()
                    .create(GulfService.class)
                    .fLSProfileUpdate(
                            hashMap,
                            TextUtils.isEmpty(this.profileImgLocalPath) ? null :
                                    RetrofitUtils.prepareFilePart("profile_image", this.profileImgLocalPath)
                    )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateFLSProfileAPIResponse, this::updateFLSProfileAPIError);
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.str_internet_disconnected), Toast.LENGTH_LONG).show();
        }
    }

    /* access modifiers changed from: private */
    public void updateFLSProfileAPIResponse(FLSProfileUpdateResponse fLSProfileUpdateResponse) {
        ProgressDialogHelper.hideProgressDailog();
        if (ServiceBuilder.isSuccess(fLSProfileUpdateResponse.getStatus())) {
            new GulfUnnatiDialog(getActivity()).setTitle(getString(R.string.str_success)).hideDialogCloseButton(true).setDescription(fLSProfileUpdateResponse.getMessage())
                    .setPosButtonText(getString(R.string.str_ok), dialog -> {
                        userPref.edit().putString("profile_image", fLSProfileUpdateResponse.getProfile_image()).apply();
                        dialog.dismiss();
                    }).show();

        } else {
            new GulfUnnatiDialog(getActivity()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(fLSProfileUpdateResponse.getError()).setPosButtonText(getString(R.string.str_ok), dialog -> {
                dialog.dismiss();
                Glide.with(this)
                        .load(this.userPref.getString("profile_image", ""))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .placeholder((int) R.drawable.ic_default_user_avatar)
                        .into(this.ivProfileImage);
            }).show();
        }
    }


    /* access modifiers changed from: private */
    public void updateFLSProfileAPIError(Throwable th) {
        ProgressDialogHelper.hideProgressDailog();
        new GulfUnnatiDialog(getActivity()).setTitle(getString(R.string.str_error)).hideDialogCloseButton(true).setDescription(getString(R.string.something_went_wrong)).setPosButtonText(getString(R.string.str_ok), dialog -> {
            dialog.dismiss();
            Glide.with(this)
                    .load(this.userPref.getString("profile_image", ""))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder((int) R.drawable.ic_default_user_avatar)
                    .into(this.ivProfileImage);
        }).show();
    }


}
