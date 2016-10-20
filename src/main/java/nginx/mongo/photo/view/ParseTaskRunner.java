package nginx.mongo.photo.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import nginx.mongo.photo.view.parse.ImportPhoto;
import nginx.mongo.photo.view.parse.ParseConstants;
import nginx.mongo.photo.view.parse.ParsePhoto;
import nginx.mongo.photo.view.parse.WebVO;
import nginx.mongo.photo.view.service.PhotoService;

public class ParseTaskRunner implements CommandLineRunner {

	private static List<WebVO> webs = new ArrayList<>();

	static {
		webs.add(new WebVO("2196", "宝马M3"));
		webs.add(new WebVO("3189", "宝马M4"));
		webs.add(new WebVO("2847", "宝马5系GT"));
		webs.add(new WebVO("270", "宝马6系"));
		webs.add(new WebVO("317", "宝马3系"));
		webs.add(new WebVO("159", "宝马X5"));
		webs.add(new WebVO("587", "宝马X6"));
		webs.add(new WebVO("161", "宝马Z4"));
		webs.add(new WebVO("2726", "宝马M5"));
		webs.add(new WebVO("2727", "宝马M6"));
		webs.add(new WebVO("2968", "宝马4系"));

		webs.add(new WebVO("692", "奥迪A4L"));
		webs.add(new WebVO("2734", "奥迪S5"));
		webs.add(new WebVO("740", "奥迪A7"));
		webs.add(new WebVO("2739", "奥迪S8"));
		webs.add(new WebVO("2740", "奥迪TTS"));
		webs.add(new WebVO("511", "奥迪R8"));
		webs.add(new WebVO("812", "奥迪Q5"));

		webs.add(new WebVO("162", "保时捷911"));
		webs.add(new WebVO("703", "保时捷Panamera"));
		webs.add(new WebVO("172", "保时捷卡宴"));
		webs.add(new WebVO("168", "保时捷Boxster"));
		webs.add(new WebVO("415", "保时捷Cayman"));
		webs.add(new WebVO("2838", "保时捷Macan"));

		webs.add(new WebVO("889", "法拉利458"));
		webs.add(new WebVO("2682", "法拉利F12"));
		webs.add(new WebVO("2261", "法拉利FF"));
		webs.add(new WebVO("676", "法拉利California"));

		webs.add(new WebVO("2277", "兰博基尼Aventador"));
		webs.add(new WebVO("354", "兰博基尼Gallardo"));
		webs.add(new WebVO("3277", "兰博基尼Huracan"));

		webs.add(new WebVO("2719", "奔驰CLS级AMG"));
		webs.add(new WebVO("2717", "奔驰C级AMG"));
		webs.add(new WebVO("914", "奔驰SLS级AMG"));
		webs.add(new WebVO("2197", "奔驰S级AMG"));
		webs.add(new WebVO("588", "奔驰C级"));
		webs.add(new WebVO("2722", "奔驰M级AMG"));

		webs.add(new WebVO("69", "路虎揽胜"));
		webs.add(new WebVO("754", "路虎揽胜极光"));
		webs.add(new WebVO("850", "路虎揽胜运动版"));

		webs.add(new WebVO("2903", "捷豹F-TYPE"));
		webs.add(new WebVO("589", "捷豹XF"));
		webs.add(new WebVO("178", "捷豹XJ"));
		webs.add(new WebVO("456", "捷豹XL"));

		webs.add(new WebVO("700", "大众CC"));
		webs.add(new WebVO("372", "大众高尔夫"));
		webs.add(new WebVO("224", "大众辉腾"));
		webs.add(new WebVO("210", "大众甲壳虫"));
		webs.add(new WebVO("669", "大众尚酷"));
		webs.add(new WebVO("82", "大众途锐"));
		webs.add(new WebVO("874", "大众途观"));

		webs.add(new WebVO("266", "阿斯顿马丁DB9"));
		webs.add(new WebVO("729", "阿斯顿马丁One-77"));

		webs.add(new WebVO("305", "宾利欧陆"));

		webs.add(new WebVO("289", "玛莎拉蒂总裁"));
		webs.add(new WebVO("3060", "玛莎拉蒂Ghibli"));
		webs.add(new WebVO("903", "玛莎拉蒂GranCabrio"));
		webs.add(new WebVO("551", "玛莎拉蒂GranTurismo"));

		webs.add(new WebVO("436", "日产GT-R"));

		webs.add(new WebVO("678", "雪佛兰科迈罗Camaro"));
	}

	@Autowired
	private PhotoService photoService;

	@Override
	public void run(String... args) throws Exception {
		if (photoService.count() == 0) {
			ExecutorService service = Executors.newCachedThreadPool();
			BlockingQueue<File> queue = new ArrayBlockingQueue<File>(ParseConstants.QUEUE_SIZE);
			List<List<WebVO>> splitList = ListUtils.partition(webs, ParseConstants.PARSE_LIST_SIZE);
			for (List<WebVO> subList : splitList) {
				service.execute(new ParsePhoto(subList, queue));
				service.execute(new ImportPhoto(queue, photoService));
			}
			service.shutdown();
		} else {
			System.out.println("MongoDB In The Presence Of Pictures, Skip Parse...");
		}
	}

}
