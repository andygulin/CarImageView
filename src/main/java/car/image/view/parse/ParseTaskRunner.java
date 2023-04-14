package car.image.view.parse;

import car.image.view.service.PhotoService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class ParseTaskRunner implements CommandLineRunner {

    @Autowired
    private PhotoService photoService;
    @Autowired
    private ParseConfiguration parseConfiguration;

    @Override
    public void run(String... args) {
        log.info("parseConfiguration.location : {}", parseConfiguration.getLocation());
        List<WebVO> webs = null;
        Resource resource = new FileSystemResource(parseConfiguration.getLocation());
        try {
            String data = FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8);
            webs = JSON.parseArray(data, WebVO.class);
        } catch (IOException e) {
            log.error("car.json Not Found...");
        }

        log.info(webs);

        if (webs != null && !webs.isEmpty()) {
            if (photoService.count() == 0) {
                log.info("Empty, Start Parse...");
                ExecutorService service = Executors.newCachedThreadPool();
                BlockingQueue<TransportPhoto> queue = new ArrayBlockingQueue<>(ParseConstants.QUEUE_SIZE);
                List<List<WebVO>> splitList = ListUtils.partition(webs, ParseConstants.PARSE_LIST_SIZE);
                for (List<WebVO> subList : splitList) {
                    service.execute(new ParsePhoto(subList, queue));
                    service.execute(new ImportPhoto(queue, photoService));
                }
                service.shutdown();
            } else {
                log.info("Not Empty, Skip Parse...");
            }
        }
    }
}
