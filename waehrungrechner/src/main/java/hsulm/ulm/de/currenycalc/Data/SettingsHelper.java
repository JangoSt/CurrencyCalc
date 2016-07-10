package hsulm.ulm.de.currenycalc.Data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Matze_ on 10.07.2016.
 */
public class SettingsHelper {
    String saveVal_FROM ="editor_from_index";
    String saveVal_TO="editor_to_index";
    String saveVal_VALUE="editor_Value";

    SharedPreferences prefs;
    SharedPreferences.Editor prefsEditor;
    ExchangeRateDatabase mExchangeRateDatabase;

    public SettingsHelper(Context context) {
        prefs  = context.getSharedPreferences("PREF", 0);
        prefsEditor = prefs.edit();
        mExchangeRateDatabase = new ExchangeRateDatabase();
    }

    public void saveFromAndToIndex(int FromIndex, int ToIndex){
        prefsEditor.putInt(saveVal_FROM,FromIndex);
        prefsEditor.putInt(saveVal_TO,ToIndex);
        prefsEditor.commit();
    }
    public void saveValue(double value){
        prefsEditor.putFloat(saveVal_VALUE, (float) value);
        prefsEditor.commit();
    }
    public float getValue(){
       return prefs.getFloat(saveVal_VALUE,0);
    }
    public int getFromIndex(){
        return prefs.getInt(saveVal_FROM,0);
    }
    public int getToIndex(){
       return prefs.getInt(saveVal_TO,0);
    }
    public void saveCurrDatabase(){
        for (String curr:mExchangeRateDatabase.getCurrencies()
             ) {
            prefsEditor.putFloat(curr, (float) mExchangeRateDatabase.getExchangeRate(curr));
        }
        prefsEditor.commit();
    }
    public void loadCurrDatabase(){
        for (String curr:mExchangeRateDatabase.getCurrencies()
                ) {
            prefs.getFloat(curr, (float) mExchangeRateDatabase.getExchangeRate(curr));
        }
    }

}
