package org.smartregister.brac.hnpp.repository;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import org.smartregister.AllConstants;
import org.smartregister.chw.anc.repository.VisitDetailsRepository;
import org.smartregister.chw.anc.repository.VisitRepository;
import org.smartregister.chw.core.application.CoreChwApplication;
import org.smartregister.chw.core.repository.CoreChwRepository;
import org.smartregister.brac.hnpp.BuildConfig;
import timber.log.Timber;

public class HnppChwRepository extends CoreChwRepository {
    private Context context;

    public HnppChwRepository(Context context, org.smartregister.Context openSRPContext) {
        super(context, AllConstants.DATABASE_NAME, BuildConfig.DATABASE_VERSION, openSRPContext.session(), CoreChwApplication.createCommonFtsObject(), openSRPContext.sharedRepositoriesArray());
        this.context = context;
    }

    public void deleteDatabase(){
        context.deleteDatabase(AllConstants.DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        super.onCreate(database);
    }

    @Override
    protected void onCreation(SQLiteDatabase database) {
        SSLocationRepository.createTable(database);
        HouseholdIdRepository.createTable(database);
        VisitRepository.createTable(database);
        VisitDetailsRepository.createTable(database);
        HnppVisitLogRepository.createTable(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.w(HnppChwRepository.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 10:
                    upgradeToVersion10(context,db);
                    break;
                case 9:
                    upgradeToVersion9(context, db);
                    break;
                case 8:
                    upgradeToVersion8(context, db);
                    break;
                case 11:
                    upgradeToVersion11(context, db);
                    break;
                case 12:
                    upgradeToVersion12(context, db);
                    break;

                default:
                    break;
            }
            upgradeTo++;
        }
    }


    private void upgradeToVersion9(Context context, SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE ec_child ADD COLUMN birth_weight_taken VARCHAR;");
            db.execSQL("ALTER TABLE ec_child ADD COLUMN birth_weight VARCHAR;");
            db.execSQL("ALTER TABLE ec_child ADD COLUMN chlorohexadin VARCHAR;");
            db.execSQL("ALTER TABLE ec_child ADD COLUMN breastfeeding_time VARCHAR;");
            db.execSQL("ALTER TABLE ec_child ADD COLUMN head_body_covered VARCHAR;");
            db.execSQL("ALTER TABLE ec_child ADD COLUMN breast_feeded VARCHAR;");

        } catch (Exception e) {

        }
    }
    private void upgradeToVersion10(Context context, SQLiteDatabase db) {
        try{
            db.execSQL("ALTER TABLE ec_family ADD COLUMN serial_no VARCHAR;");
            db.execSQL("ALTER TABLE ec_family_member ADD COLUMN mother_name VARCHAR;");
            db.execSQL("ALTER TABLE ec_family_member ADD COLUMN mother_entity_id VARCHAR;");
        }catch (Exception e){

        }
        try{
            db.execSQL("ALTER TABLE ec_family ADD COLUMN occupation VARCHAR;");
            db.execSQL("ALTER TABLE ec_family ADD COLUMN occupation_other VARCHAR;");
            db.execSQL("ALTER TABLE ec_family ADD COLUMN financial_status VARCHAR;");
            db.execSQL("ALTER TABLE ec_family ADD COLUMN list_of_assets VARCHAR;");
            db.execSQL("ALTER TABLE ec_family ADD COLUMN floor_material VARCHAR;");
            db.execSQL("ALTER TABLE ec_family ADD COLUMN wall_material VARCHAR;");
            db.execSQL("ALTER TABLE ec_family ADD COLUMN roof_material VARCHAR;");
        }catch (Exception e){

        }


    }
    private void upgradeToVersion8(Context context, SQLiteDatabase db) {
        try{
            db.execSQL("UPDATE client set syncStatus='Unsynced' where syncStatus='Synced'");
            db.execSQL("UPDATE event set syncStatus='Unsynced',serverVersion= 0");

        }catch (Exception e){
            Timber.w(HnppChwRepository.class.getName(),"update client problem"+e);
        }
    }
    private void upgradeToVersion11(Context context, SQLiteDatabase db) {

       try{
           db.execSQL("CREATE TABLE ec_visit_log (visit_id VARCHAR,visit_type VARCHAR,base_entity_id VARCHAR NOT NULL,visit_date VARCHAR,event_type VARCHAR,visit_json TEXT)");
       }catch (SQLiteException e){

       }

    }
    private void upgradeToVersion12(Context context, SQLiteDatabase db) {

        try{
            db.execSQL("ALTER TABLE ec_anc_register ADD COLUMN height VARCHAR;");
        }catch (SQLiteException e){
            Timber.w(HnppChwRepository.class.getName(),"ALTER TABLE ec_anc_register"+e);
        }

    }

}
