package car.image.view.parse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class WebVO implements Serializable {

    private static final long serialVersionUID = -156412151810316987L;
    private Integer catId;
    private String name;
    private String url;

    public WebVO(Integer catId, String name) {
        super();
        this.catId = catId;
        this.name = name;
        this.url = String.format("https://car.autohome.com.cn/pic/series/%d.html", catId);
    }

}
