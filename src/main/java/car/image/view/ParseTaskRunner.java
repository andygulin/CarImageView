package car.image.view;

import car.image.view.parse.*;
import lombok.extern.log4j.Log4j2;
import car.image.view.service.PhotoService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class ParseTaskRunner implements CommandLineRunner {

    @Autowired
    private PhotoService photoService;

    @Override
    public void run(String... args) {
        List<WebVO> webs = new ArrayList<>();
        webs.add(new WebVO(2196, "宝马M3"));
        webs.add(new WebVO(4658, "宝马X3"));
        webs.add(new WebVO(6548, "宝马X5"));
        webs.add(new WebVO(587, "宝马X6"));

        webs.add(new WebVO(305, "宾利欧陆"));

        webs.add(new WebVO(4274, "大众途观L"));
        webs.add(new WebVO(4232, "大众途昂"));

        webs.add(new WebVO(162, "保时捷911"));
        webs.add(new WebVO(172, "保时捷卡宴"));
        webs.add(new WebVO(703, "保时捷Panamera"));

        log.info(webs);

        if (photoService.count() == 0) {
            ExecutorService service = Executors.newCachedThreadPool();
            BlockingQueue<TransportPhoto> queue = new ArrayBlockingQueue<>(ParseConstants.QUEUE_SIZE);
            List<List<WebVO>> splitList = ListUtils.partition(webs, ParseConstants.PARSE_LIST_SIZE);
            for (List<WebVO> subList : splitList) {
                service.execute(new ParsePhoto(subList, queue));
                service.execute(new ImportPhoto(queue, photoService));
            }
            service.shutdown();
        } else {
            log.info("MongoDB In The Presence Of Pictures, Skip Parse...");
        }
    }
}
