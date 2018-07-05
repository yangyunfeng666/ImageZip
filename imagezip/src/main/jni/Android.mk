LOCAL_PATH := $(call my-dir)


#jpeg-turbo
include $(CLEAR_VARS)
LOCAL_MODULE    := libjpegbither
LOCAL_SRC_FILES := libjpegbither.so
include $(PREBUILT_SHARED_LIBRARY)


include $(CLEAR_VARS)
LOCAL_MODULE    := bitherjni
LOCAL_SRC_FILES := bitherlibjni.c
LOCAL_SHARED_LIBRARIES := libjpegbither
LOCAL_LDLIBS := -ljnigraphics -llog
LOCAL_C_INCLUDES := $(LOCAL_PATH) \
                    $(LOCAL_PATH)/libjpeg-turbo \
                    $(LOCAL_PATH)/libjpeg-turbo/android
include $(BUILD_SHARED_LIBRARY)