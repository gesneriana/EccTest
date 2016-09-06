package com.asuna.ecctest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnEcc = (Button) findViewById(R.id.brn_ecc);
        final TextView tvPub = (TextView) findViewById(R.id.tvPub);
        final TextView tvPri = (TextView) findViewById(R.id.tvPri);

        final EditText etEcc = (EditText) findViewById(R.id.etEcc);
        Button btnTest = (Button) findViewById(R.id.btnTest);
        final TextView tvDec = (TextView) findViewById(R.id.tvDec);


        // 生成密钥对
        EccManager.buildKeyPair();

        btnEcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tvPub.setText(EccManager.getPubkey().toString());
                    tvPri.setText(EccManager.getPrikey().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (etEcc.getText().toString().length() > 0) {
                        if (etEcc.getText().toString().getBytes().length > 40) {
                            Toast.makeText(MainActivity.this, "字符串不能超过40字节", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (etEcc.getText().toString().getBytes().length % 20 > 0 && etEcc.getText().toString().getBytes().length % 20 < 6) {
                            Toast.makeText(MainActivity.this, "字节流长度必须 在 6-20 或者 26-40之间", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // 加密
                        byte[] b = EccManager.Enc2(etEcc.getText().toString().getBytes(), EccManager.getPubkey());
                        Toast.makeText(MainActivity.this, "密文: " + new String(b), Toast.LENGTH_SHORT).show();
                        // 解密
                        byte[] dec = EccManager.Dec2(b, EccManager.getPrikey());
                        // 显示解密的结果
                        tvDec.setText(new String(dec));

                        Toast.makeText(MainActivity.this, "程序员请注意: EccManager.getchar255(" +
                                "的返回值在不同的处理器架构中是不同的,当前处理器架构返回值是" +
                                EccManager.getchar255(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "请输入需要加密的数据", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
