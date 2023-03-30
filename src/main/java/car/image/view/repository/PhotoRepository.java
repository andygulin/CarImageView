package car.image.view.repository;

import car.image.view.entity.Photo;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PhotoRepository extends PagingAndSortingRepository<Photo, String> {

}
