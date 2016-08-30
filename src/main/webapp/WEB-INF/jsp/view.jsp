<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/header.jsp"%>

<div class="container-fluid">
	<div class="row" style="margin-top: 20px;">
		<div class="col-md-6">
			<form class="form-inline">
				<div class="form-group">
					<p class="input-group input-group-sm">
						<input size="50" type="text" name="keywords" value="${param.keywords }" class="form-control" placeholder="输入关键字搜索...">
					</p>
				</div>
				<div class="form-group m-btn-group-fix">
					<p class="input-group input-group-sm">
						<span class="input-group-btn">
							<button type="submit" class="btn btn-primary">GO</button>
						</span>
					</p>
				</div>
			</form>
		</div>
	</div>
	<div class="row" style="margin-top: 10px;">
		<c:forEach var="pt" items="${photos }">
			<div class="col-md-3">
				<p>
				<a class="image-popup-no-margins" href="${pt.path }">
				<img title="${pt.name }&#10;${pt.remark }" class="img-polaroid" src="${pt.path }" style="width:420px;" /></a></p>
				<p title="${pt.name } - ${pt.remark }">${pt.name } - ${fn:length(pt.remark)>15?fn:substring(pt.remark,0,15):pt.remark }</p>
				<p>创建时间：<fmt:formatDate value="${pt.createAt}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
			</div>
		</c:forEach>
	</div>
	${pageList }
</div>
<<script type="text/javascript">
$(function() {
    $('.image-popup-no-margins').magnificPopup({
        type: 'image',
        closeOnContentClick: true,
        closeBtnInside: false,
        fixedContentPos: true,
        mainClass: 'mfp-no-margins mfp-with-zoom',
        image: {
          verticalFit: true
        },
        zoom: {
          enabled: true,
          duration: 300
        }
      });
});
</script>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>