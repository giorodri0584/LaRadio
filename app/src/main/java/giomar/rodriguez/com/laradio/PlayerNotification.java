package giomar.rodriguez.com.laradio;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class PlayerNotification extends Service {
    private SimpleExoPlayer player;
    private PlayerNotificationManager playerNotificationManager;
    private DefaultDataSourceFactory dataSourceFactory;
    final Context context = this;

    @Override
    public void onCreate() {
        super.onCreate();

        dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, "La Radio Dominicana"));
        player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(intent.getStringExtra("url")));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);

        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(
                context, "La Radio", R.string.playback_la_radio_dominicana, 6678,
                new NotificationAdapter(intent.getStringExtra("title"), intent.getStringExtra("description"), intent.getStringExtra("logoUrl"),context));

        playerNotificationManager.setNotificationListener(new PlayerNotificationManager.NotificationListener() {
            @Override
            public void onNotificationStarted(int notificationId, Notification notification) {
                startForeground(notificationId, notification);
            }

            @Override
            public void onNotificationCancelled(int notificationId) {
                stopSelf();
            }
        });

        playerNotificationManager.setPlayer(player);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        playerNotificationManager.setPlayer(null);
        player.release();
        player = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
