# 微信图片压缩之JPEG
[ ![Download](https://api.bintray.com/packages/jakercode/android/imagezip/images/download.svg?version=1.0.0) ](https://bintray.com/jakercode/android/imagezip/1.0.0/link)
在安卓中，低版本的手机的图片压缩质量是不一样，是因为google使用了libjpeg,但是当时在考虑压缩时候，GPU等消耗比较大，所以，在使用libjpeg在压缩图像时，有一个参数叫optimize_coding，使用了false,就是没有使用最优的哈夫曼编码，所以低版本的Android手机的版本是默认使用false，而我的三星7.0，我根据我的压缩效果发现，是一样的。所以在搞版本中，可能有厂商已经把这个底层的地方修改了。
这里我们使用了libjpeg-turbo-android的代码进行自己封装了一个module。
### 构建项目
1.使用依赖库引入
```
dependencies {
//这个是有armeabi,armeabi-v7a
implementation 'com.kye.imagezip:yangjaker:1.0.0'
//只有armeabi
implementation 'com.kye.imagezip:yangjaker:1.0.0-armeabi'
//只有armeabi-v7a
implementation 'com.kye.imagezip:yangjaker:1.0.0-armeabi-v7'
}

```
如果maven找不到报错，那么添加一个maven的地址，在下面的build.gradle里面添加如下
```
allprojects {
    repositories {
        google()
        jcenter()
        maven{
        //地址
            url "https://dl.bintray.com/jakercode/android/"
        }
    }
}

```
2.使用module引入
下载demo ,然后在app build.gradle里面添加如下依赖
```
dependencies {
 implementation project(path: ':imagezip')
}
```

### 如何使用
```
ImageZipUtil.compressByJNI(bitmap, fromJniFlaseFile.getAbsolutePath(), false);
//传递压缩图片的bitmap，输出文件的路径，是否使用哈夫曼编码压缩
```

### 其他
1.如果你觉得包太大了，因为这里面有armeabi和armeabi-v7a的包，如果你只要其中之一，可以把demo 下载下来，删除libs包中的其中之一就可,或者在依赖库里面使用对应的依赖即可




