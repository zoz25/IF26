package fr.utt.if26.zouhairi_kadiri_application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Activity_product extends Activity_Base {
    LayoutInflater inflater;
    ListView list_product;
    RelativeLayout rel_product;
    Button btn_addToCart;
    Product_Adapter adapter;
    String category_id, category_name;
    Product_Task product_Task;
    List<Product_bean> product_data;
    Product_bean my_product;
    Product_bean add_tocartData;
    Product_Adapter product_Adapter;
    Add_to_Cart_Task add_to_Cart_Task;
    String is_selected;
    ArrayList<Product_bean> added_Data;
    ArrayList<Product_bean> qty_array;
    ArrayList<Boolean> ischecked;
    String price = "", product_id = "", type = "", quantity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = getLayoutInflater();
        rel_product = (RelativeLayout) inflater.inflate(
                R.layout.activity_product, null);
        wrapper.addView(rel_product);
        list_product = (ListView) findViewById(R.id.list_product);
        btn_addToCart = (Button) findViewById(R.id.btn_add);
        rel_top.setVisibility(View.VISIBLE);
        main_image.setVisibility(View.GONE);

        lin_bottom.setVisibility(View.GONE);

        ErrorReporter1 errReporter = new ErrorReporter1();
        errReporter.Init(Activity_product.this);
        errReporter.CheckErrorAndSendMail(Activity_product.this);

        Intent i = getIntent();
        category_id = i.getExtras().getString("cat_id");
        category_name = i.getExtras().getString("item_name");
        // order_id = i.getExtras().getString("order_id");
        Log.e("LOG", "PRODUCT " + AppConstants.order_ID);
        txt_heading.setText(category_name);

        if (category_id != null && category_id.length() > 0) {
            product_Task = new Product_Task(Activity_product.this);
            product_Task.execute();
        }
        img_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                product_data.clear();
                my_product.release();
            }
        });

        btn_addToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("LOG", "ADDED ORDER :" + added_Data.size());
                Log.e("LOG", "ADD ORDER ID:" + AppConstants.order_ID);
                StringBuilder sb = new StringBuilder(qty_array.size());
                if (AppConstants.order_ID != null
                        && AppConstants.order_ID.length() > 0) {
                    for (int j = 0; j < added_Data.size(); j++) {
                        if (j == 0) {
                            if (added_Data.get(j).product_price != null
                                    && added_Data.get(j).product_price.length() > 0) {
                                price = price + added_Data.get(j).product_price;
                            } else if (added_Data.get(j).medium_price != null
                                    && added_Data.get(j).medium_price.length() > 0) {
                                price = price + added_Data.get(j).medium_price;
                            } else if (added_Data.get(j).large_price != null
                                    && added_Data.get(j).large_price.length() > 0) {
                                price = price + added_Data.get(j).large_price;
                            }
                            product_id = product_id + added_Data.get(j).cat_id;
                        } else {
                            if (added_Data.get(j).product_price != null
                                    && added_Data.get(j).product_price.length() > 0) {
                                price = price + "@#"
                                        + added_Data.get(j).product_price;
                            } else if (added_Data.get(j).medium_price != null
                                    && added_Data.get(j).medium_price.length() > 0) {
                                price = price + "@#"
                                        + added_Data.get(j).medium_price;
                            } else if (added_Data.get(j).large_price != null
                                    && added_Data.get(j).large_price.length() > 0) {
                                price = price + "@#"
                                        + added_Data.get(j).large_price;
                            }
                            product_id = product_id + "@#"
                                    + added_Data.get(j).cat_id;
                        }
                    }

                    // add quantity
                    for (int k = 0; k < added_Data.size(); k++) {
                        for (int i = 0; i < qty_array.size(); i++) {
                            if (added_Data.get(k).cat_id.equals(qty_array
                                    .get(i).cat_id)) {
                                if (quantity.equals("")) {
                                    quantity = qty_array.get(i).qty;
                                } else {
                                    quantity = quantity + "@#"
                                            + qty_array.get(i).qty;
                                }
                                break;
                            }
                        }
                    }
                    // call task
                    Log.e("LOG", "Order_ID==> " + AppConstants.order_ID);
                    try {
                        add_to_Cart_Task = new Add_to_Cart_Task(
                                Activity_product.this, URLEncoder.encode(
                                product_id, "utf-8"), URLEncoder
                                .encode(price, "utf-8"),
                                AppConstants.order_ID, URLEncoder.encode(type,
                                "utf-8"), URLEncoder.encode(quantity,
                                "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    add_to_Cart_Task.execute();
                } else {
                    Toast.makeText(
                            Activity_product.this,
                            getResources()
                                    .getString(
                                            R.string.ORDER_ID_IS_NOT_AVAIBLE_PLEASE_SCAN_QRCODE_OUN_YOUR_TABLE),
                            Toast.LENGTH_SHORT).show();
                    // startActivity(new Intent(getApplicationContext(),
                    // Activity_Confirm_Order.class));

                }
            }
        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        product_Task = new Product_Task(Activity_product.this);
        product_Task.execute();
    }

    public class Product_Task extends BaseTask {
        Context context;
        CustomProgressDialog pdialog;

        public Product_Task(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // bar.setVisibility(View.VISIBLE);
            pdialog = new CustomProgressDialog(context, "");
            pdialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

            SimpleListFactory factory = SimpleListFactory.getInstance();
            product_data = factory.get_Product(category_id,
                    AppConstants.order_ID);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pdialog != null) {
                pdialog.dismiss();
            }
            if (product_data != null && product_data.size() > 0) {

                ischecked = null;
                ischecked = new ArrayList<Boolean>();
                added_Data = null;
                added_Data = new ArrayList<Product_bean>();
                added_Data.clear();
                qty_array = null;
                qty_array = new ArrayList<Product_bean>();

                for (int i = 0; i < product_data.size(); i++) {
                    ischecked.add(false);
                    Product_bean pb = new Product_bean();
                    pb = product_data.get(i);
                    pb.qty = "1";
                    qty_array.add(pb);

                }

                product_Adapter = new Product_Adapter(context);
                list_product.setAdapter(product_Adapter);

            } else {
                Toast.makeText(context, "No products found!", Toast.LENGTH_LONG)
                        .show();
            }

        }

        @Override
        public <T extends ResponseData> T getData(int pos) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void release() {
            // TODO Auto-generated method stub

        }

    }

    public class Product_Adapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        int height, width;
        int num;

        public Product_Adapter(Context context) {
            super();
            this.context = context;
            DisplayMetrics displaymetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay()
                    .getMetrics(displaymetrics);
            height = displaymetrics.heightPixels;
            width = displaymetrics.widthPixels;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return product_data.size();
        }

        @Override
        public Product_bean getItem(int position) {
            // TODO Auto-generated method stub
            return product_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (convertView == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater
                        .inflate(R.layout.item_sub_category, null);
                holder = new ViewHolder();
                holder.tv_title = (TextView) convertView
                        .findViewById(R.id.txt_productname);
                holder.tv_price = (TextView) convertView
                        .findViewById(R.id.txt_price);
                holder.iv = (ImageView) convertView
                        .findViewById(R.id.item_image);
                holder.iv_bg = (ImageView) convertView
                        .findViewById(R.id.img_backgrnd);
                holder.plus = (ImageView) convertView
                        .findViewById(R.id.img_plus1);
                holder.minus = (ImageView) convertView
                        .findViewById(R.id.img_minus);
                holder.edt_qty = (TextView) convertView
                        .findViewById(R.id.edt_qty);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            my_product = new Product_bean();
            add_tocartData = new Product_bean();
            my_product = product_data.get(position);
            holder.ck = (CheckBox) convertView.findViewById(R.id.check_item);

            if (my_product.product_name != null
                    && my_product.product_name.length() > 0) {
                holder.tv_title.setText(my_product.product_name);
            }
            if (my_product.product_price != null
                    && my_product.product_price.length() > 0) {
                holder.tv_price.setText(my_product.product_price);
            } else if (my_product.medium_price != null
                    && my_product.medium_price.length() > 0) {
                holder.tv_price.setText(my_product.medium_price);
            } else if (my_product.large_price != null
                    && my_product.large_price.length() > 0) {
                holder.tv_price.setText(my_product.large_price);
            }

            if (my_product.product_image != null
                    && my_product.product_image.length() > 0) {
                Picasso.with(context).load(my_product.product_image)
                        .into(holder.iv);
                Picasso.with(context).load(my_product.product_image)
                        .resize(width, 130).into(holder.iv_bg);

            }

            // if (my_product.is_select.equalsIgnoreCase("1")) {
            // holder.ck.setChecked(true);
            //
            // }
            // if (my_product.is_select.equalsIgnoreCase("1")) {
            // holder.ck.setChecked(true);
            // ischecked.set(position, true);
            // added_Data.add(getItem(position));
            // // added_Data.add(getItem(position));
            // }
            holder.edt_qty.setText(qty_array.get(position).qty + "");

            if (ischecked.get(position) == false) {
                holder.ck.setChecked(false);
            } else {
                holder.ck.setChecked(true);
            }
            holder.plus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (ischecked.get(position) == true) {
                        holder.ck.setChecked(true);
                        ischecked.set(position, true);
                    }
                    num = Integer.parseInt(qty_array.get(position).qty);
                    num = num + 1;

                    my_product.qty = num + "";
                    Product_bean pb = product_data.get(position);
                    pb.qty = num + "";
                    qty_array.set(position, pb);

                    holder.edt_qty.setText(qty_array.get(position).qty + "");

                }
            });

            holder.minus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (ischecked.get(position) == true) {
                        holder.ck.setChecked(true);
                        ischecked.set(position, true);
                    }
                    num = Integer.parseInt(qty_array.get(position).qty);
                    if (num > 1) {
                        num = num - 1;
                        Product_bean pb = product_data.get(position);
                        pb.qty = num + "";
                        qty_array.set(position, pb);
                        holder.edt_qty.setText(qty_array.get(position).qty + "");
                    } else {

                    }

                    my_product.qty = num + "";

                }
            });

            holder.ck.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (ischecked.get(position) == false) {

                        System.out.println("position : id ==>" + position
                                + ">>>" + product_data.get(position).cat_id);
                        added_Data.add(product_data.get(position));
                        ischecked.set(position, true);
                        holder.ck.setChecked(true);
                    } else {

                        for (int i = 0; i < added_Data.size(); i++) {
                            if (added_Data.get(i).cat_id.equals(product_data
                                    .get(position).cat_id)) {
                                added_Data.remove(added_Data.get(i));
                                ischecked.set(position, false);
                                my_product.qty = "1";
                                if (my_product.is_select.equals("1")) {
                                    my_product.is_select = "0";

                                    product_data.set(position, my_product);
                                }
                                qty_array.set(position, my_product);
                                break;
                            }
                        }

                    }
                    // notifyDataSetChanged();
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    my_product = product_data.get(position);
                    Intent i = new Intent(Activity_product.this,
                            Activity_product_details.class);
                    i.putExtra("data", my_product);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    // ((Activity) context).finish();
                }
            });

            return convertView;

        }

    }

    class ViewHolder {
        TextView tv_title;
        TextView tv_price;
        ImageView iv, plus, minus;
        ImageView iv_bg;
        CheckBox ck;
        TextView edt_qty;
    }
}
