package nginx.mongo.photo.view.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nginx.mongo.photo.view.service.PhotoService;
import nginx.mongo.photo.view.vo.Page;
import nginx.mongo.photo.view.vo.PhotoVO;

@Controller
public class PhotoController {

	@Autowired
	private PhotoService photoService;

	@GetMapping("/view")
	public String view() {
		return "view";
	}

	@ResponseBody
	@PostMapping("/query")
	public Page<PhotoVO> query(
			@RequestParam(value = "keywords", required = false, defaultValue = StringUtils.EMPTY) String keywords,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
		return photoService.findByPage(keywords, pageNo, pageSize);
	}

	@ResponseBody
	@PostMapping("/get/{id}")
	public PhotoVO get(@PathVariable("id") String id) {
		return photoService.findById(id);
	}
}
