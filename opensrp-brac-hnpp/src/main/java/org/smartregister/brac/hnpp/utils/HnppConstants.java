package org.smartregister.brac.hnpp.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.brac.hnpp.BuildConfig;
import org.smartregister.chw.core.utils.CoreConstants;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class HnppConstants extends CoreConstants {
    public static final String TEST_GU_ID = "test";
    public static final String MODULE_ID_TRAINING = "TRAINING";
    public static final int MEMBER_ID_SUFFIX = 11;
    public static final int HOUSE_HOLD_ID_SUFFIX = 9;
    public static final String IS_RELEASE = "is_release_build";
    public static final String IS_DEVICE_VERIFY = "is_device_verify";


    public static boolean isExistSpecialCharacter(String filters) {
        if (!TextUtils.isEmpty(filters) && filters.contains("/")) {
            return true;
        }
        return false;
    }

    public static void updateAppBackground(View view) {
        if (!isReleaseBuild()) {
            view.setBackgroundColor(Color.parseColor("#B53737"));
        }
    }

    public static void updateAppBackgroundOnResume(View view) {
        if (!isReleaseBuild()) {
            view.setBackgroundColor(Color.parseColor("#B53737"));
        } else {
            view.setBackgroundColor(Color.parseColor("#F6F6F6"));
        }
    }

    public static ArrayList<String> getClasterSpinnerArray() {

        return new ArrayList<>(getClasterNames().keySet());
    }

    public static String getClusterNameFromValue(String value) {
        HashMap<String, String> keys = getClasterNames();
        for (String key : keys.keySet()) {
            if (keys.get(key).equalsIgnoreCase(value)) {
                return key;
            }
        }
        return "";
    }

    public static HashMap<String, String> getClasterNames() {
        LinkedHashMap<String, String> clusterArray = new LinkedHashMap<>();
        clusterArray.put("ক্লাস্টার ১", "1st_Cluster");
        clusterArray.put("ক্লাস্টার ২", "2nd_Cluster");
        clusterArray.put("ক্লাস্টার ৩", "3rd_Cluster");
        clusterArray.put("ক্লাস্টার ৪", "4th_Cluster");
        return clusterArray;
    }

    public static final class DrawerMenu {
        public static final String ELCO_CLIENT = "Elco Clients";
        public static final String ALL_MEMBER = "All member";
    }

    public static final class FORM_KEY {
        public static final String SS_INDEX = "ss_index";
        public static final String VILLAGE_INDEX = "village_index";
    }

    public static String getTotalCountBn(int count) {
        char[] bn_numbers = "০১২৩৪৫৬৭৮৯".toCharArray();
        String c = String.valueOf(count);
        String number_to_return = "";
        for (char ch : c.toCharArray()) {

            number_to_return += bn_numbers[Integer.valueOf(ch) % Integer.valueOf('0')];
        }
        return number_to_return;
    }

    public static boolean isReleaseBuild() {
        AllSharedPreferences preferences = Utils.getAllSharedPreferences();
        String isReleaseBuild = preferences.getPreference(IS_RELEASE);
        if (TextUtils.isEmpty(isReleaseBuild) || isReleaseBuild.equalsIgnoreCase("L")) {
            return true;
        }
        return false;
    }

    public static String getDeviceId(TelephonyManager mTelephonyManager, Context context,boolean fromSettings) {
        String deviceId = null;
        if (mTelephonyManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                deviceId = mTelephonyManager.getDeviceId(1);
                if(fromSettings){
                    deviceId = deviceId+"\n"+mTelephonyManager.getDeviceId(2);
                }
            }

        }
        return deviceId;
    }
    public static boolean isDeviceVerified(){
        AllSharedPreferences preferences = Utils.getAllSharedPreferences();
        String isDeviceVerif = preferences.getPreference(IS_DEVICE_VERIFY);
        if(!TextUtils.isEmpty(isDeviceVerif) && isDeviceVerif.equalsIgnoreCase("V")){
            return true;
        }
        return false;
    }
    public static void updateLiveTest(String appMode){
        AllSharedPreferences preferences = Utils.getAllSharedPreferences();
        preferences.savePreference(IS_RELEASE,appMode);
    }

    private String getAppModeFromFile(){
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"mhealth_app_mode.txt");
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }
    public static void updateDeviceVerified(boolean isVerify){
        AllSharedPreferences preferences = Utils.getAllSharedPreferences();
        preferences.savePreference(IS_DEVICE_VERIFY,isVerify?"V":"");
    }
    public static String getSimPrintsProjectId(){

        return isReleaseBuild()?BuildConfig.SIMPRINT_PROJECT_ID_RELEASE:BuildConfig.SIMPRINT_PROJECT_ID_TRAINING;
    }
    public static final class KEY {
        public static final String TOTAL_MEMBER = "member_count";
        public static final String VILLAGE_NAME = "village_name";
        public static final String CLASTER = "claster";
        public static final String MODULE_ID = "module_id";
        public static final String RELATION_WITH_HOUSEHOLD = "relation_with_household_head";
        public static final String GU_ID = "gu_id";
        public static final String HOUSE_HOLD_ID = "house_hold_id";
        public static final String HOUSE_HOLD_NAME = "house_hold_name";
        public static final String SS_NAME = "ss_name";
        public static final String SERIAL_NO = "serial_no";
        public static final String CHILD_MOTHER_NAME_REGISTERED = "mother_name";
        public static final String CHILD_MOTHER_NAME = "Mother_Guardian_First_Name_english";
        public static final String ID_AVAIL = "id_avail";
        public static final String NATIONAL_ID = "national_id";
        public static final String BIRTH_ID = "birth_id";
        public static final String IS_BITHDAY_KNOWN = "is_birthday_known";
        public static final String BLOOD_GROUP = "blood_group";
    }

    public static class IDENTIFIER {
        public static final String FAMILY_TEXT = "Family";

        public IDENTIFIER() {
        }
    }

    public static String getRelationWithHouseholdHead(String value){
        try {
            JSONObject choiceObject = new JSONObject(relationshipObject);
            for (int i = 0; i < choiceObject.names().length(); i++) {
                if (value.equalsIgnoreCase(choiceObject.getString(choiceObject.names().getString(i)))) {
                    value = choiceObject.names().getString(i);
                    return value;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }
    public static String relationshipObject = "{" +
            "  \"খানা প্রধান\": \"Household Head\"," +
            "  \"মা/আম্মা\": \"Mother\"," +
            "  \"বাবা/আব্বা\": \"Father\"," +
            "  \"ছেলে\": \"Son\"," +
            "  \"মেয়ে\": \"Daughter\"," +
            "  \"স্ত্রী\": \"Wife\"," +
            "  \"স্বামী\": \"Husband\"," +
            "  \"নাতি\": \"Grandson\"," +
            "  \"নাতনী\": \"GrandDaughter\"," +
            "  \"ছেলের বউ\": \"SonsWife\"," +
            "  \"মেয়ের স্বামী\": \"DaughtersHusband\"," +
            "  \"শ্বশুর\": \"Father in law\"," +
            "  \"শাশুড়ি\": \"Mother in law\"," +
            "  \"দাদা\": \"Grandpa\"," +
            "  \"দাদি\": \"Grandma\"," +
            "  \"নানা\": \"Grandfather\"," +
            "  \"নানী\": \"Grandmother\"," +
            "  \"অন্যান্য\": \"Others\"" +
            "}";
}
