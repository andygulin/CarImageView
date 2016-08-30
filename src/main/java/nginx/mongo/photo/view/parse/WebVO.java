package nginx.mongo.photo.view.parse;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class WebVO implements Serializable {

	private static final long serialVersionUID = -156412151810316987L;
	private String catId;
	private String name;

	private final String CAR_PHOTO_URL = "http://car.autohome.com.cn/photolist/series/%s/";

	public WebVO(String catId, String name) {
		super();
		String cid = String.format(CAR_PHOTO_URL, catId);
		this.catId = cid;
		this.name = name;
	}

	public String getCatId() {
		return catId;
	}

	public void setCatId(String catId) {
		this.catId = catId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
