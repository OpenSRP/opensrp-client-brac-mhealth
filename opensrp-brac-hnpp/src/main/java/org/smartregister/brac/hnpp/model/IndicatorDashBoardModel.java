package org.smartregister.brac.hnpp.model;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.smartregister.brac.hnpp.R;
import org.smartregister.brac.hnpp.contract.DashBoardContract;
import org.smartregister.brac.hnpp.repository.IndicatorRepository;
import org.smartregister.brac.hnpp.utils.DashBoardData;
import org.smartregister.brac.hnpp.utils.HnppConstants;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.family.util.DBConstants;

import java.text.MessageFormat;

public class IndicatorDashBoardModel implements DashBoardContract.Model {

    private Context context;

    public IndicatorDashBoardModel(Context context){
        this.context = context;
    }

    public DashBoardData getHHCount(String ssName, String month, String year){
        String query;

        DashBoardData  dashBoardData1 = new DashBoardData();
        if(TextUtils.isEmpty(ssName) && TextUtils.isEmpty(month) & TextUtils.isEmpty(year) ){
            query = "select count(*) as count from ec_family where date_removed is null ";
        }else if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            query = "select count(*) as count from ec_family where ss_name = '"+ssName+"' and strftime('%m', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+month+"' and strftime('%Y', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+year+"' and date_removed is null ";

        }
        else if(!TextUtils.isEmpty(ssName)){
            query = "select count(*) as count from ec_family where ss_name = '"+ssName+"' and date_removed is null ";
        }else {
            query = "select count(*) as count from ec_family where strftime('%m', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+month+"' and strftime('%Y', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+year+"' and date_removed is null ";
        }

