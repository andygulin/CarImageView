package nginx.mongo.photo.view.repository;

import nginx.mongo.photo.view.entity.Photo;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, String> {

}
