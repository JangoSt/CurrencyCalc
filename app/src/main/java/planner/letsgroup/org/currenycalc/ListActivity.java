package planner.letsgroup.org.currenycalc;

import android.content.Intent;
import android.net.Uri;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Locale;

public class ListActivity extends AppCompatActivity {
   ListView mCurrList;
    CurrencyItemAdapter mCurrencyItemAdapter;
    ExchangeRateDatabase mExchangeRateDatabase;
    boolean isInEditMode = false;
    AdapterView.OnItemClickListener mInEditModeListener;
    AdapterView.OnItemClickListener mInNormalModeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mCurrList = (ListView) findViewById(R.id.listView);

        mExchangeRateDatabase = new ExchangeRateDatabase();
        mCurrencyItemAdapter = new CurrencyItemAdapter(mExchangeRateDatabase);
        mCurrList.setAdapter(mCurrencyItemAdapter);

        mCurrList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mInNormalModeListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String query = mExchangeRateDatabase.getCapital(mCurrencyItemAdapter.getItem(position).toString());
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=%s", query);
//                String uri = String.format(Locale.ENGLISH, "comgooglemaps://?q=%s", query);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        };
        mInEditModeListener =new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ChangeCurrencyDialog editNameDialog = new ChangeCurrencyDialog();
                Bundle bundle = new Bundle();
                bundle.putString(ChangeCurrencyDialog.CHANGECURR_LANG_VALUE,mCurrencyItemAdapter.getItem(position).toString());
                editNameDialog.setArguments(bundle);
                editNameDialog.show(getFragmentManager(), "fragment_edit_name");
            }
        };
        mCurrList.setOnItemClickListener(mInNormalModeListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                isInEditMode = !isInEditMode;
                if(isInEditMode) {
                    item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit_active));
                    mCurrList.setOnItemClickListener(mInEditModeListener);
//                    mCurrList.setAdapter(new CurrencyItemChangeAdapter(mExchangeRateDatabase));
                }
                else {
                    item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_edit));
                    mCurrList.setOnItemClickListener(mInNormalModeListener);
//                    mCurrList.setAdapter(new CurrencyItemAdapter(mExchangeRateDatabase));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
