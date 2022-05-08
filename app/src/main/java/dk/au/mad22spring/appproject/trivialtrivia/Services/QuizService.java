package dk.au.mad22spring.appproject.trivialtrivia.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LifecycleService;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.au.mad22spring.appproject.trivialtrivia.Constants.Constants;
import dk.au.mad22spring.appproject.trivialtrivia.Database.Database;
import dk.au.mad22spring.appproject.trivialtrivia.Models.Game;
import dk.au.mad22spring.appproject.trivialtrivia.R;

public class QuizService extends LifecycleService {

    private ExecutorService executorService;
    private boolean started = false;
    private Database database;

    public static final String SERVICE_CHANNEL = "serviceChannel";
    public static final int NOTIFICATION_ID = 1;

    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    Notification notification;

    public QuizService(){}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        database = Database.getInstance(getApplication());
        String category = (String) intent.getSerializableExtra(Constants.CATEGORY_STRING);
        String difficulty = (String) intent.getSerializableExtra(Constants.DIFFICULTY_OBJ);

        //check for Android version - whether we need to create a notification channel (from Android 0 and up, API 26)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "Foreground Service", NotificationManager.IMPORTANCE_LOW);
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
        notificationBuilder = new NotificationCompat.Builder(this, SERVICE_CHANNEL);

        notification = notificationBuilder
                .setContentTitle(" ")
                .setContentText(" ")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        database.getGames().observe(this, new Observer<List<Game>>() {
            Random random = new Random();
            @Override
            public void onChanged(List<Game> games) {
                if(!games.isEmpty()) {
                    Game randomGame = games.get(random.nextInt(games.size()));
                    if (randomGame == null) {
                        //Do nothing
                    }
                    else{
                        notification = notificationBuilder
                                .setContentTitle(getResources().getString(R.string.activeQuiz))
                                .setContentText(String.format(getResources().getString(R.string.quizUpdateInfo), difficulty, category))
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .build();
                    }
                    notificationManager.notify(NOTIFICATION_ID, notification);
                }
            }
        });

        startForeground(NOTIFICATION_ID, notification);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public void onDestroy() {
        started = false;
        super.onDestroy();
    }


}
