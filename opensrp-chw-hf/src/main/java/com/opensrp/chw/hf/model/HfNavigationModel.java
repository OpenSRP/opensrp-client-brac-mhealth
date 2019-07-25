package com.opensrp.chw.hf.model;

import com.opensrp.chw.core.model.NavigationModel;
import com.opensrp.chw.core.model.NavigationOption;
import com.opensrp.chw.core.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class HfNavigationModel implements NavigationModel.Flavor {
    private List<NavigationOption> navigationOptions = new ArrayList<>();
    @Override
    public List<NavigationOption> getNavigationItems() {
        if (navigationOptions.size() == 0) {
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_families, com.opensrp.chw.core.R.mipmap.sidemenu_families_active, com.opensrp.chw.core.R.string.menu_all_clients, Constants.DrawerMenu.ALL_CLIENTS, 0));
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_families, com.opensrp.chw.core.R.mipmap.sidemenu_families_active, com.opensrp.chw.core.R.string.menu_all_families, Constants.DrawerMenu.ALL_FAMILIES, 0));
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_anc, com.opensrp.chw.core.R.mipmap.sidemenu_anc_active, com.opensrp.chw.core.R.string.menu_anc, Constants.DrawerMenu.ANC, 0));
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_pnc, com.opensrp.chw.core.R.mipmap.sidemenu_pnc_active, com.opensrp.chw.core.R.string.menu_pnc, Constants.DrawerMenu.PNC, 0));
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_children, com.opensrp.chw.core.R.mipmap.sidemenu_children_active, com.opensrp.chw.core.R.string.menu_child_clients, Constants.DrawerMenu.CHILD_CLIENTS, 0));
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_fp, com.opensrp.chw.core.R.mipmap.sidemenu_fp_active, com.opensrp.chw.core.R.string.menu_family_planning, Constants.DrawerMenu.FAMILY_PLANNING, 0));
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_malaria, com.opensrp.chw.core.R.mipmap.sidemenu_malaria_active, com.opensrp.chw.core.R.string.menu_malaria, Constants.DrawerMenu.MALARIA, 0));
            navigationOptions.add(new NavigationOption(com.opensrp.chw.core.R.mipmap.sidemenu_referrals, com.opensrp.chw.core.R.mipmap.sidemenu_referrals_active, com.opensrp.chw.core.R.string.menu_referrals, Constants.DrawerMenu.REFERRALS, 0));
        }

        return navigationOptions;
    }
}
