package net.sakuramilk.TweakGS2.Parts;

import net.sakuramilk.TweakGS2.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Button;

public class WizardPreferenceActivity extends PreferenceActivity {

    protected Button mBackButton;
    protected Button mNextButton;

    public interface OnPageEventListener {
        public void onPageEvent(int eventCode, Object objValue);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard);
        
        mBackButton = (Button)findViewById(R.id.back_button);
        mNextButton = (Button)findViewById(R.id.next_button);
    }
}
