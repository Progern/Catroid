package org.catrobat.catroid.cast;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.widget.RelativeLayout;

import com.google.android.gms.cast.CastPresentation;
import com.google.android.gms.cast.CastRemoteDisplayLocalService;

import org.catrobat.catroid.R;
import org.catrobat.catroid.utils.ToastUtil;

public class CastService extends CastRemoteDisplayLocalService {

    private Display display;
    private CastPresentation presentation;


    @Override
    public void onCreatePresentation(Display display) {
        createPresentation(display);
    }

    @Override
    public void onDismissPresentation() {
        dismissPresentation();
    }

    private void dismissPresentation() {
        if (presentation != null) {
            presentation.dismiss();
            presentation = null;
        }
    }

    public void createPresentation(Display display) {
        if (display != null) {
            this.display = display;
        }
        dismissPresentation();
        presentation = new FirstScreenPresentation(this, this.display);

        try {
            presentation.show();
        } catch (Exception ex) {
            ToastUtil.showError(getApplicationContext(), getString(R.string.cast_error_not_connected_msg));
            dismissPresentation();
        }
    }

    public class FirstScreenPresentation extends CastPresentation {

        public FirstScreenPresentation(Context serviceContext, Display display) {
            super(serviceContext, display);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            RelativeLayout layout = new RelativeLayout(getApplication());
            Drawable drawable = ContextCompat
                    .getDrawable(getApplicationContext(), R.drawable.idle_screen_1);
            layout.setBackground(drawable);
            setContentView(layout);
            synchronized (this) {
                CastManager.getInstance().setIsConnected(true);
            }
        }
    }
}
