package nginx.mongo.photo.view.parse;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

public class ParseConstants {

	public static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";

	public static final String IMAGE_PATH = FileUtils.getTempDirectoryPath();

	public static final String REMARK_FILE_EXT = ".remark";

	public static final String FILE_ENCODING = "UTF-8";

	public static final int DEFAULT_PAGE_SIZE = 1;

	public static final int TIMEOUT = Integer.MAX_VALUE;

	public static final long WAIT_TIME = 30L;
	public static final TimeUnit WAIT_UNIT = TimeUnit.SECONDS;

	public static final int QUEUE_SIZE = 100;
	public static final int PARSE_LIST_SIZE = 4;

}
