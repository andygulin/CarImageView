package nginx.mongo.photo.view.parse;

public final class ParseUtils {

    public static String completionImageProtocol(String source) {
        if (!source.startsWith("http:") || !source.startsWith("https:")) {
            return "https:" + source;
        }
        return source;
    }

    public static String completionUrlPrefix(String source) {
        if (!source.startsWith("http:") || !source.startsWith("https:")) {
            return "https://car.autohome.com.cn" + source;
        }
        return source;
    }
}
