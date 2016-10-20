package nginx.mongo.photo.view.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import nginx.mongo.photo.view.entity.Photo;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, String> {

}
