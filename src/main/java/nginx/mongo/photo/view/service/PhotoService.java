package nginx.mongo.photo.view.service;

import nginx.mongo.photo.view.entity.Photo;
import nginx.mongo.photo.view.vo.Page;
import nginx.mongo.photo.view.vo.PhotoVO;

import java.io.File;

public interface PhotoService {

    Page<PhotoVO> findByPage(String keywords, int pageNo, int pageSize);

    void insert(Photo photo);

    String store(File file);

    PhotoVO findById(String id);

    long count();

}