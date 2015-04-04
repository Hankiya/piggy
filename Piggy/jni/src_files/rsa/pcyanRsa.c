#include ".\RSAEuro\rsa.h"
#include "pcyanRsa.h"
#include <stdio.h>
#include <string.h>

static R_RSA_PUBLIC_KEY PUBLIC_KEY_DEBUG = { 1024, { 0xBB, 0x94, 0xA9, 0xC7,
		0x9F, 0xCC, 0xDF, 0x04, 0xCF, 0x00, 0x51, 0x27, 0xB4, 0xF5, 0xDC, 0x04,
		0x17, 0x20, 0x77, 0x64, 0x87, 0xBA, 0x04, 0x0B, 0x6D, 0x55, 0xD2, 0x8A,
		0x98, 0x08, 0x44, 0x79, 0x25, 0x3D, 0x28, 0xDF, 0xC9, 0xEE, 0x6A, 0xD2,
		0x99, 0xB8, 0xB6, 0xC8, 0xEA, 0xE2, 0x34, 0xBC, 0x78, 0x13, 0x70, 0x26,
		0x5B, 0xAB, 0xE6, 0x1D, 0x04, 0x5E, 0x68, 0x12, 0xE1, 0xF6, 0xF6, 0x58,
		0x07, 0x1A, 0x72, 0x0B, 0x08, 0x22, 0x31, 0xAD, 0x42, 0x85, 0xC9, 0x14,
		0x03, 0xD9, 0xE5, 0x4C, 0xBA, 0x75, 0x29, 0x4A, 0xE7, 0xA9, 0x3B, 0x55,
		0xB7, 0xEF, 0x0E, 0x21, 0xF5, 0xC5, 0x73, 0x11, 0xEE, 0x67, 0xE9, 0x3D,
		0xD6, 0xAF, 0xDC, 0xF6, 0xFD, 0xA4, 0xB0, 0x42, 0xE0, 0x8E, 0x05, 0x13,
		0x30, 0xE7, 0xF0, 0x2B, 0x7A, 0x74, 0x4F, 0x87, 0xF3, 0x32, 0x28, 0x5D,
		0x46, 0xDD, 0x67, 0x97 }, { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00,
		0x01 } };

static R_RSA_PUBLIC_KEY PUBLIC_KEY_RELEASE = { 1024, { 0xD8, 0xEE, 0x48, 0x79,
		0xA2, 0x48, 0x64, 0xD0, 0x46, 0x01, 0xD1, 0xA8, 0x6F, 0x05, 0xFD, 0x98,
		0xE3, 0x76, 0x49, 0xD7, 0x58, 0x15, 0xC9, 0x2C, 0xD7, 0xAB, 0x83, 0xBB,
		0x03, 0x86, 0x26, 0xE2, 0xEF, 0x17, 0xD9, 0x7F, 0xA2, 0x54, 0x48, 0x72,
		0xBD, 0x1B, 0x54, 0x7E, 0xDA, 0xFB, 0xDF, 0x51, 0xE6, 0xC5, 0xEB, 0x85,
		0x8B, 0xE5, 0x1C, 0x42, 0x46, 0xFB, 0xB7, 0x40, 0x48, 0xF6, 0x46, 0xE7,
		0xBF, 0xDF, 0xCD, 0xF8, 0xEA, 0x08, 0xF9, 0x3D, 0x99, 0x12, 0xBB, 0xDF,
		0xC4, 0x8E, 0x16, 0x5A, 0x51, 0xDA, 0xF3, 0x1F, 0x12, 0x71, 0xA9, 0x51,
		0xBB, 0xF1, 0x10, 0x2D, 0x5F, 0x9B, 0xF6, 0x13, 0xCE, 0xFD, 0xCC, 0xD0,
		0xFF, 0x4E, 0x4C, 0xB2, 0x4F, 0x65, 0x2D, 0xBF, 0x61, 0x36, 0xB9, 0x60,
		0xA2, 0x6B, 0x79, 0x8D, 0x8E, 0x1C, 0xCE, 0xB7, 0xB7, 0x22, 0xF3, 0x31,
		0x45, 0xCF, 0x0F, 0x01 }, { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00,
		0x01 } };

void GetPublicKey(R_RSA_PUBLIC_KEY **publicKey, int debugFlag) {
	if (debugFlag)
	{
		*publicKey = &PUBLIC_KEY_DEBUG;
	}
	else
	{
		*publicKey = &PUBLIC_KEY_RELEASE;
	}
}

int pcyanRsaDecrypt(const unsigned char *input, unsigned int len,
		unsigned char *output, int debugFlag) {
	unsigned int status = 0, outputLength = 0;
	R_RSA_PUBLIC_KEY *publicKey = 0x00;

	GetPublicKey(&publicKey, debugFlag);
	status = RSAPublicDecrypt(output, &outputLength, (unsigned char*) input,
			len, publicKey);
	if (status) {
		printf("RSAPrivateDecrypt failed with %x\n", status);
		return -1;
	}

	return 0;
}
