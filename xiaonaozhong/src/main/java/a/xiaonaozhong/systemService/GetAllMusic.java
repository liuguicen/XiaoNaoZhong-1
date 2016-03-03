package a.xiaonaozhong.systemService;

import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Media;

public class GetAllMusic {
	Context context;

	public GetAllMusic(Context context) {
		this.context = context;
	}

	ContentResolver musicContentResolver;

	public List<String> getAllMusicPath(List<String> allMusicUri) {
		musicContentResolver = context.getContentResolver();
		Cursor cursor = musicContentResolver.query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				null);
		while (cursor.moveToNext()) {
			String path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			allMusicUri.add(path);
		}
		cursor.close();
		cursor = musicContentResolver.query(
				MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, null, null,
				null);
		while (cursor.moveToNext()) {
			String path = cursor.getString(cursor.getColumnIndex(Media.DATA));
			allMusicUri.add(path);
		}
		cursor.close();
		return allMusicUri;
	}

}
