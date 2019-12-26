# javaCV-rtmp-demo

## 从设备获取视频流

>参考链接
>https://blog.csdn.net/eguid_1/article/details/52678775

这块内容主要是javaCV基本API的使用，代码非常简单，我将其写在了一个类中，位于
src/main/java/com/zkalan/capture下，然后再demo类中调用测试；需要注意的是，网
上的很多代码将画板canvas的销毁方式设置为EXIT_ON_CLOSE，这会导致java虚拟机直接
推出，资源无法释放。我认为正确的写法应该是DISPOSED_ON_CLOSE。

```java
canvas.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
```

## 搭建rtmp推流服务器

通过nginx服务器和nginx-rtmp-module可以简单的搭建一个rtmp服务器，我将使用的nginx
编译版本放在了[这里](https://download.csdn.net/download/bucuo12345/12054279)
，这是已经包含了nginx-rtmp-module的版本，使用非常简单，首先在配置文件，例如
nginx-win-rtmp.conf中，添加一段rtmp配置项

```
rtmp {
    server {
        listen 1935;
        chunk_size 4000;
        #mylive就是直播项目名
        application mylive {
             live on;
 
             # record first 1K of stream
             record all;
             record_path /tmp/av;
             record_max_size 1K;
 
             # append current timestamp to each flv
             record_unique on;
 
             # publish only from localhost
             #allow publish 127.0.0.1;
             #deny publish all;
 
             #allow play all;
        }
    }
}
```

然后在命令行中启动nginx服务器，例如命令

```
start nginx.exe -c conf\nginx-win-rtmp.conf
```

最后，启动第一部分写好的demo程序，注意publish address应该是这样的格式
rtmp://localhost:port/mylive/，使用potplayer或者vlc访问该链接，测试推流是否成功。

## 在网页播放rtmp流

>参考链接
>https://blog.csdn.net/qq_30152271/article/details/84334734

操作起来很简单，就是创建一个静态网页，放到服务器目录下，启动任何一个静态服务器，例如
上文的nginx，访问该静态网页，只要地址填写正确，就可以观看推流内容了。

<font color="#99CC00">
然而需要注意的是，videojs虽然声称是一个html5播放器，但它的5.x版本播放rtmp流时依然需要flash
支持，并且6.x及以后版本不支持rtmp播放，也许是为了真正的叫做“html5播放器”？
</font>

## Demo地址
[javaCV-rtmp-demo](https://github.com/zkalan/javaCV-rtmp-demo)

浏览器效果图

![](https://wx4.sinaimg.cn/mw690/005OmCBrly1gaa4n8lli6j30hd0cf42n.jpg)
