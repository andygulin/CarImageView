<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/jsp/header.jsp"%>

<div class="container-fluid" ng-controller="GridCtrl" ng-init="query();">
	<div class="row" style="margin-top: 20px;">
		<div class="col-md-6">
			<form class="form-inline" ng-submit="setPage(1);">
				<div class="form-group">
					<p class="input-group input-group-sm">
						<input size="50" type="text" name="keywords" ng-model="search.keywords" class="form-control" placeholder="输入关键字搜索...">
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
		<div class="col-md-2" ng-repeat="item in page.result">
			<p>
			<img title="{{item.name}}&#10;{{item.remark}}" class="img-responsive center-block" ng-src="{{item.path}}" ng-click="showImg(item.id);" style="cursor: pointer;" /></a></p>
			<p title="{{item.name}} - {{item.remark}}">{{item.name}} - {{item.remark | limitTo:15}}</p>
			<p>创建时间：{{item.createAt | date:"yyyy-MM-dd HH:mm:ss"}}</p>
		</div>
	</div>
	<div class="row" ng-show="page.result.length > 0">
		<div class="col-sm-12">
			<uib-pagination class="pagination-sm" total-items="page.totalSize" ng-model="page.pageNo" max-size="10" boundary-links="true" items-per-page="page.pageSize" ng-change="query()" first-text="首页" last-text="末页" next-text="下一页" previous-text="上一页"></uib-pagination>
		</div>
	</div>
	
	<div class="modal fade" id="myModal">
		<div class="modal-dialog modal-lg" role="document">
	    	<div class="modal-content">
	    		<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
			        <h2 class="modal-title">{{image.name}} - {{image.remark}}</h2>
			        <h4 class="modal-title">{{image.createAt | date:"yyyy-MM-dd HH:mm:ss"}}</h4>
		    	</div>
				<div class="modal-body">
					<img ng-src="{{image.path}}" class="img-responsive center-block" />
				</div>
	    	</div>
	  	</div>
	</div>
</div>

<script type="text/javascript">
_MAIN_APP_.controller("GridCtrl",
function($scope, $http) {
    $scope.page = {
        pageNo: 1,
        pageSize: 18
    };
    $scope.search = {
        keywords: ""
    };
    $scope.query = function() {
        var data = {
            pageNo: $scope.page.pageNo,
            pageSize: $scope.page.pageSize,
        };
        if ($scope.search.keywords) {
            data.keywords = $scope.search.keywords;
        }
        $http.post("${ctx}/query", data).success(function(resp) {
            if (resp) {
                $scope.page = resp;
            }
        });
    };
    
    $scope.onKeypress = function($event) {
        if ($event.keyCode == 13) {
            $scope.setPage(1);
        }
    };
    
    $scope.setPage = function(pageNo) {
        $scope.page.pageNo = pageNo;
        $scope.query();
    };
    
    $scope.showImg = function(id) {
    	$http.post("${ctx}/get/" + id, {}).success(function(resp) {
            if (resp) {
            	$scope.image = resp;
            	$('#myModal').modal();
            }
        });
    }
});
</script>
<%@ include file="/WEB-INF/jsp/footer.jsp"%>