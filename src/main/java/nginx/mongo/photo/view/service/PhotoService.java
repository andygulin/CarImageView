package nginx.mongo.photo.view.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import com.mongodb.gridfs.GridFSFile;

import nginx.mongo.photo.view.entity.Photo;
import nginx.mongo.photo.view.repository.PhotoRepository;

@Service
public class PhotoService {

	@Autowired
	private PhotoRepository photoRepository;
	@Autowired
	private GridFsTemplate gridFsTemplate;

	public void insert(Photo photo) {
		photoRepository.insert(photo);
	}

	public void insert(List<Photo> photos) {
		photoRepository.insert(photos);
	}

	public Photo get(String id) {
		return photoRepository.get(id);
	}

	public void delete(String id) {
		photoRepository.delete(id);
	}

	public List<Photo> findByPage(String keywords, int pageNumber, int pageSize) {
		return photoRepository.findByPage(keywords, pageNumber, pageSize);
	}

	public long count(String keywords) {
		return photoRepository.count(keywords);
	}

	public String store(File file) {
		InputStream is = null;
		try {
			is = FileUtils.openInputStream(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		GridFSFile fsFile = gridFsTemplate.store(is, file.getName());
		return fsFile.getId().toString();
	}
}
