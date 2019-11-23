package nginx.mongo.photo.view.parse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class WebVO implements Serializable {

    private static final long serialVersionUID = -156412151810316987L;
    private String catId;
    private String name;

    private final String CAR_PHOTO_URL = "http://car.autohome.com.cn/photolist/series/%s/";

    public WebVO(String catId, String name) {
        super();
        this.catId = String.format(CAR_PHOTO_URL, catId);
        this.name = name;
    }

}
