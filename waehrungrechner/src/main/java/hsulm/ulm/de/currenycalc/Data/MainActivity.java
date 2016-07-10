package hsulm.ulm.de.currenycalc.Data;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import hsulm.ulm.de.currenycalc.CurrencyItemAdapter;
import hsulm.ulm.de.currenycalc.ListActivity;
import hsulm.ulm.de.currenycalc.R;

public class MainActivity extends AppCompatActivity {
    Spinner mSpinFrom;
    Spinner mSpinTo;
    EditText mFromText;
    Button mCalcButton;
    TextView mResultText;
    TextView mLAstDate;
    CurrencyItemAdapter mCurrencyItemAdapter;
    ExchangeRateDatabase mExchangeRateDatabase;
    ShareActionProvider shareActionProvider;
    NetworkHelper.NetworkHelperListener mNetworkHelperListener;
    SettingsHelper mSettingsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinFrom = (Spinner) findViewById(R.id.main_spin_from);
        mSpinTo = (Spinner) findViewById(R.id.main_spin_to);
        mFromText = (EditText) findViewById(R.id.main_editText);
        mCalcButton = (Button) findViewById(R.id.main_calcBtn);
        mResultText = (TextView) findViewById(R.id.main_resultText);
        mLAstDate = (TextView) findViewById(R.id.main_lastdate);

        mSettingsHelper = new SettingsHelper(getApplicationContext());
        mExchangeRateDatabase = new ExchangeRateDatabase();
        mCurrencyItemAdapter = new CurrencyItemAdapter(mExchangeRateDatabase);
        mSpinFrom.setAdapter(mCurrencyItemAdapter);
        mSpinTo.setAdapter(mCurrencyItemAdapter);

        mCalcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewCalc();
            }
        });


        mSpinTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                makeNewCalc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSpinFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                makeNewCalc();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mNetworkHelperListener = new NetworkHelper.NetworkHelperListener() {
            @Override
            public boolean onUpdatePerformed() {
                updateUI();
                Toast.makeText(getApplicationContext(),"Values updated !",Toast.LENGTH_SHORT).show();
                return false;
            }
        };
        NetworkHelper networkHelper = new NetworkHelper(mNetworkHelperListener);
        networkHelper.makeUpdate();
        mSettingsHelper.loadCurrDatabase();
        updateUI();

    }

    private void updateUI() {
        mLAstDate.setText(mExchangeRateDatabase.getFormatedDate(getApplicationContext()));
        mCurrencyItemAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        setShareText(null);
        return true;
    }

    private void setShareText(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (text != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareActionProvider.setShareIntent(shareIntent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
                break;
            case R.id.action_share:
                intent = new Intent(getApplicationContext(), ListActivity.class);
                startActivity(intent);
                break;
            case R.id.action_update:
                NetworkHelper networkHelper = new NetworkHelper(mNetworkHelperListener);
                networkHelper.makeUpdate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeNewCalc() {
        if (!TextUtils.isEmpty(mFromText.getText())) {
            double textParsed = Double.parseDouble(mFromText.getText().toString());
            double result = mExchangeRateDatabase.convert(textParsed,
                    mSpinFrom.getSelectedItem().toString(),
                    mSpinTo.getSelectedItem().toString());
            result = ((int) (result * 100)) / 100.0;
            String retText = mFromText.getText().toString() + " " + mSpinFrom.getSelectedItem() + " = " + result + " " + mSpinTo.getSelectedItem().toString();
            if(shareActionProvider!=null)
            setShareText(retText);
            mResultText.setText(retText);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpinFrom.setSelection(mSettingsHelper.getFromIndex());
        mSpinTo.setSelection(mSettingsHelper.getToIndex());
        mFromText.setText(mSettingsHelper.getValue()+"");

    }

    @Override
    protected void onPause() {
        super.onPause();
        mSettingsHelper.saveFromAndToIndex(mSpinFrom.getSelectedItemPosition(),mSpinTo.getSelectedItemPosition());
        mSettingsHelper.saveValue(Double.parseDouble(mFromText.getText().toString()));
        mSettingsHelper.saveCurrDatabase();
    }
}
