#ifndef _BASE64_H_
#define _BASE64_H_

#ifdef __cplusplus
extern "C" {
#endif

void base64en(char *srcStr, char *desStr);
int base64_index(char ch);
void base64de(char *srcStr, char *desStr);

#ifdef __cplusplus
}
#endif

#endif