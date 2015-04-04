LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
APP_ABI := armeabi
APP_OPTIM := debug
APP_PLATFORM = android-14
LOCAL_MODULE    := desrsa
LOCAL_LDLIBS += -llog

#获取srcfiles
LOCAL_SRC_FILES := howbuy_android_piggy_jni_DesUtil.c \
                src_files/des/pcyanDes.c \
                src_files/base64/Base64.c \
                src_files/rsa/pcyanRsa.c \
                src_files/rsa/RSAEuro/desc.c \
                src_files/rsa/RSAEuro/md2c.c \
                src_files/rsa/RSAEuro/md4c.c \
                src_files/rsa/RSAEuro/md5c.c \
                src_files/rsa/RSAEuro/nn.c \
                src_files/rsa/RSAEuro/prime.c \
                src_files/rsa/RSAEuro/r_dh.c \
                src_files/rsa/RSAEuro/r_encode.c \
                src_files/rsa/RSAEuro/r_enhanc.c \
                src_files/rsa/RSAEuro/r_keygen.c \
                src_files/rsa/RSAEuro/r_random.c \
                src_files/rsa/RSAEuro/r_stdlib.c \
                src_files/rsa/RSAEuro/rsa.c \
                src_files/rsa/RSAEuro/shsc.c \

include $(BUILD_SHARED_LIBRARY)
