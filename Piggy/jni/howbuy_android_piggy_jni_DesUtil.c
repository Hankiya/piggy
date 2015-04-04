#include <string.h>
#include <jni.h>
#include <android/log.h>
#include "howbuy_android_piggy_jni_DesUtil.h"
#include "./src_files/rsa/pcyanRsa.h"
#include "./src_files/des/pcyanDes.h"

static unsigned char* sKey = 0x00;
static unsigned int sKeyLen = 0;
static unsigned char* s3DesKey = 0x00;

JNIEXPORT jbyteArray JNICALL Java_howbuy_android_piggy_jni_DesUtil_encryptDes(
		JNIEnv *aEnv, jobject aObj, jbyteArray aSrc, jbyteArray aKey, jboolean debugFlag) {

	unsigned char* src = NULL;
	unsigned char* key = NULL;
	unsigned char* dest = NULL;
	int srcLen = 0;
	int keyLen = 0;
	int destLen = 0;

	src = (*aEnv)->GetByteArrayElements(aEnv, aSrc, NULL);
	if (NULL == src) {
		return NULL;
	}

	key = (*aEnv)->GetByteArrayElements(aEnv, aKey, NULL);
	if (NULL == key) {
		return NULL;
	}

	srcLen = (*aEnv)->GetArrayLength(aEnv, aSrc);
	keyLen = (*aEnv)->GetArrayLength(aEnv, aKey);
	dest = encrypt(src, srcLen, key, keyLen, &destLen, debugFlag);

	jbyteArray result = (*aEnv)->NewByteArray(aEnv, destLen);
	(*aEnv)->SetByteArrayRegion(aEnv, result, 0, destLen, dest);
	free(dest);

	(*aEnv)->ReleaseByteArrayElements(aEnv, aKey, key, 0);
	(*aEnv)->ReleaseByteArrayElements(aEnv, aSrc, src, 0);

	return result;

}

/*
 * Class:     com_tang_testndk_DesUtil
 * Method:    dencrypt
 * Signature: ([B[B)[B
 */JNIEXPORT jbyteArray JNICALL Java_howbuy_android_piggy_jni_DesUtil_dencryptDes(
		JNIEnv *aEnv, jobject aObj, jbyteArray aSrc, jbyteArray aKey, jboolean debugFlag) {
	unsigned char* src = NULL;
	unsigned char* key = NULL;
	unsigned char* dest = NULL;
	int srcLen = 0;
	int keyLen = 0;
	int destLen = 0;

	src = (*aEnv)->GetByteArrayElements(aEnv, aSrc, NULL);
	if (NULL == src) {
		return NULL;
	}

	key = (*aEnv)->GetByteArrayElements(aEnv, aKey, NULL);
	if (NULL == key) {
		return NULL;
	}

	srcLen = (*aEnv)->GetArrayLength(aEnv, aSrc);
	keyLen = (*aEnv)->GetArrayLength(aEnv, aKey);
	dest = decrypt(src, srcLen, key, keyLen, debugFlag);
	destLen = strlen(dest);

	jbyteArray result = (*aEnv)->NewByteArray(aEnv, destLen);
	(*aEnv)->SetByteArrayRegion(aEnv, result, 0, destLen, dest);
	free(dest);

	(*aEnv)->ReleaseByteArrayElements(aEnv, aKey, key, 0);
	(*aEnv)->ReleaseByteArrayElements(aEnv, aSrc, src, 0);

	return result;
}

/*
 * Class:     com_tang_testndk_DesUtil
 * Method:    dencryptRsa
 * Signature: ([B[B)[B
 */JNIEXPORT jbyteArray JNICALL Java_howbuy_android_piggy_jni_DesUtil_dencryptRsa(
		JNIEnv *aEnv, jobject aObj, jbyteArray aSrc, jbyteArray aKey, jboolean debugFlag) {
	unsigned char* dest = NULL;
	int destLen = 0;
	unsigned char* key = NULL;

	key = (*aEnv)->GetByteArrayElements(aEnv, aKey, NULL);
	if (NULL == key) {
		return NULL;
	}

	dest = get3DESKey(key, debugFlag);
	if (dest == NULL)
		return NULL;

	destLen = strlen(dest);

	jbyteArray result = (*aEnv)->NewByteArray(aEnv, destLen);
	(*aEnv)->SetByteArrayRegion(aEnv, result, 0, destLen, dest);
	free(dest);

	(*aEnv)->ReleaseByteArrayElements(aEnv, aKey, key, 0);
	return result;
}

