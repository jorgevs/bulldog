/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_bulldog_linux_jni_NativeTools */

#ifndef _Included_org_bulldog_linux_jni_NativeTools
#define _Included_org_bulldog_linux_jni_NativeTools
#ifdef __cplusplus
extern "C" {
#endif
#undef org_bulldog_linux_jni_NativeTools_OPEN_READ_ONLY
#define org_bulldog_linux_jni_NativeTools_OPEN_READ_ONLY 0L
#undef org_bulldog_linux_jni_NativeTools_OPEN_WRITE_ONLY
#define org_bulldog_linux_jni_NativeTools_OPEN_WRITE_ONLY 1L
#undef org_bulldog_linux_jni_NativeTools_OPEN_READ_WRITE
#define org_bulldog_linux_jni_NativeTools_OPEN_READ_WRITE 2L
/*
 * Class:     org_bulldog_linux_jni_NativeTools
 * Method:    getJavaDescriptor
 * Signature: (I)Ljava/io/FileDescriptor;
 */
JNIEXPORT jobject JNICALL Java_org_bulldog_linux_jni_NativeTools_getJavaDescriptor
  (JNIEnv *, jclass, jint);

/*
 * Class:     org_bulldog_linux_jni_NativeTools
 * Method:    open
 * Signature: (Ljava/lang/String;I)I
 */
JNIEXPORT jint JNICALL Java_org_bulldog_linux_jni_NativeTools_open
  (JNIEnv *, jclass, jstring, jint);

/*
 * Class:     org_bulldog_linux_jni_NativeTools
 * Method:    close
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_bulldog_linux_jni_NativeTools_close
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
