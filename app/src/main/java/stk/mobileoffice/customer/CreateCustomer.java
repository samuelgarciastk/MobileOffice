package stk.mobileoffice.customer;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import stk.mobileoffice.CurrentUser;
import stk.mobileoffice.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: stk
 * Date: 2016/6/25
 * Time: 15:25
 */
public class CreateCustomer extends AppCompatActivity {
    private MHandler handler = new MHandler(this);
    private EditText text_name;
    private EditText text_type;
    private EditText text_tel;
    private EditText text_mail;
    private EditText text_web;
    private EditText text_addr;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_create);
        initView();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
    }

    private void create() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://nqiwx.mooctest.net:8090/wexin.php/Api/Index/customer_create_json");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    String str = "staffid=" + CurrentUser.id +
                            "&customername=" + text_name.getText().toString() +
                            "&customertype=" + text_type.getText().toString() +
                            "&telephone=" + text_tel.getText().toString() +
                            "&email=" + text_mail.getText().toString() +
                            "&website=" + text_web.getText().toString() +
                            "&address=" + text_addr.getText().toString();
                    byte[] strData = str.getBytes("UTF-8");
                    OutputStream out = con.getOutputStream();
                    out.write(strData);
                    if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder result = new StringBuilder();
                        String s;
                        while ((s = in.readLine()) != null)
                            result.append(s);
                        Log.i("Customer_Create_Data", result.toString());
                        if (new JSONObject(result.toString()).getInt("resultcode") == 0) {
                            Message msg = new Message();
                            msg.obj = "创建成功";
                            handler.sendMessage(msg);
                            CreateCustomer.this.finish();
                        } else {
                            Message msg = new Message();
                            msg.obj = "创建失败";
                            handler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Customer_Create", "Create failed.");
                    Message msg = new Message();
                    msg.obj = "无法创建";
                    handler.sendMessage(msg);
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        text_name = (EditText) findViewById(R.id.customer_create_name);
        text_type = (EditText) findViewById(R.id.customer_create_type);
        text_tel = (EditText) findViewById(R.id.customer_create_tel);
        text_mail = (EditText) findViewById(R.id.customer_create_mail);
        text_web = (EditText) findViewById(R.id.customer_create_web);
        text_addr = (EditText) findViewById(R.id.customer_create_addr);
        confirm = (Button) findViewById(R.id.customer_create_confirm);
    }

    private static class MHandler extends Handler {
        private final WeakReference<CreateCustomer> outer;
        MHandler(CreateCustomer target) {
            outer = new WeakReference<>(target);
        }
        @Override
        public void handleMessage(Message msg) {
            CreateCustomer target = outer.get();
            if (target != null) {
                String str = (String) msg.obj;
                Toast.makeText(target, str, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
