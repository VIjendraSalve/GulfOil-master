package com.taraba.gulfoilapp.util;

import android.content.Context;

import com.taraba.gulfoilapp.R;
import com.taraba.gulfoilapp.model.DashboardMenu;
import com.taraba.gulfoilapp.model.DashboardMenuModel;
import com.taraba.gulfoilapp.model.FlsUnnatiDashboardModel;
import com.taraba.gulfoilapp.model.MenuTag;
import com.taraba.gulfoilapp.model.SearchMemberButton;
import com.taraba.gulfoilapp.royalty_user_view.dashboard.model.RoyaltyDashboardMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarabasoftwareiinc on 06/05/17.
 */

public class DataProvider {
    public static final int brown = 0xFFA71919;
    public static final int orange = 0xFFF55505;
    public static final int red = 0xFFFF180B;

    public List<DashboardMenu> getMechDashboardMenuList(Context context) {
        List<DashboardMenu> dashboardMenuList = new ArrayList<>();
        dashboardMenuList.add(new DashboardMenu(R.mipmap.h, context.getString(R.string.menu_view_profile), brown));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.k, context.getString(R.string.menu_view_transaction), orange));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.b, context.getString(R.string.menu_view_reward), red));
        dashboardMenuList.add(new DashboardMenu(R.drawable.ic_digital_reward, context.getString(R.string.menu_your_digital_reward), brown));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.e, context.getString(R.string.menu_whats_new), orange));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.d, context.getString(R.string.menu_scheme_letter), red));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.j, context.getString(R.string.menu_target_meter), brown));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.c, context.getString(R.string.menu_change_pass), orange));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.i, context.getString(R.string.menu_help_new), red));
        dashboardMenuList.add(new DashboardMenu(R.drawable.calc, context.getString(R.string.menu_points_calculator), brown));
        dashboardMenuList.add(new DashboardMenu(R.drawable.ic_retailer_campaign_rewards, context.getString(R.string.campaign_rewards), red));
        //dashboardMenuList.add(new DashboardMenu(R.drawable.ic_retailer_dashbord_tally_invoice_upload, context.getString(R.string.tally_invoice_upload), orange));
        dashboardMenuList.add(new DashboardMenu(R.drawable.ipl_logo, context.getString(R.string.ipl_offer), orange));
        return dashboardMenuList;
    }

    public List<DashboardMenu> getRetailorDashboardMenuList(Context context) {
        List<DashboardMenu> dashboardMenuList = new ArrayList<>();
        dashboardMenuList.add(new DashboardMenu(R.mipmap.h, context.getString(R.string.menu_view_profile), brown));
//        dashboardMenuList.add(new DashboardMenu(R.mipmap.f, context.getString(R.string.menu_edit_regi), orange));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.g, context.getString(R.string.menu_member_search), red));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.e, context.getString(R.string.menu_whats_new), brown));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.b, context.getString(R.string.menu_view_reward), orange));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.d, context.getString(R.string.menu_scheme_letter), red));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.j, context.getString(R.string.menu_target_meter), brown));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.c, context.getString(R.string.menu_change_pass), orange));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.i, context.getString(R.string.menu_help_new), red));
        dashboardMenuList.add(new DashboardMenu(R.mipmap.a, context.getString(R.string.menu_app_demo), brown));
        dashboardMenuList.add(new DashboardMenu(R.drawable.calc, context.getString(R.string.menu_points_calculator), red));
        //dashboardMenuList.add(new DashboardMenu(R.drawable.ic_retailer_campaign_rewards, context.getString(R.string.campaign_rewards), red));
        return dashboardMenuList;
    }

    public List<RoyaltyDashboardMenu> getRoyaltyDashboardMenuList(Context context, String retailerType) {
        List<RoyaltyDashboardMenu> dashboardMenuList = new ArrayList<>();
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_profile, context.getString(R.string.menu_view_profile)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_view_transactions, context.getString(R.string.menu_view_transaction)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.view_reward, context.getString(R.string.menu_view_reward)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic__royalty_digital_reward, context.getString(R.string.menu_your_digital_reward)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_whats_new, context.getString(R.string.menu_whats_new)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_scheme_letter, context.getString(R.string.menu_scheme_letter)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_target_meter, context.getString(R.string.menu_target_meter)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_change_pass, context.getString(R.string.menu_change_pass)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_new_help, context.getString(R.string.menu_help)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_app_demo, context.getString(R.string.menu_app_demo)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_new_points_calculator, context.getString(R.string.menu_points_calculator)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_campaign_rewards, context.getString(R.string.campaign_rewards)));
        //if (retailerType.equalsIgnoreCase("Unnati Royale")) {
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_royale_benefit, context.getString(R.string.royale_benefit)));
//            dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_royale_unnati_dashbord_tally_invoice_upload, context.getString(R.string.tally_invoice_upload)));
        dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.ic_ipl_royalty, context.getString(R.string.ipl_offer)));
        //}
        //dashboardMenuList.add(new RoyaltyDashboardMenu(R.drawable.two_wheelar, "Knowledge Corner"));
        return dashboardMenuList;
    }


    public List<SearchMemberButton> getSearchMemberButtons(Context context) {

        List<SearchMemberButton> searchMemberButtonsList = new ArrayList<>();

        searchMemberButtonsList.add(new SearchMemberButton("Profile", R.color.gridbutton_b));
        searchMemberButtonsList.add(new SearchMemberButton("Transaction History", R.color.gridbutton_r));
        searchMemberButtonsList.add(new SearchMemberButton("Order Status", R.color.gridbutton_o));
        searchMemberButtonsList.add(new SearchMemberButton("View Target", R.color.lightpic));
        searchMemberButtonsList.add(new SearchMemberButton("Submit OTP", R.color.gridbutton_o));
        searchMemberButtonsList.add(new SearchMemberButton("Reset OTP", R.color.gridbutton_r));

        return searchMemberButtonsList;
    }

    public List<DashboardMenuModel> getMoreMenuList() {
        List<DashboardMenuModel> list = new ArrayList<>();
        list.add(new DashboardMenuModel(R.drawable.ic_unnati_connect, "Unnati Connect", MenuTag.UNNATI_CONNECT));
        list.add(new DashboardMenuModel(R.drawable.ic_rewards, "Rewards", MenuTag.REWARDS));
        list.add(new DashboardMenuModel(R.drawable.ic_new_points_calculator, "Points Calculator", MenuTag.POINTS_CALCULATOR));
        list.add(new DashboardMenuModel(R.drawable.ic_knowledge_corner, "Knowledge Corner", MenuTag.KNOWLEDGE_CORNER));
        return list;
    }

    public List<DashboardMenuModel> getAccountMenuList() {
        List<DashboardMenuModel> list = new ArrayList<>();
        list.add(new DashboardMenuModel(R.drawable.ic_my_profile, "My Profile", MenuTag.MY_PROFILE));
        list.add(new DashboardMenuModel(R.drawable.ic_points, "Points", MenuTag.POINTS));
        list.add(new DashboardMenuModel(R.drawable.ic_your_digital_rewards, "Your Digital Rewards", MenuTag.YOUR_DIGITAL_REWARDS));
        list.add(new DashboardMenuModel(R.drawable.tds, "My TDS Certificate", MenuTag.MyTDSCertificate));
        list.add(new DashboardMenuModel(R.drawable.ic_new_help, "Help", MenuTag.HELP));
        list.add(new DashboardMenuModel(R.drawable.ic_change_password, "Change Password", MenuTag.CHANGE_PASSWORD));
        list.add(new DashboardMenuModel(R.drawable.ic_sign_out, "Sign Out", MenuTag.SIGN_OUT));
        return list;
    }
    public List<DashboardMenuModel> getFlsAccountMenuList() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new DashboardMenuModel(R.drawable.ic_new_help, "Help", MenuTag.HELP));
        arrayList.add(new DashboardMenuModel(R.drawable.ic_change_password, "Change Password", MenuTag.CHANGE_PASSWORD));
        arrayList.add(new DashboardMenuModel(R.drawable.ic_sign_out, "Sign Out", MenuTag.SIGN_OUT));
        return arrayList;
    }
    public static List<FlsUnnatiDashboardModel> getFlsDashboardList() {
        FlsUnnatiDashboardModel flsUnnatiDashboardModel = new FlsUnnatiDashboardModel();
        flsUnnatiDashboardModel.setTitle("My Retailers");
        flsUnnatiDashboardModel.setImg_res(R.drawable.fls_my_retailers_bg);
        flsUnnatiDashboardModel.setDescription(" Your retailers' point balance, target and rewards.");
        FlsUnnatiDashboardModel flsUnnatiDashboardModel2 = new FlsUnnatiDashboardModel();
        flsUnnatiDashboardModel2.setTitle("Magic Bonanza");
        flsUnnatiDashboardModel2.setImg_res(R.drawable.fls_magic_bonanza_bg);
        flsUnnatiDashboardModel2.setDescription("Quarterly target & achievement");
        FlsUnnatiDashboardModel flsUnnatiDashboardModel3 = new FlsUnnatiDashboardModel();
        flsUnnatiDashboardModel3.setTitle("Unnati Connect");
        flsUnnatiDashboardModel3.setImg_res(R.drawable.fls_unnati_connect_bg);
        flsUnnatiDashboardModel3.setDescription("All Schemes, Policies and Brochures.");
        FlsUnnatiDashboardModel flsUnnatiDashboardModel4 = new FlsUnnatiDashboardModel();
        flsUnnatiDashboardModel4.setTitle("View Rewards");
        flsUnnatiDashboardModel4.setImg_res(R.drawable.fls_rewards_bg);
        flsUnnatiDashboardModel4.setDescription("Unnati Rewards Catalogue");
        ArrayList arrayList = new ArrayList();
        arrayList.add(flsUnnatiDashboardModel);
        arrayList.add(flsUnnatiDashboardModel2);
        arrayList.add(flsUnnatiDashboardModel3);
        arrayList.add(flsUnnatiDashboardModel4);
        return arrayList;
    }
}
