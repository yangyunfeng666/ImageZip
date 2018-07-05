# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
#添加文件包
include_directories(src/main/jni/libjpeg-turbo/)
include_directories(src/main/jni/libjpeg-turbo/android/)
add_library( # Sets the name of the library.
             imageZip

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/jni/bitherlibjni.c)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.





add_library(jpegbither SHARED IMPORTED )
set_target_properties(
    jpegbither
    PROPERTIES
    IMPORTED_LOCATION   ${CMAKE_SOURCE_DIR}/libs/${ANDROID_ABI}/libjpegbither.so
)


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )
target_link_libraries( # Specifies the target library.
                       imageZip
                       jpegbither
                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )