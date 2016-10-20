package nginx.mongo.photo.view.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import nginx.mongo.photo.view.entity.Photo;
import nginx.mongo.photo.view.repository.PhotoRepository;
import nginx.mongo.photo.view.service.PhotoService;
import nginx.mongo.photo.view.vo.Page;
import nginx.mongo.photo.view.vo.PhotoVO;

@Service
public class PhotoServiceImpl implements PhotoService {

	@Autowired
	private PhotoRepository photoRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Value("${image.domain}")
	private String imageDomain;

	@Override
	public Page<PhotoVO> findByPage(String keywords, int pageNo, int pageSize) {
		Query query = getQuery(keywords);
		query.skip(pageNo).limit(pageSize);
		query.with(new Sort(Direction.DESC, "createAt"));
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
	public String insert(Photo photo) {
		return photoRepository.save(photo).getId();
	}

	@Override
	public String store(File file) {
		InputStream is = null;
		try {
			is = FileUtils.openInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gridFsTemplate.store(is, file.getName()).getId().toString();
	}

	private PhotoVO convertVO(Photo photo) {
		PhotoVO photoVO = new PhotoVO();
		photoVO.setId(photo.getId());
		photoVO.setName(photo.getName());
		photoVO.setCreateAt(photo.getCreateAt());
		photoVO.setRemark(photo.getRemark());
		photoVO.setPath(imageDomain + photo.getFileId());
		return photoVO;
	}

	@Override
	public PhotoVO findById(String id) {
		Photo photo = photoRepository.findOne(id);
		PhotoVO photoVO = convertVO(photo);
		return photoVO;
	}

	@Override
	public long count() {
		return photoRepository.count();
	}
}
