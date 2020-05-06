package com.example.randomizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.randomizer.R;

public class CommunityActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        SharedPreferences prefs = getSharedPreferences("comPref", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartDialog();
        }

        //ACCESS THE COMMUNITY FEATURE
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://medicationmanager.info/board/index.php");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void showStartDialog() {

        //custom alert dialog
        TextView cusTitle = new TextView(this);
        cusTitle.setText("Welcome!");
        cusTitle.setBackgroundColor(Color.parseColor("#12a3eb"));
        cusTitle.setPadding(10, 20, 10, 20);
        cusTitle.setGravity(Gravity.CENTER);
        cusTitle.setTextColor(Color.WHITE);
        cusTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        final TextView cusTitle2 = new TextView(this);
        cusTitle2.setText("Safety");
        cusTitle2.setBackgroundColor(Color.parseColor("#12a3eb"));
        cusTitle2.setPadding(10, 20, 10, 20);
        cusTitle2.setGravity(Gravity.CENTER);
        cusTitle2.setTextColor(Color.WHITE);
        cusTitle2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        final TextView cusTitle3 = new TextView(this);
        cusTitle3.setText("Finally");
        cusTitle3.setBackgroundColor(Color.parseColor("#12a3eb"));
        cusTitle3.setPadding(10, 20, 10, 20);
        cusTitle3.setGravity(Gravity.CENTER);
        cusTitle3.setTextColor(Color.WHITE);
        cusTitle3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        new AlertDialog.Builder(this)
//                .setTitle("Welcome to our discussion board!")
                .setCustomTitle(cusTitle)
                .setMessage("--- Getting Started ---\n" +
                        "\n" +
                        "You're welcome to browse the website without registering to see what others " +
                        "have to say about medications, but participating requires making an account. " +
                        "Registering for an account takes roughly 5 minutes, although be sure to have your " +
                        "e-mail ready as confirmation may be required to reduce spam.\n" +
                        "\n" +
                        "Once you have an account, or simply wish to just browse, find a category of " +
                        "medications you're interested in, and see if a post has been made for your " +
                        "medication and if people are talking about it.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
                        new AlertDialog.Builder(CommunityActivity.this)
//                                .setTitle("Safety")
                                .setCustomTitle(cusTitle2)
                                .setMessage("--- Keeping a Safe Community ---\n" +
                                        "\n" +
                                        "Registered users are encouraged to report any posts that are" +
                                        " inflammatory, or in general posts that are spreading misinformation" +
                                        " or off-topic nonsense. Administrators will promptly handle these reports.")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new AlertDialog.Builder(CommunityActivity.this)
//                                                .setTitle("Overall")
                                                .setCustomTitle(cusTitle3)
                                                .setMessage("Overall, the point of this forum is to discuss" +
                                                        " experiences with different prescribed medications. " +
                                                        "Since an abundance of medications exist, this website will " +
                                                        "categorize medications, meaning you should be able to find the medication " +
                                                        "you are looking for easily. You can also search for different medications.")
                                                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                    }
                                                }).create().show();
                                    }
                                })
                        .create().show();

                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("comPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

}
