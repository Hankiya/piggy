/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class howbuy_android_piggy_jni_DesUtil */

#ifndef _Included_howbuy_android_piggy_jni_DesUtil
#define _Included_howbuy_android_piggy_jni_DesUtil
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     howbuy_android_piggy_jni_DesUtil
 * Method:    encryptDes
 * Signature: ([B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_howbuy_android_piggy_jni_DesUtil_encryptDes
  (JNIEnv *, jobject, jbyteArray, jbyteArray, jboolean);

/*
 * Class:     howbuy_android_piggy_jni_DesUtil
 * Method:    dencryptDes
 * Signature: ([B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_howbuy_android_piggy_jni_DesUtil_dencryptDes
  (JNIEnv *, jobject, jbyteArray, jbyteArray, jboolean);

/*
 * Class:     howbuy_android_piggy_jni_DesUtil
 * Method:    dencryptRsa
 * Signature: ([B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_howbuy_android_piggy_jni_DesUtil_dencryptRsa
  (JNIEnv *, jobject, jbyteArray, jbyteArray, jboolean);

unsigned char* encrypt(unsigned char* src, unsigned int srcLen, unsigned char* key, unsigned int keyLen, unsigned int* pDestLen, int debugFlag);
unsigned char* decrypt(unsigned char* src, unsigned int srcLen, unsigned char* key, unsigned int keyLen, int debugFlag);
unsigned char* get3DESKey(unsigned char* key, int debugFlag);

#ifdef __cplusplus
}
#endif
#endif