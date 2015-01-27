/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class org_bulldog_linux_jni_NativeMmap */

#ifndef _Included_org_bulldog_linux_jni_NativeMmap
#define _Included_org_bulldog_linux_jni_NativeMmap
#ifdef __cplusplus
extern "C" {
#endif
#undef org_bulldog_linux_jni_NativeMmap_NONE
#define org_bulldog_linux_jni_NativeMmap_NONE 0L
#undef org_bulldog_linux_jni_NativeMmap_READ
#define org_bulldog_linux_jni_NativeMmap_READ 1L
#undef org_bulldog_linux_jni_NativeMmap_WRITE
#define org_bulldog_linux_jni_NativeMmap_WRITE 2L
#undef org_bulldog_linux_jni_NativeMmap_EXEC
#define org_bulldog_linux_jni_NativeMmap_EXEC 4L
#undef org_bulldog_linux_jni_NativeMmap_SHARED
#define org_bulldog_linux_jni_NativeMmap_SHARED 1L
#undef org_bulldog_linux_jni_NativeMmap_PRIVATE
#define org_bulldog_linux_jni_NativeMmap_PRIVATE 2L
/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    createMap
 * Signature: (JJIIIJ)J
 */
JNIEXPORT jlong JNICALL Java_org_bulldog_linux_jni_NativeMmap_createMap
  (JNIEnv *, jclass, jlong, jlong, jint, jint, jint, jlong);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    deleteMap
 * Signature: (JJ)I
 */
JNIEXPORT jint JNICALL Java_org_bulldog_linux_jni_NativeMmap_deleteMap
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    setLongValueAt
 * Signature: (JJ)V
 */
JNIEXPORT void JNICALL Java_org_bulldog_linux_jni_NativeMmap_setLongValueAt
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    getLongValueAt
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_org_bulldog_linux_jni_NativeMmap_getLongValueAt
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    setIntValueAt
 * Signature: (JI)V
 */
JNIEXPORT void JNICALL Java_org_bulldog_linux_jni_NativeMmap_setIntValueAt
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    getIntValueAt
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_org_bulldog_linux_jni_NativeMmap_getIntValueAt
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    setShortValueAt
 * Signature: (JS)V
 */
JNIEXPORT void JNICALL Java_org_bulldog_linux_jni_NativeMmap_setShortValueAt
  (JNIEnv *, jclass, jlong, jshort);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    getShortValueAt
 * Signature: (J)S
 */
JNIEXPORT jshort JNICALL Java_org_bulldog_linux_jni_NativeMmap_getShortValueAt
  (JNIEnv *, jclass, jlong);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    setByteValueAt
 * Signature: (JB)V
 */
JNIEXPORT void JNICALL Java_org_bulldog_linux_jni_NativeMmap_setByteValueAt
  (JNIEnv *, jclass, jlong, jbyte);

/*
 * Class:     org_bulldog_linux_jni_NativeMmap
 * Method:    getByteValueAt
 * Signature: (J)B
 */
JNIEXPORT jbyte JNICALL Java_org_bulldog_linux_jni_NativeMmap_getByteValueAt
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif
