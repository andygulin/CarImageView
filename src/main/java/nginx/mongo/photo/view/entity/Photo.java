package nginx.mongo.photo.view.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@ToString
@Document
public class Photo implements Serializable {

    @Id
    private String id;
    @Indexed
    private String name;
    @Indexed
    private String title;

    @Indexed
    private String thumbnailFileId;
    @Indexed
    private Long thumbnailFileCRC32;
    @Indexed
    private String thumbnailSourceFileUrl;

    @Indexed
    private String originalFileId;
    @Indexed
    private Long originalFileCRC32;
    @Indexed
    private String originalSourceFileUrl;

    @Indexed
    private Date createAt;
}
