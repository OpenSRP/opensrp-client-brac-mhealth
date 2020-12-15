package org.smartregister.brac.hnpp.interactor;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.http.NoHttpResponseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opensrp.api.domain.Client;
import org.smartregister.CoreLibrary;
import org.smartregister.brac.hnpp.contract.SearchDetailsContract;
import org.smartregister.brac.hnpp.model.Migration;
import org.smartregister.family.util.AppExecutors;
import org.smartregister.service.HTTPAgent;

import java.util.ArrayList;

public class SearchDetailsInteractor implements SearchDetailsContract.Interactor {
    private AppExecutors appExecutors;
    private ArrayList<Migration> migrationArrayList = new ArrayList<>();
    private static final String MEMBER_URL = "/rest/client/search-client?";


    public SearchDetailsInteractor(AppExecutors appExecutors){
        this.appExecutors = appExecutors;
    }
    private void addMember(String villageId, String gender, String age){
        migrationArrayList.clear();
        JSONArray jsonArray = getMigrationMemberList(villageId, gender, age);
        if(jsonArray!=null){
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Migration migration = new Gson().fromJson(object.toString(), Migration.class);
                    if (migration != null) {
                        migrationArrayList.add(migration);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void fetchData(String villageId, String gender, String age, SearchDetailsContract.InteractorCallBack callBack) {
        Runnable runnable = () -> {
            addMember(villageId, gender, age);
            appExecutors.mainThread().execute(() -> callBack.onUpdateList(migrationArrayList));
        };
        appExecutors.diskIO().execute(runnable);
    }

    private JSONArray getMigrationMemberList(String villageId, String gender, String age){
        villageId = "9315";
        gender ="F";
        age ="50";
        try {
            HTTPAgent httpAgent = CoreLibrary.getInstance().context().getHttpAgent();
            String baseUrl = CoreLibrary.getInstance().context().
                    configuration().dristhiBaseURL();
            String endString = "/";
            if (baseUrl.endsWith(endString)) {
                baseUrl = baseUrl.substring(0, baseUrl.lastIndexOf(endString));
            }
            String userName = CoreLibrary.getInstance().context().allSharedPreferences().fetchRegisteredANM();
            if (TextUtils.isEmpty(userName)) {
                return null;
            }
            String url = baseUrl + MEMBER_URL + "username='"+userName+"'&villageId=" + villageId + "&gender=" + gender + "&startAge=0&endAge=" + age + "&type=Member";
            /*+ "?username=" + userName;*/

            Log.v("MEMBER_URL", "url:" + url);
            org.smartregister.domain.Response resp = httpAgent.fetch(url);
            if (resp.isFailure()) {
                throw new NoHttpResponseException(MEMBER_URL + " not returned data");
            }
            JSONObject jsonObject = new JSONObject((String)resp.payload());
            JSONArray jsonArray = jsonObject.getJSONArray("clients");
            return jsonArray;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
