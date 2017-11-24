package fr.utt.if26.zouhairi_kadiri_application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Activity_Base extends AppCompatActivity {
    ImageView main_image, menu_image, img_back, img_plus;
    RelativeLayout rel_top, wrapper;
    RelativeLayout lin_menu, lin_call, lin_request;
    LinearLayout lin_bottom;
    TextView txt_heading;
    ImageView iv_menu, iv_call, iv_req, iv_menu_selected, iv_call_selected,
            iv_req_selected, img_cart;


    public String order_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        main_image = (ImageView) findViewById(R.id.image_home);
        rel_top = (RelativeLayout) findViewById(R.id.rel_base);
        lin_bottom = (LinearLayout) findViewById(R.id.linear_base);
        menu_image = (ImageView) findViewById(R.id.img_res);
        wrapper = (RelativeLayout) findViewById(R.id.rel_wrapper);
        lin_menu = (RelativeLayout) findViewById(R.id.lin_menu);
        lin_call = (RelativeLayout) findViewById(R.id.lin_callwaiter);
        lin_request = (RelativeLayout) findViewById(R.id.lin_request);
        iv_menu = (ImageView) findViewById(R.id.img_menu);
        iv_menu_selected = (ImageView) findViewById(R.id.img_menu_selected);
        iv_call = (ImageView) findViewById(R.id.img_waiter);
        iv_call_selected = (ImageView) findViewById(R.id.img_waiter_selected);
        iv_req = (ImageView) findViewById(R.id.img_request);
        iv_req_selected = (ImageView) findViewById(R.id.img_request_selected);
        txt_heading = (TextView) findViewById(R.id.txt_heading);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_plus = (ImageView) findViewById(R.id.img_plus);
        img_cart = (ImageView) findViewById(R.id.img_cart);


        lin_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Base.this, Activity_home.class);
                i.putExtra("order_id", order_ID);
                startActivity(i);
                iv_menu.setVisibility(View.GONE);
                iv_menu_selected.setVisibility(View.VISIBLE);
                iv_call.setVisibility(View.VISIBLE);
                iv_call_selected.setVisibility(View.GONE);
                iv_req.setVisibility(View.VISIBLE);
                iv_req_selected.setVisibility(View.GONE);
                // finish();
            }
        });
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Base.this, Activity_Confirm_Order.class);
                startActivity(i);
            }
        });

        iv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LOG ", "ORDER ID" + order_ID);
                if (lin_call.isClickable()) {
                    iv_menu.setVisibility(View.VISIBLE);
                    iv_menu_selected.setVisibility(View.GONE);
                    iv_call.setVisibility(View.VISIBLE);
                    iv_call_selected.setVisibility(View.VISIBLE);
                    iv_req.setVisibility(View.VISIBLE);
                    iv_req_selected.setVisibility(View.GONE);
                    /*
                    if (AppConstants.order_ID_call_waiter != null) {
                        Call_Waiter_Task call_Task = new Call_Waiter_Task(
                                Activity_Base.this,
                                AppConstants.order_ID_call_waiter);
                        call_Task.execute();
                    } else {
                        Toast.makeText(Activity_Base.this,
                                "Please scan your QR code on your table!!",
                                Toast.LENGTH_LONG).show();
                    }
                    */

                } else {

                }

            }
        });

        lin_request.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                iv_menu.setVisibility(View.VISIBLE);
                iv_menu_selected.setVisibility(View.GONE);
                iv_call.setVisibility(View.VISIBLE);
                iv_call_selected.setVisibility(View.GONE);
                iv_req.setVisibility(View.GONE);
                iv_req_selected.setVisibility(View.VISIBLE);
                /*
                if (isCameraAvailable()) {
                    Intent intent = new Intent(Activity_Base.this,
                            ZBarScannerActivity.class);
                    intent.putExtra(ZBarConstants.SCAN_MODES,
                            new int[] { Symbol.QRCODE });
                    startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
                } else {
                    Toast.makeText(Activity_Base.this,
                            "Rear Facing Camera Unavailable",
                            Toast.LENGTH_SHORT).show();
                }
                */
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        img_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Activity_Base.this, Activity_home.class));
            }
        });
    }
}
