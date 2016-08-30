package nginx.mongo.photo.view.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import nginx.mongo.photo.view.Constants;
import nginx.mongo.photo.view.entity.Photo;
import nginx.mongo.photo.view.service.PhotoService;
import nginx.mongo.photo.view.vo.Page;
import nginx.mongo.photo.view.vo.PhotoVO;

@Controller
public class PhotoController {

	@Autowired
	private PhotoService photoService;
	@Value("${image.domain}")
	private String imageDomain;

	@GetMapping("/view")
	public String view(@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "keywords", required = false, defaultValue = "") String keywords, Model model,
			HttpServletRequest request) {
		int offset = (pageNumber - 1) * Constants.DEFAULT_PAGE_SIZE;
		Page page = new Page();
		page.setUrl(request.getRequestURI() + "?keywords=" + keywords);
		page.setPageNo(pageNumber);
		long count = photoService.count(keywords);
		page.setCount(count);
		page.setRowPerPage(Constants.DEFAULT_PAGE_SIZE);
		page.setShowNum(10);

		List<Photo> photos = photoService.findByPage(keywords, offset, Constants.DEFAULT_PAGE_SIZE);

		List<PhotoVO> photoVOs = new ArrayList<>(photos.size());
		for (Photo photo : photos) {
			PhotoVO photoVO = new PhotoVO();
			photoVO.setName(photo.getName());
			photoVO.setCreateAt(photo.getCreateAt());
			photoVO.setRemark(photo.getRemark());
			photoVO.setPath(imageDomain + photo.getFileId());
			photoVOs.add(photoVO);
		}

		model.addAttribute("photos", photoVOs);
		model.addAttribute("title", "图片浏览");
		model.addAttribute("pageList", page.pageList());
		model.addAttribute("count", count);
		model.addAttribute("keywords", keywords);
		return "view";
	}
}
