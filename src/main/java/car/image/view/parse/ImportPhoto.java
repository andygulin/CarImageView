package car.image.view.parse;

import car.image.view.entity.Photo;
import car.image.view.service.PhotoService;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

import static org.apache.commons.io.FileUtils.deleteQuietly;

@Log4j2
public class ImportPhoto implements Runnable {

    private final BlockingQueue<TransportPhoto> queue;
    private final PhotoService photoService;

    public ImportPhoto(BlockingQueue<TransportPhoto> queue, PhotoService photoService) {
        this.queue = queue;
        this.photoService = photoService;
    }

    @Override
    public void run() {
        TransportPhoto transportPhoto = null;
        while (true) {
            try {
                transportPhoto = queue.poll(ParseConstants.WAIT_TIME, ParseConstants.WAIT_UNIT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Objects.nonNull(transportPhoto)) {
                File thumbnailFile = transportPhoto.getThumbnailFile();
                File originalFile = transportPhoto.getOriginalFile();

                Photo photo = new Photo();
                photo.setCreateAt(new Date());
                String thumbnailFileId = photoService.store(thumbnailFile);
                String originalFileId = photoService.store(originalFile);

                photo.setThumbnailFileId(thumbnailFileId);
                photo.setThumbnailFileCRC32(transportPhoto.getThumbnailFileCRC32());
                photo.setThumbnailSourceFileUrl(transportPhoto.getThumbnailSourceFileUrl());
                photo.setOriginalFileId(originalFileId);
                photo.setOriginalFileCRC32(transportPhoto.getOriginalFileCRC32());
                photo.setOriginalSourceFileUrl(transportPhoto.getOriginalSourceFileUrl());
                photo.setName(transportPhoto.getWebVO().getName());
                photo.setTitle(transportPhoto.getTitle());

                photoService.insert(photo);
                log.info("Store To MongoDB: {}", photo.getId());

                deleteQuietly(thumbnailFile);
                deleteQuietly(originalFile);
            } else {
                log.info("Wait {}ms , Shutdown Task...", ParseConstants.WAIT_TIME);
                return;
            }
        }
    }
}