unsigned char* encrypt(unsigned char* src, unsigned int srcLen,
		unsigned char* key, unsigned int keyLen, unsigned int* pDestLen, int debugFlag) {
	int i = 0;
	int j = 0;
	unsigned int destLen = (srcLen + 7) / 8 * 8;
	unsigned char* destData = NULL;

	if (srcLen % 8 == 0)
		destLen += 8;
	destData = malloc(destLen + 1);
	*pDestLen = destLen;

	// rsa解密得到3deskey的明文
	if (sKey != 0x00) {
		// 检查是否为新key
		if (keyLen == sKeyLen && strcmp(sKey, key) == 0) {
			// 旧key，就不用解密了
		} else // 新key，释放内存，准备重新解密key
		{
			free(sKey);
			sKey = 0x00;
			free(s3DesKey);
			sKey = 0x00;
			sKeyLen = 0;
		}
	}
	if (sKey == 0x00) {
		sKey = malloc(keyLen + 1);
		memset(sKey, 0, keyLen + 1);
		memcpy(sKey, key, keyLen);

		sKeyLen = keyLen;

		s3DesKey = malloc(keyLen + 1);
		memset(s3DesKey, 0, keyLen + 1);

		pcyanRsaDecrypt(sKey, sKeyLen, s3DesKey, debugFlag);
	}

	memset(destData, 0, destLen + 1);
	pcyan3DESEncode(src, srcLen, s3DesKey, destData, pDestLen);
	__android_log_print(ANDROID_LOG_INFO, "rsakey", "encrypt %s",s3DesKey);
	return destData;
}

unsigned char* decrypt(unsigned char* src, unsigned int srcLen,
		unsigned char* key, unsigned int keyLen, int debugFlag) {
	int i = 0;
	int j = 0;
	unsigned int destLen = (srcLen + 7) / 8 * 8;
	unsigned char* destData = malloc(destLen + 1);

	// rsa解密得到3deskey的明文
	if (sKey != 0x00) {
		// 检查是否为新key
		if (keyLen == sKeyLen && strcmp(sKey, key) == 0) {
			// 旧key，就不用解密了
		} else // 新key，释放内存，准备重新解密key
		{
			free(sKey);
			sKey = 0x00;
			free(s3DesKey);
			sKey = 0x00;
			sKeyLen = 0;
		}
	}
	if (sKey == 0x00) {
		sKey = malloc(keyLen + 1);
		memset(sKey, 0, keyLen + 1);
		memcpy(sKey, key, keyLen);

		sKeyLen = keyLen;

		s3DesKey = malloc(keyLen + 1);
		memset(s3DesKey, 0, keyLen + 1);

		pcyanRsaDecrypt(sKey, sKeyLen, s3DesKey, debugFlag);
	}

	memset(destData, 0, destLen + 1);
	pcyan3DESDecode(src, srcLen, s3DesKey, destData, &destLen);
	__android_log_print(ANDROID_LOG_INFO, "rsakey", "decrypt %s",s3DesKey);
	return destData;
}

unsigned char* get3DESKey(unsigned char* key, int debugFlag) {
	__android_log_print(ANDROID_LOG_INFO, "rsakey", "get3DESKey");
	unsigned int keyLen = strlen(key);
	int result = 0;
	unsigned char* tripleDesKey = 0x00;

	tripleDesKey = malloc(keyLen + 1);
	memset(tripleDesKey, 0, keyLen + 1);

	result = pcyanRsaDecrypt(key, keyLen, tripleDesKey, debugFlag);
	if (result)
		return NULL;

	return tripleDesKey;

}
