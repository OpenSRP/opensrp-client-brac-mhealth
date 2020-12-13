package org.smartregister.brac.hnpp.model;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.smartregister.brac.hnpp.contract.DashBoardContract;
import org.smartregister.brac.hnpp.utils.DashBoardData;
import org.smartregister.brac.hnpp.utils.HnppConstants;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.utils.ChwDBConstants;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.family.util.DBConstants;

import java.text.MessageFormat;

public class WorkSummeryDashBoardModel implements DashBoardContract.Model {

    private Context context;

    public WorkSummeryDashBoardModel(Context context){
        this.context = context;
    }

    public DashBoardData getHHCount(String ssName, String month, String year){
        String query;

        DashBoardData  dashBoardData1 = new DashBoardData();
        if(TextUtils.isEmpty(ssName) && TextUtils.isEmpty(month) & TextUtils.isEmpty(year) ){
            query = "select count(*) as count from ec_family where date_removed is null ";
        }else if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            query = "select count(*) as count from ec_family where ss_name = '"+ssName+"' and strftime('%m', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+month+"' and strftime('%y', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+year+"' and date_removed is null ";

        }
        else if(!TextUtils.isEmpty(ssName)){
            query = "select count(*) as count from ec_family where ss_name = '"+ssName+"' and date_removed is null ";
        }else {
            query = "select count(*) as count from ec_family where strftime('%m', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+month+"' and strftime('%y', datetime("+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime')) = '"+year+"' and date_removed is null ";
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

    //for PA
    public DashBoardData getEyeTestCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.EYE_TEST,ssName,month,year);
    }
    public DashBoardData getBloodGroupingCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.BLOOD_GROUP,ssName,month,year);
    }
    //

    public DashBoardData getANCRegisterCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.ANC_REGISTRATION,ssName,month,year);
    }

    public DashBoardData getAnc1Count(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.ANC1_REGISTRATION,ssName,month,year);
    }
    public DashBoardData getAnc2Count(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.ANC2_REGISTRATION,ssName,month,year);
    }
    public DashBoardData getAnc3Count(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.ANC3_REGISTRATION,ssName,month,year);
    }
    public DashBoardData getElcoCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.ELCO,ssName,month,year);
    }
    public DashBoardData getAncCount(String ssName, String month, String year){
        return getVisitTypeCount("ANC",ssName,month,year);
    }
    public DashBoardData getDeliveryCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.PREGNANCY_OUTCOME,ssName,month,year);
    }
    public DashBoardData getPncCount(String ssName, String month, String year){
        return getVisitTypeCount("pnc",ssName,month,year);
    }
    public DashBoardData getEncCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.ENC_REGISTRATION,ssName,month,year);
    }
    public DashBoardData getChildFollowUpCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.CHILD_FOLLOWUP,ssName,month,year);
    }
    public DashBoardData getNcdForumCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.FORUM_NCD,ssName,month,year);
    }
    public DashBoardData getNcdServiceCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.NCD_PACKAGE,ssName,month,year);
    }
    public DashBoardData getWomenForumCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.FORUM_WOMEN,ssName,month,year);
    }
    public DashBoardData getWomenServiceCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.WOMEN_PACKAGE,ssName,month,year);
    }
    public DashBoardData getAdoForumCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.FORUM_ADO,ssName,month,year);
    }
    public DashBoardData getAdoServiceCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.GIRL_PACKAGE,ssName,month,year);
    }
    public DashBoardData getChildForumCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.FORUM_CHILD,ssName,month,year);
    }
    public DashBoardData getChildServiceCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.IYCF_PACKAGE,ssName,month,year);
    }
    public DashBoardData getAdultForumCount(String ssName, String month, String year){
        return getVisitTypeCount(HnppConstants.EVENT_TYPE.FORUM_ADULT,ssName,month,year);
    }

    public DashBoardData getVisitTypeCount(String visitType, String ssName, String month, String year){
        DashBoardData dashBoardData1 = new DashBoardData();
        String mainCondition;
        if(visitType.equalsIgnoreCase("ANC")){
            mainCondition = "where visit_type = '"+ HnppConstants.EVENT_TYPE.ANC1_REGISTRATION+"' or visit_type = '"+HnppConstants.EVENT_TYPE.ANC2_REGISTRATION+"'" +
                    " or visit_type = '"+ HnppConstants.EVENT_TYPE.ANC3_REGISTRATION+"' or visit_type = '"+ HnppConstants.EventType.ANC_HOME_VISIT+"'";
        }else if(visitType.equalsIgnoreCase("pnc")){
            mainCondition = "where visit_type = '"+ HnppConstants.EVENT_TYPE.PNC_REGISTRATION+"' or visit_type = '"+HnppConstants.EventType.PNC_HOME_VISIT+"'" ;

        }else{
            mainCondition= " where visit_type ='"+visitType+"'";
        }
        String query;
        if(TextUtils.isEmpty(ssName) && TextUtils.isEmpty(month)){
            query = MessageFormat.format("select count(*) as count from {0} {1}", "ec_visit_log", mainCondition);
        }else{
            query = MessageFormat.format("select count(*) as count from {0} {1}", "ec_visit_log", getVisitFilterCondition(ssName,month,year,mainCondition));

        }
        Log.v("WORK_SUMMERY","visit_type:"+query);

        Cursor cursor = null;
        // try {
        cursor = CoreChwApplication.getInstance().getRepository().getReadableDatabase().rawQuery(query, new String[]{});
        if(cursor !=null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                dashBoardData1.setCount(cursor.getInt(0));
                dashBoardData1.setEventType(visitType);
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
    public String getFilterCondition(String ssName, String month, String year){
        StringBuilder build = new StringBuilder();
        build.append(MessageFormat.format(" inner join {0} ", CoreConstants.TABLE_NAME.FAMILY));
        build.append(MessageFormat.format(" on {0}.{1} = {2}.{3} ", CoreConstants.TABLE_NAME.FAMILY, DBConstants.KEY.BASE_ENTITY_ID,
                CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.RELATIONAL_ID));
        if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" where {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%y', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+year+"'"));

        }
        else if(!TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" where {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%y', datetime("+CoreConstants.TABLE_NAME.FAMILY_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+year+"'"));

        }
        else if(!TextUtils.isEmpty(ssName)){
            build.append(MessageFormat.format(" where {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));

        }

        return build.toString();
    }
    public String getAncFilterCondition(String ssName, String month){
        StringBuilder build = new StringBuilder();

        if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.ANC_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));

        }
        else if(!TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.ANC_MEMBER+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));

        }
        else if(!TextUtils.isEmpty(ssName)){
            build.append(MessageFormat.format(" and {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));

        }

        return build.toString();
    }
    public String getDeliveryFilterCondition(String ssName, String month){
        StringBuilder build = new StringBuilder();

        if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.ANC_PREGNANCY_OUTCOME+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));

        }
        else if(!TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0}.{1} is null and {2}  = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED,"strftime('%m', datetime("+CoreConstants.TABLE_NAME.ANC_PREGNANCY_OUTCOME+"."+DBConstants.KEY.LAST_INTERACTED_WITH+"/1000,'unixepoch','localtime'))" ,"'"+month+"'"));

        }
        else if(!TextUtils.isEmpty(ssName)){
            build.append(MessageFormat.format(" and {0}.{1} is null and {2} = {3}", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.DATE_REMOVED, HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));

        }

        return build.toString();
    }
    public String getVisitFilterCondition(String ssName, String month, String year, String mainCondition){
        StringBuilder build = new StringBuilder();
//        if(!TextUtils.isEmpty(ssName)){
//            build.append(MessageFormat.format(" inner join {0} ", CoreConstants.TABLE_NAME.FAMILY_MEMBER));
//            build.append(MessageFormat.format(" on {0}.{1} = {2}.{3} ", CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.BASE_ENTITY_ID,
//                    "visits", DBConstants.KEY.BASE_ENTITY_ID));
//            build.append(MessageFormat.format(" inner join {0} ", CoreConstants.TABLE_NAME.FAMILY));
//            build.append(MessageFormat.format(" on {0}.{1} = {2}.{3} ", CoreConstants.TABLE_NAME.FAMILY, DBConstants.KEY.BASE_ENTITY_ID,
//                    CoreConstants.TABLE_NAME.FAMILY_MEMBER, DBConstants.KEY.RELATIONAL_ID));
//        }

        build.append(mainCondition);
        if(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0} = {1} ", HnppConstants.KEY.SS_NAME,"'"+ssName+"'"));
            build.append(MessageFormat.format(" and {0} = {1} ", "strftime('%m', datetime(ec_visit_log.visit_date/1000,'unixepoch','localtime'))" ,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0} = {1} ", "strftime('%y', datetime(ec_visit_log.visit_date/1000,'unixepoch','localtime'))" ,"'"+year+"'"));

        }
        else if(!TextUtils.isEmpty(month)){
            build.append(MessageFormat.format(" and {0} = {1} ", "strftime('%m', datetime(ec_visit_log.visit_date/1000,'unixepoch','localtime'))" ,"'"+month+"'"));
            build.append(MessageFormat.format(" and {0} = {1} ", "strftime('%y', datetime(ec_visit_log.visit_date/1000,'unixepoch','localtime'))" ,"'"+year+"'"));

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
