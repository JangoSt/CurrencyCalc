package hsulm.ulm.de.currenycalc.Data;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Matze_ on 21.06.2016.
 */
public class NetworkHelper {
    String TAG = "NetworkHelper";
    Handler mMainHandler;
    ExchangeRateDatabase mExchangeRateDatabase;
    NetworkHelperListener mNetworkHelperListener;
    SettingsHelper mSettingsHelper;
    interface NetworkHelperListener{
        public boolean onUpdatePerformed();
    }

    public NetworkHelper(NetworkHelperListener networkHelperListener) {
        mExchangeRateDatabase = new ExchangeRateDatabase();
        mMainHandler = new Handler(Looper.getMainLooper());
        mNetworkHelperListener = networkHelperListener;
    }

    public void makeUpdate(){
        // Create an object for subclass of AsyncTask
        DownloadXMLTask task = new DownloadXMLTask();
        // Execute the task
        task.execute("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
    }
    private class DownloadXMLTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            try{
                URL url=new URL(params[0]);
                URLConnection urlConnection =url.openConnection();
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(urlConnection.getInputStream(),urlConnection.getContentEncoding());
                int eventType = parser.getEventType();
                Log.d(TAG,"Starting Task = "+parser.getEventType());
                while (eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType==XmlPullParser.START_TAG){
                        if(parser.getName()!=null)
                        if(TextUtils.equals("cube", parser.getName().toLowerCase())){
                            String exchangeCurrency ="";
                            String exchangeValue = "";
                            boolean isCurr=false;
                            for (int i = 0; i <parser.getAttributeCount() ; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if(TextUtils.equals(name, "time")){
                                    mExchangeRateDatabase.setDate(value);
                                }
                                else if(TextUtils.equals(name, "currency")){
                                    exchangeCurrency = value;
                                    isCurr = true;
                                }
                                else if(TextUtils.equals(name, "rate")){
                                    exchangeValue = value;
                                    isCurr = true;
                                }

                                Log.d(TAG,"name = "+name+"value = "+value);
                            }
                            if(isCurr)
                                mExchangeRateDatabase.setExchangeRate(exchangeCurrency,Double.parseDouble(exchangeValue));
                        }
                    }
                    eventType=parser.next();

                }
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mNetworkHelperListener.onUpdatePerformed();
                    }
                });

                return "";

            }catch (IOException ioEx){

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
