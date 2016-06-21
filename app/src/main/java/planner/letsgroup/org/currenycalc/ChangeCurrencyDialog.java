package planner.letsgroup.org.currenycalc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeCurrencyDialog extends DialogFragment {
    public static String TAG = "ChangeCurrencyDialog";
    public static String CHANGECURR_LANG_VALUE = "CHANGECURR_LANG_VALUE";
    View mRoot;
    String mLang ="";
    ExchangeRateDatabase mExchangeRateDatabase;
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mRoot = inflater.inflate(R.layout.activity_change_currency, null);

        builder.setTitle(getResources().getString(R.string.edit_currency_text));
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(mRoot)
                // Add action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        String editTextVal = ((EditText)mRoot.findViewById(R.id.currchange_edittext)).getText().toString();
                        Log.d(TAG, "EditTeext = "+editTextVal);
                        mExchangeRateDatabase.setExchangeRate(mLang, Double.parseDouble(editTextVal));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeCurrencyDialog.this.getDialog().cancel();
                    }
                });
        mExchangeRateDatabase = new ExchangeRateDatabase();
        setViewValues();
        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mLang = bundle.getString(CHANGECURR_LANG_VALUE);
        if(TextUtils.isEmpty(mLang))
            throw new RuntimeException("Soryy no Language given");
        super.onCreate(savedInstanceState);
    }

    private void setViewValues() {
        ((TextView)mRoot.findViewById(R.id.currchange_info)).setText(mLang);
        ((TextView)mRoot.findViewById(R.id.currchange_edittext)).setText(mExchangeRateDatabase.getExchangeRate(mLang)+"");

    }
}
