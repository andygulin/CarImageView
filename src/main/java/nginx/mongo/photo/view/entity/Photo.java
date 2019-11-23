package nginx.mongo.photo.view.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@ToString
@Document
public class Photo {

    @Id
    private String id;
    @Indexed
    private String name;
    @Indexed
    private String fileId;
    @TextIndexed
    private String remark;
    @Indexed
    private Date createAt;
}
