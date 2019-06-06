package com.elintminds.mac.metatopos.common;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;

import com.elintminds.mac.metatopos.R;

public class DialogUtils {

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog m_Dialog = new ProgressDialog(context,R.style.ProgressDialogStyle);

        m_Dialog.setCancelable(true);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        m_Dialog.getWindow().setGravity(Gravity.CENTER);
        m_Dialog.show();
        m_Dialog.setContentView(R.layout.progress_dialog_layout);
        return m_Dialog;
    }

}
