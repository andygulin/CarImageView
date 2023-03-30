package nginx.mongo.photo.view.parse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class TransportPhoto implements Serializable {

    private WebVO webVO;

    private File thumbnailFile;
    private Long thumbnailFileCRC32;
    private String thumbnailSourceFileUrl;

    private File originalFile;
    private Long originalFileCRC32;
    private String originalSourceFileUrl;

    private String title;
}
