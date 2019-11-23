package nginx.mongo.photo.view.parse;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import nginx.mongo.photo.view.entity.Photo;
import nginx.mongo.photo.view.service.PhotoService;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

import static com.alibaba.fastjson.JSON.parseObject;
import static org.apache.commons.io.FileUtils.deleteQuietly;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.ArrayUtils.INDEX_NOT_FOUND;
import static org.apache.commons.lang3.ArrayUtils.indexOf;

@Log4j2
public class ImportPhoto implements Runnable {

    private BlockingQueue<File> queue;
    private PhotoService photoService;

    public ImportPhoto(BlockingQueue<File> queue, PhotoService photoService) {
        this.queue = queue;
        this.photoService = photoService;
    }

    @Override
    public void run() {
        String[] imageExt = {"jpg", "jpeg"};
        String obj = null;
        JSONObject object;
        File file = null;
        File remarkFile;
        while (true) {
            try {
                file = queue.poll(ParseConstants.WAIT_TIME, ParseConstants.WAIT_UNIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (file != null && file.exists() && indexOf(imageExt, getExtension(file.getName())) != INDEX_NOT_FOUND) {
                Photo photo = new Photo();
                photo.setCreateAt(new Date());
                String fileId = photoService.store(file);
                photo.setFileId(fileId);
                remarkFile = new File(file + ParseConstants.REMARK_FILE_EXT);
                try {
                    obj = readFileToString(remarkFile, ParseConstants.FILE_ENCODING);
                } catch (IOException e) {
                    //
                }
                object = parseObject(obj);
                photo.setRemark(object.getString("remark"));
                photo.setName(object.getString("name"));
                String id = photoService.insert(photo);
                log.info("Store To MongoDB: {}", id);

                deleteQuietly(file);
                deleteQuietly(remarkFile);
            } else {
                log.info("Wait {}ms , Shutdown Task...", ParseConstants.WAIT_TIME);
                return;
            }
        }
    }
}
