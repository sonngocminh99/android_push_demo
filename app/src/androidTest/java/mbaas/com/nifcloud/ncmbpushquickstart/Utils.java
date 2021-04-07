package mbaas.com.nifcloud.ncmbpushquickstart;

import android.util.Log;
import android.view.View;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import com.nifcloud.mbaas.core.DoneCallback;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBInstallation;
import com.nifcloud.mbaas.core.NCMBPush;
import com.nifcloud.mbaas.core.NCMBQuery;
import com.nifcloud.mbaas.core.TokenCallback;

import org.hamcrest.Matcher;
import org.json.JSONArray;
import org.json.JSONException;

import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
public class Utils {
    private static final String TAG = "FcmService";
    public final static String NOTIFICATION_TITLE = "UITest push notification";
    public final static String NOTIFICATION_TEXT = "Thank you! We appreciate your business, and we’ll do our best to continue to give you the kind of service you deserve.";
    public final static String NOTIFICATION_RICH_URL = "https://www.nifcloud.com/";

    public void sendPushWithSearchCondition() throws JSONException {
        NCMBInstallation installation = NCMBInstallation.getCurrentInstallation();
        installation.getDeviceTokenInBackground(new TokenCallback() {
            @Override
            public void done(String token, NCMBException e) {
                NCMBQuery<NCMBInstallation> query = new NCMBQuery<>("installation");
                query.whereEqualTo("deviceToken", token);
                NCMBPush push = new NCMBPush();
                push.setSearchCondition(query);
                push.setTitle(NOTIFICATION_TITLE);
                push.setMessage(NOTIFICATION_TEXT);
                push.setRichUrl(NOTIFICATION_RICH_URL);
                try {
                    push.setTarget(new JSONArray("[android]"));
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
                push.setDialog(true);
                push.sendInBackground(new DoneCallback() {
                    @Override
                    public void done(NCMBException e) {
                        if (e != null) {
                            Log.d(TAG, "Send push fail");
                        } else {
                            Log.d(TAG, "Send push success!");
                        }
                    }
                });
            }
        });
    }
}
