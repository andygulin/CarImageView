package nginx.mongo.photo.view.parse;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static com.alibaba.fastjson.JSON.toJSONString;
import static org.apache.commons.io.FileUtils.copyURLToFile;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.io.IOUtils.DIR_SEPARATOR;

@Log4j2
public class ParsePhoto implements Runnable {

    private List<WebVO> webs;
    private BlockingQueue<File> queue;

    public ParsePhoto(List<WebVO> webs, BlockingQueue<File> queue) {
        this.webs = webs;
        this.queue = queue;
    }

    @Override
    public void run() {
        String url;
        String carId;
        String name;
        File out;
        Document doc = null;
        URL imageURL = null;
        File remarkFile;
        Map<String, String> remarkMap;
        for (WebVO webVO : webs) {
            carId = webVO.getCatId();
            name = webVO.getName();
            for (int i = 1; i <= ParseConstants.DEFAULT_PAGE_SIZE; i++) {
                String page = "p" + i + "/";
                url = carId + page;
                try {
                    doc = Jsoup.connect(url).userAgent(ParseConstants.USER_AGENT).timeout(ParseConstants.TIMEOUT).get();
                } catch (IOException e) {
                    //
                }
                Elements els = doc.select("div#pa1 ul#imgList li");
                for (Element el : els) {
                    String t = el.select("a img").attr("src");
                    String src = t.replace("t_", "");
                    String fileName = src.substring(src.lastIndexOf("/") + 1);
                    String alt = el.select("a img").attr("alt");
                    try {
                        imageURL = new URL(src);
                    } catch (MalformedURLException e) {
                        //
                    }
                    out = new File(ParseConstants.IMAGE_PATH + name + DIR_SEPARATOR + fileName);
                    remarkFile = new File(ParseConstants.IMAGE_PATH + name + DIR_SEPARATOR + fileName
                            + ParseConstants.REMARK_FILE_EXT);
                    remarkMap = new HashMap<>(3);
                    remarkMap.put("remark", alt);
                    remarkMap.put("name", name);
                    remarkMap.put("src", src);
                    try {
                        copyURLToFile(imageURL, out, ParseConstants.TIMEOUT, ParseConstants.TIMEOUT);
                        writeStringToFile(remarkFile, toJSONString(remarkMap), ParseConstants.FILE_ENCODING, false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    queue.offer(out);
                    log.info("Store To Disk: {}", out);
                }
            }
        }
    }

}
