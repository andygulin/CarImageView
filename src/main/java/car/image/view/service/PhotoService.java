package car.image.view.service;

import car.image.view.entity.Photo;
import car.image.view.vo.Page;
import car.image.view.vo.PhotoVO;

import java.io.File;

public interface PhotoService {

    Page<PhotoVO> findByPage(String keywords, int pageNo, int pageSize);

    void insert(Photo photo);

    String store(File file);

    PhotoVO findById(String id);

    long count();

}