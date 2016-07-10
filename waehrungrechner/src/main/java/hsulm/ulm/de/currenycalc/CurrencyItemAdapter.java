package hsulm.ulm.de.currenycalc;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import hsulm.ulm.de.currenycalc.Data.ExchangeRateDatabase;

/**
 * Created by Matze_ on 31.05.2016.
 */
public class CurrencyItemAdapter extends BaseAdapter {
    static String TAG ="CurrencyItemAdapter";
    ExchangeRateDatabase rateDb;
    public CurrencyItemAdapter(ExchangeRateDatabase db) {
        rateDb = db;
    }
    @Override
    public int getCount() {
        return rateDb.getCurrencies().
                length;
    }
    @Override
    public Object getItem(int position) {
        return rateDb.getCurrencies()[position];
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        String currencyName = rateDb.getCurrencies()[position];
        LayoutInflater inflater =
                (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = convertView;
        ImageView mCurrencyImage;
        TextView mName;
        TextView mCurr;

        if (root == null) {
            root = inflater.inflate(R.layout.list_view_item, null, false);

        }
        mCurrencyImage = (ImageView) root.findViewById(R.id.list_imageView);
        mName = (TextView) root.findViewById(R.id.textView);
        mCurr = (TextView) root.findViewById(R.id.textView2);
        Log.d(TAG, "try to find flag_"+currencyName.toLowerCase());
        int imageId = context.getResources
                ().getIdentifier("flag_"+currencyName.toLowerCase(),"drawable", context.getPackageName());
        mCurrencyImage.setImageDrawable(ContextCompat.getDrawable(context,imageId));
        mName.setText(currencyName);
        mCurr.setText(rateDb.getExchangeRate(currencyName)+"");
        return root;
    }
}