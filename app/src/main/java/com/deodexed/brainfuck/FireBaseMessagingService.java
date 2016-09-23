package com.deodexed.brainfuck;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.view.View;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Deode on 14/09/2016.
 */
public class FireBaseMessagingService extends FirebaseMessagingService {
    private static final String KEY_TEXT_REPLY = "key_text_reply";

   private PendingIntent pi;



    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getData().get("message"));
    }

    private String text = "La fusion nucléaire, dite parfois fusion thermonucléaire" +
            ", est un processus où deux noyaux atomiques légers s'assemblent pour former un noyau plus lourd. " +
            "Cette réaction est à l'œuvre de manière naturelle dans le Soleil et la plupart des étoiles de l'Univers.";

    private void showNotification(String message) {

       /* Uri uri = Uri.parse("smsto:0607609808");
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "Here you can set the SMS text to be sent");
        pi = PendingIntent.getActivity(this,0,it,0);
        String replyLabel = "Répondre";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(replyLabel)
                .build();

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_add_white_24dp,
                        "Reply", pi)
                        .addRemoteInput(remoteInput)
                        .build();
                                                      */


        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Update")
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setSubText("Clique ici")
                .setDefaults(Notification.DEFAULT_ALL)
       .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());


    }
}
