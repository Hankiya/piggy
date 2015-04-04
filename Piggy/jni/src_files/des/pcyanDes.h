#ifndef tripleDESTest_pcyanDes_h
#define tripleDESTest_pcyanDes_h

#ifdef __cplusplus
extern "C" {
#endif

#define		ENCRYPT		0
#define		DESCRYPT	1
#define     PINLEN      9

void AscToBcd( unsigned char* charbcd, unsigned char* charasc, int len );
void BcdToAsc( unsigned char* charasc,unsigned char* charbcd, int len );
void ShowHex( char* name, unsigned char* string, int len );

void key_encrypt_Asc(unsigned char* input,unsigned char* key);
int key_encrypt_Bcd(unsigned char* input,unsigned char* key);

void key_uncrypt_Asc(unsigned char* input,unsigned char* key);
int key_uncrypt_Bcd(unsigned char* input,unsigned char* key);

void pan_encrypt_Bcd(unsigned char* input, unsigned char* pan,unsigned char* key);
void pan_encrypt_Asc(unsigned char* input, unsigned char* pan,unsigned char* key);
void pan_uncrypt_Bcd(unsigned char* input,unsigned char* pan,unsigned char* key);
void pan_uncrypt_Asc(unsigned char* input,unsigned  char* pan,unsigned  char* key);

unsigned char cdesoutput[9];
unsigned char 	cascoutput[17];
unsigned char cdeskey[9];
unsigned char cdesinput[9];

unsigned char	KS[16][48];
unsigned char	E[64];

int _setkey_(unsigned char* key);
int _encrypt_(unsigned char* block, int edflag);
int expand(unsigned char* in, unsigned char* out);
int compress(unsigned char* in, unsigned char* out);
void TriDes(unsigned char *input,unsigned char *doublekey,unsigned char *output);
void TriUnDes(unsigned char *input, unsigned char *doublekey, unsigned char *output);
void Undes(unsigned char *pin,unsigned char *workkey,unsigned char *cipher_pin);
void Des(unsigned char *pin,unsigned char *workkey,unsigned char *cipher_pin);
int StringToMAC(unsigned char * string,unsigned char * MacKey,unsigned char * MAC);

// just call this functions
void pcyan3DESEncode(const unsigned char *input, const unsigned int inputLen, const unsigned char *doublekey, unsigned char *output, unsigned int* pOutputLen);
void pcyan3DESDecode(const unsigned char *input, const unsigned int inputLen, const unsigned char *doublekey, unsigned char *output, unsigned int* pOutputLen);

#ifdef __cplusplus
}
#endif

#endif
