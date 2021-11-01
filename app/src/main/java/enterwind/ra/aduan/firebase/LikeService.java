package enterwind.ra.aduan.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by dayana on 7/20/19.
 */

public class LikeService extends Service {

    private static final String NOTIFICATION_ID_EXTRA = "notificationId";
    private static final String IMAGE_URL_EXTRA = "imageUrl";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Saving action implementation
        Toast.makeText(this, "" + intent, Toast.LENGTH_SHORT).show();
        return null;
    }
}
