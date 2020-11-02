package org.smartregister.brac.hnpp.activity;

import org.smartregister.brac.hnpp.fragment.HnppChildSevenTwentyFourRegisterFragment;
import org.smartregister.brac.hnpp.fragment.HnppChildZeroSixRegisterFragment;
import org.smartregister.view.fragment.BaseRegisterFragment;


public class HnppChildSevenTwentyFourRegisterActivity extends ChildRegisterActivity {



    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new HnppChildSevenTwentyFourRegisterFragment();
    }
}
