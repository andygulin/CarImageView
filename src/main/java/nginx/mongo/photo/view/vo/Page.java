package nginx.mongo.photo.view.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Page {

	private String url = "";
	private Integer pageNo = 1;
	private Long count = 0L;
	private Integer rowPerPage = 10;
	private Integer showNum = 5;
	private Integer totalPages = 0;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Integer getRowPerPage() {
		return rowPerPage;
	}

	public void setRowPerPage(Integer rowPerPage) {
		this.rowPerPage = rowPerPage;
	}

	public Integer getShowNum() {
		return showNum;
	}

	public void setShowNum(Integer showNum) {
		this.showNum = showNum;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public String pageList() {
		StringBuffer page = new StringBuffer();
		int totalPages = (int) ((this.count + this.rowPerPage - 1) / this.rowPerPage);
		int begin = Math.max(1, this.pageNo - this.showNum / 2);
		int end = Math.min(begin + (this.showNum - 1), totalPages);
		final int INDEX_NOT_FOUND = -1;
		this.url = this.url.endsWith("?") ? this.url.substring(0, this.url.length() - 1) : this.url;
		String linkChar = this.url.indexOf("?") != INDEX_NOT_FOUND ? "&" : "?";
		this.url += linkChar;
		page.append("<nav><ul class=\"pagination\">");
		if (this.pageNo > 1) {
			page.append("<li><a href=\"" + this.url + "page=1\">首页</a></li>");
			page.append("<li><a href=\"" + this.url + "page=" + (this.pageNo - 1) + "\">上一页</a></li>");
		} else {
			page.append("<li class=\"disabled\"><a href=\"#\">首页</a></li>");
			page.append("<li class=\"disabled\"><a href=\"#\">上一页</a></li>");
		}
		for (int i = begin; i <= end; i++) {
			if (i == this.pageNo) {
				page.append("<li class=\"active\"><a href=\"" + this.url + "page=" + i + "\">" + i + "</a></li>");
			} else {
				page.append("<li><a href=\"" + this.url + "page=" + i + "\">" + i + "</a></li>");
			}
		}
		if (this.pageNo < totalPages) {
			page.append("<li><a href=\"" + this.url + "page=" + (this.pageNo + 1) + "\">下一页</a></li>");
			page.append("<li><a href=\"" + this.url + "page=" + totalPages + "\">末页</a></li>");
		} else {
			page.append("<li class=\"disabled\"><a href=\"#\">下一页</a></li>");
			page.append("<li class=\"disabled\"><a href=\"#\">末页</a></li>");
		}
		page.append("</ul></nav>");
		return page.toString();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
