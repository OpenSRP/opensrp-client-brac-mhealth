package org.smartregister.brac.hnpp.fragment;

import org.smartregister.brac.hnpp.R;
import org.smartregister.brac.hnpp.model.HnppChildRegisterFragmentModel;
import org.smartregister.brac.hnpp.presenter.HnppChildSevenTwentyFourFragmentPresenter;
import org.smartregister.brac.hnpp.presenter.HnppChildZeroSixRegisterFragmentPresenter;
import org.smartregister.view.activity.BaseRegisterActivity;

public class HnppChildSevenTwentyFourRegisterFragment extends HnppChildRegisterFragment {
    @Override
    protected void initializePresenter() {
        if (getActivity() == null) {
            return;
        }

        String viewConfigurationIdentifier = ((BaseRegisterActivity) getActivity()).getViewIdentifiers().get(0);
        presenter = new HnppChildSevenTwentyFourFragmentPresenter(this, new HnppChildRegisterFragmentModel(), viewConfigurationIdentifier);

    }
    @Override
    protected int getToolBarTitle() {
        return R.string.menu_child_0_6;
    }
}
