package a.xiaonaozhong.dateAndLogic;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * SimpleNaozhong����ID���Ǵ�ID
 */
public class SimpleNaozhong {
	int id;
	int tixingId;
	Long time;

	/**
	 * SimpleNaozhong����ID���Ǵ�ID
	 */
	public SimpleNaozhong(int id2, int tixingId2, long time2) {
		this.id = id2;
		this.tixingId = tixingId2;
		this.time = time2;
	}

	public SimpleNaozhong() {

	}

	public int getTixingId() {
		return tixingId;
	}

	public void setTixingId(int tixingId) {
		this.tixingId = tixingId;
	}

	public int getId() {
		return id;
	}

	/**
	 * SimpleNaozhong����ID���Ǵ�ID
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 *
	 * @return 长整型的时间
	 */
	public long getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = Long.valueOf(time);
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getFormatTime() {
		TimeZone.setDefault(TimeZone.getTimeZone(AllData.TIME_ZONE));
		Date date = new Date(Long.valueOf(time));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr.substring(11, 16);
	}
}
