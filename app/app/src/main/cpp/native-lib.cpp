#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_cppsystem_cppbus_CppApp_getXYZ(JNIEnv *env, jobject instance)  {
    std::string txt = "ZjlhZjEwOWQyMDE1ZThhODc2MWRmMWYxNTZjOTBkN2U=";
    return env->NewStringUTF(txt.c_str());
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_cppsystem_cppbus_CppApp_getOPQLM(JNIEnv *env, jobject instance) {
    std::string txt = "ODRjM2I4ZDg2ZWQxM2Q2ZmNmMTZjNTdjZDFiMzdiNzBmZWFjNTU5ODM2Y2QyNTFjNTk5OTU0YTNkMmRhZjU2MGY1YjhlZDZmOGQzNzAwNWNmZTUzM2U0ODk2ZTkwNTMw";
    return env->NewStringUTF(txt.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_cppsystem_cppbus_CppApp_getABDCD(JNIEnv *env, jobject instance) {
    std::string txt = "MWNkZDE3ZmMwODAxMDVmZWYyOTMxNzNmYjI2N2E4MDgxMWMyMTQ2YTZiYmEyYjU0MTMyYWI1NTE5OWZjM2UyNA==";
    return env->NewStringUTF(txt.c_str());
}
