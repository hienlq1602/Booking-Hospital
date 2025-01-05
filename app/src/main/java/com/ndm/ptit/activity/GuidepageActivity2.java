package com.ndm.ptit.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.ndm.ptit.R;

public class GuidepageActivity2 extends AppCompatActivity {
    private ImageButton btnBack;
    private WebView webView;
    private AppCompatButton btnOpenWithGoogleMap;
    private VideoView videoView;
    private String url = "https://www.google.com/maps/search/b%E1%BB%87nh+vi%E1%BB%87n+b%E1%BA%A1ch+mai/@21.002261,105.8385441,18z?entry=ttu&g_ep=EgoyMDI0MTIxMS4wIKXMDSoASAFQAw%3D%3D";
    private String videoUrl = "https://youtu.be/3t6eYCXWep8";
    private String videoPath ="android.resource://" + "com.ndm.ptit" +"/" + R.raw.video_datn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidepage);
        setupComponent();
        setupEvent();
    }

    private void setupComponent() {
        btnBack = findViewById(R.id.btnBack);
        videoView = findViewById(R.id.videoView);

        Uri videoUri = Uri.parse(videoPath);
        videoView.setVideoURI(videoUri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);


//        videoView.setVideoPath(videoUrl);
        videoView.requestFocus();
        videoView.start();
//        webView = findViewById(R.id.wvwDescription);
//        WebSettings webSettings = webView.getSettings();
//
//        webSettings.setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                if (!progressDialog.isShowing()) {
//                    progressDialog.show();
//                }
//            }
//
//            public void onPageFinished(WebView view, String url) {
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                if (progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
//            }
//        });
//        webView.loadUrl(url);
        btnOpenWithGoogleMap = findViewById(R.id.btnOpenWithGoogleMap);
    }
    private void setupEvent() {
        btnBack.setOnClickListener(view-> finish());

        /*GOOGLE MAP*/
//        String location =
//                "<html>\n" +
//                        "   <body>\n" +
//                        "<iframe src=\"src=\"https://accounts.google.com/RotateCookiesPage?og_pid=113&rot=3&origin=https%3A%2F%2Fwww.google.com&exp_id=0\"\" " +
//                        "width=\"600\" height=\"450\" \n" +
//                        "style=\"border:0;\" allowfullscreen=\"\"\n" +
//                        "loading=\"lazy\" \n" +
//                        "referrerpolicy=\"no-referrer-when-downgrade\">\n" +
//                        "</iframe>\n" +
//                        "   </body>\n" +
//                        "</html> ";
//
//        ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage( this.getString(R.string.loading));
//        progressDialog.setCancelable(false);
//
//        wvwDescription.requestFocus();
//        wvwDescription.getSettings().setJavaScriptEnabled(true);
//        wvwDescription.getSettings().setGeolocationEnabled(true);
//        wvwDescription.setWebChromeClient(new WebChromeClient() {
//            public void onProgressChanged(WebView view, int progress) {
//                if (progress < 100) {
//                    progressDialog.show();
//                }
//                if (progress == 100) {
//                    progressDialog.dismiss();
//                }
//            }
//        });
//        wvwDescription.loadDataWithBaseURL(null, location, "text/HTML", "UTF-8", null);
        /*end GOOGLE MAP*/


        /*BUTTON OPEN WITH GOOGLE MAP*/
        btnOpenWithGoogleMap.setOnClickListener(view->{
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            finish();
        });
    }


    protected void onResume() {
        super.onResume();
    }

}
