## 抓取图片入库到mongodb，通过nginx-gridfs访问图片

### 下载 nginx-gridfs
```
yum install git
git clone https://github.com/mdirolf/nginx-gridfs.git
cd nginx-gridfs
git checkout v0.8
git submodule init
git submodule update
```

### 安装 nginx
```
./configure --prefix=/data/nginx --with-http_ssl_module --add-module=/home/nginx-gridfs/
make && make install
```

### nginx配置
```
location /image/ {
    gridfs photo <!--数据库名字-->
    root_collection=fs <!--gridfs的前缀-->
    field=_id <!--默认_id,还可以为filename-->
    type=objectid;
    mongo 127.0.0.1:27017;
}
```

### 运行
```
nginx.mongo.photo.view.Application
```

### 访问
[http://localhost:8080/view](http://localhost:8080/view)

