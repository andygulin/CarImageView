package car.image.view.controller;

import car.image.view.service.PhotoService;
import car.image.view.vo.Page;
import car.image.view.vo.PhotoVO;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class PhotoController {

    private final PhotoService photoService;
    private final GridFsTemplate gridFsTemplate;

    public PhotoController(PhotoService photoService, GridFsTemplate gridFsTemplate) {
        this.photoService = photoService;
        this.gridFsTemplate = gridFsTemplate;
    }

    @ResponseBody
    @PostMapping("/query")
    public Page<PhotoVO> query(
            @RequestParam(value = "keywords", required = false, defaultValue = StringUtils.EMPTY) String keywords,
            @RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        return photoService.findByPage(keywords, pageNo, pageSize);
    }

    @ResponseBody
    @PostMapping("/get/{id}")
    public PhotoVO get(@PathVariable("id") String id) {
        return photoService.findById(id);
    }

    @ResponseBody
    @GetMapping("/image/{fileId}")
    public ResponseEntity<byte[]> image(@PathVariable("fileId") String fileId) throws IOException {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        GridFsResource gridFsResource = gridFsTemplate.getResource(gridFSFile);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        byte[] bytes = IOUtils.toByteArray(gridFsResource.getInputStream());
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
