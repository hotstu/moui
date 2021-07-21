package io.github.hotstu.moui.arch;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.github.hotstu.moui.util.MOStatusBarHelper;

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
