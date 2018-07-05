# 微信图片压缩之JPEG
在安卓中，低版本的手机的图片压缩质量是不一样，是因为google使用了libjpeg,但是当时在考虑压缩时候，GPU等消耗比较大，所以，在使用libjpeg在压缩图像时，有一个参数叫optimize_coding，使用了false,就是没有使用最优的哈夫曼编码，所以低版本的Android手机的版本是默认使用false，而我的三星7.0，我根据我的压缩效果发现，是一样的。所以在搞版本中，可能有厂商已经把这个底层的地方修改了。
这里我们使用了libjpeg-turbo-android的代码进行自己封装了一个module。
1.使用依赖库引入
2.使用module引入

3.如何使用呢
```
ImageZipUtil.compressByJNI(bitmap, fromJniFlaseFile.getAbsolutePath(), false);
//传递压缩图片的bitmap，输出文件的路径，是否使用哈夫曼编码压缩
```