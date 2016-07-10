package in.co.theshipper.www.shipper_customer;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by GB on 12/7/2015.
 */

public class GcmMessageHandler extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    @Override
    public void onMessageReceived(String from, Bundle data) {
//        String message = data.getString("message");
        createNotification(from, data);
    }

    // Creates notification based on title and body received
    private void createNotification(String title, Bundle body) {

        String message ="Truck Allocated";
        Intent i = new Intent(this, FullActivity.class);
        Fn.logD("message_body",String.valueOf(body));
        // i.putExtra(body);
        Bundle bundle = new Bundle();
        bundle.putString("crn_no",body.getString("crn_no"));
        bundle.putString("menuFragment", "NewBooking");
        bundle.putString("method", "push");
        i.putExtras(Fn.CheckBundle(bundle));
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                .setContentText(message);
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

}
