package com.nanda.jsoupexample.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.nanda.jsoupexample.BuildConfig;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class UpdateChecker extends AsyncTask<String, String, String> {

    private String sourceUrl;
    private VersionUpdateListner listner;
    private ProgressDialog dialog;
    private Context context;

    public UpdateChecker(Context context, String sourceUrl, VersionUpdateListner listner) {
        this.context = context;
        this.sourceUrl = sourceUrl;
        this.listner = listner;
    }

    public interface VersionUpdateListner {
        void onVersionChecked(String playStoreVersion, boolean isNewVersionAvailable);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (dialog != null) {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Fetching Content from Url...");
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            Document document = Jsoup.connect(sourceUrl)
                    .timeout(30000)
                    .get();
            Elements element = document.body().getElementsByClass("xyOfqd").select(".hAyfc");
            return element.get(3).child(1).child(0).child(0).ownText();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String playStoreVersion) {
        super.onPostExecute(playStoreVersion);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!TextUtils.isEmpty(playStoreVersion)) {
            boolean isNewVersionAvailable = isNewVersionAvailable(BuildConfig.VERSION_NAME, playStoreVersion);
            if (listner != null) {
                listner.onVersionChecked(playStoreVersion, isNewVersionAvailable);
            }
        } else listner.onVersionChecked(playStoreVersion, false);
    }

    private boolean isNewVersionAvailable(String currentVersion, String newVersion) {
        final DefaultArtifactVersion current = new DefaultArtifactVersion(currentVersion);
        final DefaultArtifactVersion target = new DefaultArtifactVersion(newVersion);
        if (current.compareTo(target) < 0) {
            return true;
        } else {
            return false;
        }
    }


}
