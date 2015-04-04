#ifndef _pcyanRsa_H_
#define _pcyanRsa_H_

#ifdef __cplusplus
extern "C" {
#endif

// just call this functions
int pcyanRsaDecrypt(const unsigned char *input, unsigned int len, unsigned char *output, int debugFlag);

#ifdef __cplusplus
}
#endif

#endif
