package nginx.mongo.photo.view.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import nginx.mongo.photo.view.entity.Photo;

@Service
public class PhotoRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insert(Photo photo) {
		mongoTemplate.insert(photo);
	}

	public void insert(List<Photo> photos) {
		mongoTemplate.insert(photos, Photo.class);
	}

	public Photo get(String id) {
		return mongoTemplate.findById(id, Photo.class);
	}

	public void delete(String id) {
		Query query = new Query(where("id").is(id));
		mongoTemplate.findAndRemove(query, Photo.class);
	}

	public List<Photo> findByPage(String keywords, int pageNumber, int pageSize) {
		Query query = getQuery(keywords);
		query.skip(pageNumber).limit(pageSize);
		query.with(new Sort(Direction.DESC, "createAt"));
		return mongoTemplate.find(query, Photo.class);
	}

	public long count(String keywords) {
		return mongoTemplate.count(getQuery(keywords), Photo.class);
	}

	private Query getQuery(String keywords) {
		Query query = new Query();
		if (StringUtils.isNotEmpty(keywords)) {
			Pattern pattern = Pattern.compile("^.*" + keywords + ".*$", Pattern.CASE_INSENSITIVE);
			query.addCriteria(Criteria.where("name").regex(pattern));
		}
		return query;
	}
}
