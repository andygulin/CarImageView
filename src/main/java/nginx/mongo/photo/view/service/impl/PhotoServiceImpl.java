package nginx.mongo.photo.view.service.impl;

import nginx.mongo.photo.view.entity.Photo;
import nginx.mongo.photo.view.repository.PhotoRepository;
import nginx.mongo.photo.view.service.PhotoService;
import nginx.mongo.photo.view.vo.Page;
import nginx.mongo.photo.view.vo.PhotoVO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final MongoTemplate mongoTemplate;
    private final GridFsTemplate gridFsTemplate;

    public PhotoServiceImpl(PhotoRepository photoRepository, MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate) {
        this.photoRepository = photoRepository;
        this.mongoTemplate = mongoTemplate;
        this.gridFsTemplate = gridFsTemplate;
    }

    @Override
    public Page<PhotoVO> findByPage(String keywords, int pageNo, int pageSize) {
        Query query = getQuery(keywords);
        query.skip(pageNo).limit(pageSize);
        query.with(Sort.by(Direction.DESC, "createAt"));
        List<Photo> photos = mongoTemplate.find(query, Photo.class);
        long count = mongoTemplate.count(getQuery(keywords), Photo.class);
        List<PhotoVO> photoVOs = new ArrayList<>(photos.size());
        for (Photo photo : photos) {
            photoVOs.add(convertVO(photo));
        }
        return Page.build(photoVOs, PhotoVO.class, pageNo, pageSize, count);
    }

    private Query getQuery(String keywords) {
        Query query = new Query();
        if (StringUtils.isNotEmpty(keywords)) {
            Pattern pattern = Pattern.compile("^.*" + keywords + ".*$", Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("name").regex(pattern));
        }
        return query;
    }

    @Override
    public void insert(Photo photo) {
        photoRepository.save(photo);
    }

    @Override
    public String store(File file) {
        InputStream is;
        try {
            is = FileUtils.openInputStream(file);
            return gridFsTemplate.store(is, file.getName()).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PhotoVO convertVO(Photo photo) {
        PhotoVO photoVO = new PhotoVO();
        photoVO.setId(photo.getId());
        photoVO.setName(photo.getName());
        photoVO.setCreateAt(photo.getCreateAt());
        photoVO.setTitle(photo.getTitle());
        photoVO.setThumbnailFileId(photo.getThumbnailFileId());
        photoVO.setThumbnailUrl("/image/" + photo.getThumbnailFileId());
        photoVO.setOriginalFileId(photo.getOriginalFileId());
        photoVO.setOriginalUrl("/image/" + photo.getOriginalFileId());
        return photoVO;
    }

    @Override
    public PhotoVO findById(String id) {
        Optional<Photo> optional = photoRepository.findById(id);
        return optional.map(this::convertVO).orElse(null);
    }

    @Override
    public long count() {
        return photoRepository.count();
    }
}
