package github.hotstu.naiue.arch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import github.hotstu.naiue.util.MOStatusBarHelper;

/**
 * @author hglf
 * @since 2018/7/13
 */
public abstract class MOActivity extends AppCompatActivity {
    private boolean translucent =  false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        translucent = MOStatusBarHelper.translucent(this);
        super.onCreate(savedInstanceState);
    }

    public boolean isTranslucent() {
        return translucent;
    }
}
