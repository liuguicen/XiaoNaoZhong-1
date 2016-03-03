package a.xiaonaozhong.systemService;

/*
public class SetAllNaozhongService extends IntentService {
	AlarmUtil alarmUtil;
	Context context;
	TixingManager tm;
	public SetAllNaozhongService() {
		super("myIntentService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		alarmUtil=new AlarmUtil(getApplicationContext());
		context=getBaseContext();
		Bundle bundle=intent.getExtras();
		SharedPreferences sharedPreferences = getSharedPreferences("config",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor spEditor = sharedPreferences.edit();
		int activityNum = sharedPreferences.getInt("activityNum", 0);
		String myType = intent.getExtras().getString("myType");
		switch (myType) {
		case "addActivity":
			activityNum++;
			if (activityNum <= 1)
				startAll();
			spEditor.putInt("activityNum", activityNum);
			break;
		case "subActivity":
			activityNum--;
			spEditor.putInt("activityNum", activityNum);
		case "openTixing":
			startTixing(bundle.getInt("tixingId"));
		case "closeTixing":
			closeTixing(bundle.getInt("tixingId"));
		case "closeNaozhong":
		      closeNaozhong(bundle.getInt("TixingId"),
		    		  bundle.getInt("naozhongId"),bundle.getLong("time"));
		case "openNaozhong":
		      openNaozhong(bundle.getInt("tixingId"),
		    		  bundle.getInt("naozhongId"),bundle.getLong("time"));
		default:
			break;
		}
	}

	public void closeNaozhong(int tixingId, int naozhongId,Long time) {
		Tixing tx = tm.getTixingById(tixingId);
		alarmUtil.cancelNaozhong(tx, 
				new SimpleNaozhong(naozhongId,
						tixingId, String.valueOf(time)));
	}
	private void openNaozhong(int tixingId, int naozhongId,Long time) {
		Tixing tx = tm.getTixingById(tixingId);
		alarmUtil.setNaozhong(tx, 
				new SimpleNaozhong(naozhongId,
						tixingId, String.valueOf(time)));
	}

	private void closeTixing(int tixingId) {
		
		Tixing tx = tm.getTixingById(tixingId);
		List<SimpleNaozhong> snList = new ArrayList<SimpleNaozhong>();
		dbUtil.getAllNaozhongOfTixing(tixingId, snList);
		for (SimpleNaozhong sn : snList) {
			alarmUtil.cancelNaozhong(tx, sn);
		}
	}

	private void startTixing(int tixingId) {
		TixingManager tm = TixingManager.getInstance(context);
		Tixing tx = tm.getTixingById(tixingId);
		List<SimpleNaozhong> snList = new ArrayList<SimpleNaozhong>();
		dbUtil.getAllNaozhongOfTixing(tixingId, snList);
		for (SimpleNaozhong sn : snList) {
			alarmUtil.setNaozhong(tx, sn);
		}
	}

	private void startAll() {

		List<Tixing> lst = new ArrayList<Tixing>();
		dbUtil.getAllTixing(lst);
		for (Tixing tx : lst) {
			if (tx.getOpen().equals(AllData.OPEN_TEXT)) {
				List<SimpleNaozhong> lsn = new ArrayList<SimpleNaozhong>();
				dbUtil.getAllNaozhongOfTixing(tx.getId(), lsn);
				for (SimpleNaozhong sn : lsn) {
					P.le("time是", sn.getTime());
					alarmUtil.setNaozhong(tx, sn);
				}
			}

		}
	}
}
*/