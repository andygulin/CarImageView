package car.image.view.parse;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

@Log4j2
public class ParsePhoto implements Runnable {

    private final List<WebVO> webs;
    private final BlockingQueue<TransportPhoto> queue;

    public ParsePhoto(List<WebVO> webs, BlockingQueue<TransportPhoto> queue) {
        this.webs = webs;
        this.queue = queue;
    }

    @Override
    public void run() {
        for (WebVO webVO : webs) {
            String url = webVO.getUrl();
            Document doc;
            try {
                doc = Jsoup.connect(url).userAgent(ParseConstants.USER_AGENT).timeout(ParseConstants.TIMEOUT).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Elements elements = doc.select("div.uibox-con:not(#vrlist)").select("li:not(.last)").select("a");
            elements.forEach(element -> {
                String href = element.attr("href");
                href = ParseUtils.completionUrlPrefix(href);

                Element img = element.selectFirst("img");
                if (Objects.nonNull(img)) {
                    String thumbnail = img.attr("src");
                    thumbnail = ParseUtils.completionImageProtocol(thumbnail);
                    String title = img.attr("title");

                    String original;
                    try {
                        original = getOriginalImage(href);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        String thumbnailFileName = UUID.randomUUID() + FilenameUtils.getExtension(thumbnail);
                        File thumbnailFile = new File(ParseConstants.IMAGE_PATH, thumbnailFileName);
                        IOUtils.copy(new URL(thumbnail), thumbnailFile);

                        String originalFileName = UUID.randomUUID() + FilenameUtils.getExtension(original);
                        File originalFile = new File(ParseConstants.IMAGE_PATH, originalFileName);
                        IOUtils.copy(new URL(original), originalFile);

                        TransportPhoto transportPhoto = new TransportPhoto();
                        transportPhoto.setWebVO(webVO);
                        transportPhoto.setTitle(title);
                        transportPhoto.setThumbnailFile(thumbnailFile);
                        transportPhoto.setThumbnailFileCRC32(FileUtils.checksumCRC32(thumbnailFile));
                        transportPhoto.setThumbnailSourceFileUrl(thumbnail);
                        transportPhoto.setOriginalFile(originalFile);
                        transportPhoto.setOriginalFileCRC32(FileUtils.checksumCRC32(originalFile));
                        transportPhoto.setOriginalSourceFileUrl(original);

                        boolean result = queue.offer(transportPhoto);
                        log.info("Store To Disk: {}, result : {}", transportPhoto, result);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private String getOriginalImage(String url) throws IOException {
        Document doc = Jsoup.connect(url).userAgent(ParseConstants.USER_AGENT).timeout(ParseConstants.TIMEOUT).get();
        Element img = doc.selectFirst("img#img");
        String image = img.attr("src");
        image = ParseUtils.completionImageProtocol(image);
        return image;
    }

}
