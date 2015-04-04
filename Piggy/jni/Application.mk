LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
APP_ABI := armeabi x86
APP_OPTIM := debug
APP_PLATFORM = android-14
LOCAL_MODULE    := desrsa
LOCAL_LDLIBS += -llog

