/*
 *  Pdf display copied & modified from PdfRenderBasic sample
 *  https://developer.android.com/samples/PdfRendererBasic/index.html
 */

package com.benjaminsklar.ra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RulesActivity extends AppCompatActivity {

    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_rule, new RulesFragment(),
                            FRAGMENT_PDF_RENDERER_BASIC)
                    .commit();
        }
    }
}