        Log.v("WORD_QUERY","log:"+query);
        Cursor cursor = null;
        // try {
        cursor = CoreChwApplication.getInstance().getRepository().getReadableDatabase().rawQuery(query, new String[]{});
        if(cursor !=null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dashBoardData1 = new DashBoardData();
                dashBoardData1.setCount(cursor.getInt(0));
                dashBoardData1.setEventType(HnppConstants.EventType.FAMILY_REGISTRATION);
                dashBoardData1.setTitle(HnppConstants.workSummeryTypeMapping.get(dashBoardData1.getEventType()));

                try{
                    dashBoardData1.setImageSource((int)HnppConstants.iconMapping.get(dashBoardData1.getEventType()));
                }catch (Exception e){

                }
                cursor.moveToNext();
            }
            cursor.close();

        }


        return dashBoardData1;
    }
    public DashBoardData getMemberCount(String ssName, String month, String year){
        DashBoardData dashBoardData1 = new DashBoardData();

        String query;
        if(TextUtils.isEmpty(ssName) && TextUtils.isEmpty(month)){
            query = "select count(*) as count from ec_family_member where date_removed is null";
        }else {
            query = MessageFormat.format("select count(*) as count from {0} {1}", "ec_family_member", getFilterCondition(ssName,month,year));

        }
        Log.v("WORK_QUERY","member:"+query);
        Cursor cursor = null;
        // try {
        cursor = CoreChwApplication.getInstance().getRepository().getReadableDatabase().rawQuery(query, new String[]{});
        if(cursor !=null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dashBoardData1 = new DashBoardData();
                dashBoardData1.setCount(cursor.getInt(0));
                dashBoardData1.setEventType(HnppConstants.EventType.FAMILY_MEMBER_REGISTRATION);
                dashBoardData1.setTitle(HnppConstants.workSummeryTypeMapping.get(dashBoardData1.getEventType()));

                try{
                    dashBoardData1.setImageSource((int)HnppConstants.iconMapping.get(dashBoardData1.getEventType()));
                }catch (Exception e){

                }
                cursor.moveToNext();
            }
            cursor.close();

        }


        return dashBoardData1;
    }

    //for jonosonkha sarsonkhep compilation sheet
    public DashBoardData getFamilyMethodKnown(String ssName, String month, String year){
        return getVisitTypeCount("পরিবার পরিকল্পনা পদ্ধতি ব্যবহারকারী","familyplanning_method_known","yes",ssName,month,year);
    }
    public DashBoardData getNoFamilyMethodUser(String ssName, String month, String year){
        return getVisitTypeCount("পদ্ধতি ব্যবহারের বাইরে","familyplanning_method_known","no",ssName,month,year);
    }
    public DashBoardData getFillUser(String ssName, String month, String year){
        return getVisitTypeCount("পিল ব্যবহারকারী","familyplanning_method","contraceptive_pill",ssName,month,year);
    }
    public DashBoardData getFillFromSS(String ssName, String month, String year){
        return getVisitTypeCount("স্বাস্থ্যসেবিকা থেকে পিল ব্যবহারকারী","distribution_location","brac_health_worker",ssName,month,year);
    }
    public DashBoardData getFillFromOther(String ssName, String month, String year){
        return getVisitTypeCount("অন্যান্য উৎস থেকে পিল ব্যবহারকারী","distribution_location","other",ssName,month,year);
    }
    public DashBoardData getCondomUser(String ssName, String month, String year){
        return getVisitTypeCount("কনডম ব্যবহারকারী","familyplanning_method","condom",ssName,month,year);
    }
    public DashBoardData getIudUser(String ssName, String month, String year){
        return getVisitTypeCount("আই.ইউ.ডি. ব্যবহারকারী","familyplanning_method","iud",ssName,month,year);
    }
    public DashBoardData getInjectionUser(String ssName, String month, String year){
        return getVisitTypeCount("ইনজেকশন ব্যবহারকারী","familyplanning_method","injection",ssName,month,year);
    }
    public DashBoardData getNorplantUser(String ssName, String month, String year){
        return getVisitTypeCount("ইমপ্ল্যান্ট ব্যবহারকারী","familyplanning_method","norplant",ssName,month,year);
    }
    public DashBoardData getVasectomyUser(String ssName, String month, String year){
        return getVisitTypeCount("ভ্যাসেকটমি ব্যবহারকারী","familyplanning_method","vasectomy",ssName,month,year);
    }
    public DashBoardData getTubeUser(String ssName, String month, String year){
        return getVisitTypeCount("টিউব্যাকটোমি ব্যবহারকারী","familyplanning_method","ligation",ssName,month,year);
    }

    //work activity dashboard
    public DashBoardData getAnotherSource(String ssName, String month, String year){
        return getVisitTypeCount("এ\u200C এন সি অন্যান্য",HnppConstants.INDICATOR.ANC_OTHER_SOURCE,"govt",ssName,month,year);
    }
    public DashBoardData get4PlusAnc(String ssName, String month, String year){
        return getVisitTypeCount("4+ এ এন সি","anc_count","3",ssName,month,year);
    }
    public DashBoardData getCigerDelivery(String ssName, String month, String year){
        return getVisitTypeCount("প্রসবের ফলাফল(সিজার)","delivery_method_c_section","c_section",ssName,month,year);
    }
    public DashBoardData getNormalDelivery(String ssName, String month, String year){
        return getVisitTypeCount("প্রসবের ফলাফল(স্বাভাবিক)","delivery_method_general","normal",ssName,month,year);
    }
    public DashBoardData getTTWomen(String ssName, String month, String year){
        return getVisitTypeCount("টিটি টিকা প্রাপ্ত প্রসূতি মায়ের সংখ্যা","vaccination_tt_dose_completed","yes",ssName,month,year);
    }
    public DashBoardData getPncService48Hrs(String ssName, String month, String year){
        return getVisitTypeCount("৪৮ ঘণ্টার মধ্যে পি এন সি সেবা","is_delay","false",ssName,month,year);
    }
    public DashBoardData getPnc1to2(String ssName, String month, String year){
        return getVisitTypeCount("1-2 পি এন সি","number_of_pnc_1_2","2",ssName,month,year);
    }
    public DashBoardData getPnc3to4(String ssName, String month, String year){
        return getVisitTypeCount("3-3+পি এন সি","number_of_pnc_3","3",ssName,month,year);
    }
    public DashBoardData getReferrelByPregnency(String ssName, String month, String year){
        return getVisitTypeCount("গর্ভ সংক্রান্ত সমস্যার জন্য রেফার","cause_of_referral_woman","pregnancy_problems",ssName,month,year);
    }
    public DashBoardData getBrestFeedingByBirth(String ssName, String month, String year){
        return getVisitTypeCount("জন্মের এক ঘণ্টার মধ্যে বুকের দুধ খাওয়া শিশুর সংখ্যা","breastfeeding_time","1",ssName,month,year);
    }
    public DashBoardData getOnlyBrestFeeding(String ssName, String month, String year){
        return getVisitTypeCount("ছয় মাস পর্যন্ত শুধুমাত্র বুকের দুধ খাওয়া শিশুর সংখ্যা",HnppConstants.INDICATOR.FEEDING_UPTO_6_MONTH,"true",ssName,month,year);
    }
    public DashBoardData getChildSevenMonth(String ssName, String month, String year){
        return getVisitTypeCount("সাত মাসে বাড়তি খাবার খাওয়া শিশুর সংখ্যা","solid_food_month","7",ssName,month,year);
    }
    public DashBoardData getDeathBirth(String ssName, String month, String year){
        return getVisitTypeCount("নবজাতক মৃত্যু","preg_outcome","born_alive_died",ssName,month,year);
    }
    public DashBoardData getMotherDeath(String ssName, String month, String year){
        return getVisitTypeCount("মাতৃমৃত্যু","cause_of_death","c",ssName,month,year);
    }
    public DashBoardData getVisitTypeCount(String title,String indicatorKey,String indicatorValue, String ssName, String month, String year){
        DashBoardData dashBoardData1 = new DashBoardData();
        String mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ='"+indicatorValue+"'";
        if(indicatorKey.equalsIgnoreCase(HnppConstants.INDICATOR.ANC_OTHER_SOURCE)){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ='Govt' or "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ='other'";
        }
        else if(indicatorKey.equalsIgnoreCase("anc_count")){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" >="+indicatorValue;
        }
        else if(indicatorKey.equalsIgnoreCase("delivery_method_general")){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ='"+indicatorValue+"' or "+IndicatorRepository.INDICATOR_NAME+" ='delivery_method_c_section' and "+IndicatorRepository.INDICATOR_VALUE+" ='"+indicatorValue+"'";
        }
        else if(indicatorKey.equalsIgnoreCase("number_of_pnc_1_2")){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='number_of_pnc' and "+IndicatorRepository.INDICATOR_VALUE+" <="+indicatorValue;
        }
        else if(indicatorKey.equalsIgnoreCase("number_of_pnc_3")){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='number_of_pnc' and "+IndicatorRepository.INDICATOR_VALUE+" >="+indicatorValue;
        }
        else if(indicatorKey.equalsIgnoreCase("breastfeeding_time")){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" <="+indicatorValue;
        }
        else if(indicatorKey.equalsIgnoreCase("solid_food_month")){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ="+indicatorValue;
        }
        else if(indicatorKey.equalsIgnoreCase("cause_of_death")){
            mainCondition = " where "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ='preterm_death' or "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ='childbirth_death' or "+IndicatorRepository.INDICATOR_NAME+" ='"+indicatorKey+"' and "+IndicatorRepository.INDICATOR_VALUE+" ='postnatal_death'";
        }
        String query;
        if(TextUtils.isEmpty(ssName) && TextUtils.isEmpty(month)){
            query = MessageFormat.format("select count(*) as count from {0} {1}", IndicatorRepository.INDICATOR_TABLE, mainCondition);
        }else{
            query = MessageFormat.format("select count(*) as count from {0} {1}", IndicatorRepository.INDICATOR_TABLE, getVisitFilterCondition(ssName,month,year,mainCondition));

        }
        Log.v("WORK_SUMMERY","visit_type:"+query);

        Cursor cursor = null;
        // try {
        cursor = CoreChwApplication.getInstance().getRepository().getReadableDatabase().rawQuery(query, new String[]{});
        if(cursor !=null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                dashBoardData1.setCount(cursor.getInt(0));
                dashBoardData1.setTitle(title);
                dashBoardData1.setImageSource(R.drawable.rowavatar_member);

                cursor.moveToNext();
            }
            cursor.close();

        }


        return dashBoardData1;
    }
    public String getFilterCondition(String ssName, String month, String year){
        StringBuilder build = new StringBuilder();
        build.append(MessageFormat.format(" inner join {0} ", CoreConstants.TABLE_NAME.FAMILY));
        build.append(MessageFormat.format(" on {0}.{1} = {2}.{3} ", CoreConstants.TABLE_NAME.FAMILY, DBConstants.KEY.BASE_ENTITY_ID,
                CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.RELATIONAL_ID));
        if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" where {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%Y', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+year+"'"));

        }
        else if(!TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" where {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%Y', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+year+"'"));

        }
        else if(!TextUtils.isEmpty(ssName)){
            build.append(MessageFormat.format(" where {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));

        }

        return build.toString();
    }

    public String getVisitFilterCondition(String ssName, String month, String year, String mainCondition){
        StringBuilder build = new StringBuilder();

        build.append(mainCondition);
        if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0} = {1} ", HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));
            build.append(MessageFormat.format(" and {0} = {1} ", IndicatorRepository.MONTH ,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0} = {1} ", IndicatorRepository.YEAR  ,"'"+year+"'"));

        }
        else if(!TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0} = {1} ", IndicatorRepository.MONTH,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0} = {1} ", IndicatorRepository.YEAR ,"'"+year+"'"));

        }
        else if(!TextUtils.isEmpty(ssName)){
            build.append(MessageFormat.format(" and {0} = {1} ", HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));

        }

        return build.toString();
    }

    @Override
    public DashBoardContract.Model getDashBoardModel() {
        return this;
    }

    @Override
    public Context getContext() {
        return context;
    }
}
