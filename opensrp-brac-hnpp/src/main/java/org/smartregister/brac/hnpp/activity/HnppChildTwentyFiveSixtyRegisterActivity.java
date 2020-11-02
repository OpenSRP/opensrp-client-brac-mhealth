package org.smartregister.brac.hnpp.activity;

import org.smartregister.brac.hnpp.fragment.HnppChildSevenTwentyFourRegisterFragment;
import org.smartregister.brac.hnpp.fragment.HnppChildTwentyFiveSixtyRegisterFragment;
import org.smartregister.view.fragment.BaseRegisterFragment;


public class HnppChildTwentyFiveSixtyRegisterActivity extends ChildRegisterActivity {



    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new HnppChildTwentyFiveSixtyRegisterFragment();
    }
}
