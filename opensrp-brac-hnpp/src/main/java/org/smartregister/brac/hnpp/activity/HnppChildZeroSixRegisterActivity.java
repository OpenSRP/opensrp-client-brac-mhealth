package org.smartregister.brac.hnpp.activity;

import org.smartregister.brac.hnpp.fragment.HnppChildZeroSixRegisterFragment;
import org.smartregister.brac.hnpp.fragment.HnppRiskChildRegisterFragment;
import org.smartregister.view.fragment.BaseRegisterFragment;


public class HnppChildZeroSixRegisterActivity extends ChildRegisterActivity {



    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new HnppChildZeroSixRegisterFragment();
    }
}
