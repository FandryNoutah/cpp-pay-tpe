#include "secrets.hpp"

#include <jni.h>

#include "sha256.hpp"
#include "sha256.cpp"
//#include "../../../../../../../../Sdk/ndk/16.1.4479499/sysroot/usr/include/string.h"

/* Copyright (c) 2020-present Klaxit SAS
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use,
* copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following
* conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*/
/**
 * Updated by andrinarivo Rakotozafinirina
 * @param s
 * @return
 */
char * roots(const std::string& s) {
    std::string ret;
    for(const char p : s) {
        const char c = '!' + (p - '!' + (11 * 3 + 14)) % (19 * 2 + 56);
        ret += c;
    }
    return  const_cast<char *>(ret.c_str());
}

jstring xyzabcedghaijklm(
        char *hgjdfhgdfgdfjk,
        int si,
        jstring lm,
        JNIEnv *pEnv) {

    // Get the obfuscating string SHA256 as the obfuscator
    const char *kiulm = pEnv->GetStringUTFChars(lm, NULL);
    char buffer[2*SHA256::DIGEST_SIZE + 1];

    sha256(kiulm, buffer);
    const char* obfuscator = buffer;

    // Apply a XOR between the obfuscated key and the obfuscating string to get original string
    char out[si + 1];
    for (int i = 0; i < si; i++) {
        out[i] = hgjdfhgdfgdfjk[i] ^ obfuscator[i % strlen(obfuscator)];
    }
    // Add string terminal delimiter
    out[si] = 0x0;
    return pEnv->NewStringUTF( roots(out));
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_cppsystem_cppbus_Secrets_getlZWKFgMj(
        JNIEnv *pEnv,
        jobject pThis,
        jstring p) {
    char opklmn[] = { 0x24, 0x48, 0x7f, 0xf, 0x4c, 0x41, 0x41, 0x55, 0x4, 0x9, 0x2, 0x5b, 0x11, 0x15, 0x41, 0x4d, 0xc, 0x1, 0x56, 0x4e, 0x1f, 0x4c, 0x4c, 0x14, 0x7, 0xf, 0x40, 0x11, 0x16, 0xf, 0x9, 0x13, 0x53, 0x5, 0x71, 0x43, 0x51, 0x9, 0x40, 0x43, 0x57, 0x77, 0x3e, 0x4c, 0x44, 0x59, 0x7e, 0x16, 0x1a };
    return xyzabcedghaijklm(opklmn, sizeof(opklmn), p, pEnv);
}
