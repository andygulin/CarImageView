package car.image.view.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
public class PhotoVO implements Serializable {

    private String id;
    private String name;
    private String thumbnailFileId;
    private String thumbnailUrl;
    private String originalFileId;
    private String originalUrl;
    private String title;
    private Date createAt;
}
