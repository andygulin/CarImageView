package nginx.mongo.photo.view.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class PhotoVO {

    private String id;
    private String name;
    private String path;
    private String remark;
    private Date createAt;
}
