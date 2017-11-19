package com.asherelgar.myfinalproject.fragments;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.asherelgar.myfinalproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class YnetWebViewFragment extends Fragment {
    @BindView(R.id.ivImageProgress)
    ImageView ivImageProgress;
    Unbinder unbinder;
    @BindView(R.id.tvNews)
    TextView tvNews;
    @BindView(R.id.tvHebNews)
    TextView tvHebNews;
    WebView webView;
    ProgressBar progressBar;


    //Factory method:
    public static YnetWebViewFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putString("Link", link);
        YnetWebViewFragment fragment = new YnetWebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_yent_web_view, container, false);

        final String link = getArguments().getString("Link");

        webView = v.findViewById(R.id.wvYnet);
        progressBar = v.findViewById(R.id.progressBar);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setFocusable(true);
        webView.setFocusableInTouchMode(true);


        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        webView.loadUrl(link);
        // webView.setWebViewClient(new WebViewClient());

        webView.setWebViewClient(new WebViewClient() {

            int counter = 0;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                String url = request.getUrl().toString();
                String tempLink = url;
                String urlSub = url.substring(url.length() - 10, url.length());
                String tempLink2 = link;
                String linkSub = link.substring(link.length() - 10, link.length());
                if (urlSub.equals(linkSub)) {
                    webView.loadUrl(tempLink);
                    return true;
                } else {
                    webView.loadUrl(tempLink);
                    return false;
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains(link)) {
                    webView.destroy();
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                ivImageProgress.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);

                ivImageProgress.startAnimation(animation);

                ivImageProgress.animate().cancel();
//                ivImageProgress.animate().cancel();
//                progressBar.setVisibility(View.VISIBLE);

                //progressBar.animate().alpha(1);
                counter++;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                tvHebNews.setVisibility(View.VISIBLE);
                tvNews.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out);
                Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);

                ivImageProgress.startAnimation(animation);
                tvHebNews.startAnimation(animation2);
                tvNews.startAnimation(animation2);

                tvNews.animate().cancel();
                tvHebNews.animate().cancel();
                ivImageProgress.animate().cancel();


//                ivImageProgress.setVisibility(View.INVISIBLE);
//                progressBar.setVisibility(View.INVISIBLE);
                //progressBar.animate().alpha(0);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });


        unbinder = ButterKnife.bind(this, v);
        return v;
    }

//    @SuppressLint("SetJavaScriptEnabled")
//    private void configWebView(final WebView webView) {
//        webView.getSettings().setJavaScriptEnabled(true);
//
//        webView.setWebViewClient(new WebViewClient() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                String url = request.getUrl().toString();
//                webView.loadUrl(url);
//                Log.d("LOLO", url);
//                Log.d("LOLO", request.toString());
//                return true;
//            }
//
//            @SuppressWarnings("deprecation")
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                webView.loadUrl(url);
//
//                return false;
//            }
//
//            int counter = 0;
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                ivImageProgress.setVisibility(View.VISIBLE);
//
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
//
//                ivImageProgress.startAnimation(animation);
//
//                ivImageProgress.animate().cancel();
////                ivImageProgress.animate().cancel();
////                progressBar.setVisibility(View.VISIBLE);
//
//                //progressBar.animate().alpha(1);
//                counter++;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//                tvHebNews.setVisibility(View.VISIBLE);
//                tvNews.setVisibility(View.VISIBLE);
//                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out);
//                Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.zoom);
//
//                ivImageProgress.startAnimation(animation);
//                tvHebNews.startAnimation(animation2);
//                tvNews.startAnimation(animation2);
//
//                tvNews.animate().cancel();
//                tvHebNews.animate().cancel();
//                ivImageProgress.animate().cancel();
//
////                ivImageProgress.setVisibility(View.INVISIBLE);
////                progressBar.setVisibility(View.INVISIBLE);
//                //progressBar.animate().alpha(0);
//            }
//
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                Toast.makeText(getContext(), "Error: "+ error, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
}
