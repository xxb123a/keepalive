//
// Created by skye on 2021/4/20.
//

#include <jni.h>
#include <sys/wait.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/file.h>

#include "log.h"


int lock_file(char *lock_file_path) {
    LOGD("start try to lock file >> %s <<", lock_file_path);
    int lockFd = open(lock_file_path, O_RDONLY);
    if (lockFd == -1) {
        lockFd = open(lock_file_path, O_CREAT, S_IRUSR);
    }
    int lockRet = flock(lockFd, LOCK_EX);
    if (lockRet == -1) {
        LOGE("lock file failed >> %s <<", lock_file_path);
        return 0;
    } else {
        LOGD("lock file success  >> %s <<", lock_file_path);
        return 1;
    }
}

void create_file_if_not_exist(char *path) {
    FILE *fp = fopen(path, "ab+");
    if (fp) {
        fclose(fp);
    }
}

void set_process_name(JNIEnv *env, jstring processName)
{
    char *process_name = (char*)(*env)->GetStringUTFChars(env, processName, 0);

    // 1.找到java代码native方法所在的字节码文件
    // jclass (*FindClass)(JNIEnv*, const char*);
    jclass clazz = (*env)->FindClass(env, "com/fork/kp/DaemonNative");
    if(clazz == 0){
        return;
    }
    // 2.找到class里面对应的方法
    // jmethodID (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);
    jmethodID method = (*env)->GetStaticMethodID(env, clazz, "setProcessName", "(Ljava/lang/String;)V");
    if(method == 0){
        return;
    }
    // 3.调用方法
    // void (*CallVoidMethod)(JNIEnv*, jobject, jmethodID, ...);
    (*env)->CallStaticVoidMethod(env, clazz, method, (*env)->NewStringUTF(env, process_name));
}

void restartProgress(JNIEnv *env)
{
    // 在C语言中调用Java的空方法
    // 1.找到java代码native方法所在的字节码文件
    // jclass (*FindClass)(JNIEnv*, const char*);
    jclass clazz = (*env)->FindClass(env, "com/fork/kp/DaemonNative");
    if(clazz == 0){
        LOGD("find class error");
        return;
    }
    // 2.找到class里面对应的方法
    // jmethodID (*GetMethodID)(JNIEnv*, jclass, const char*, const char*);
    jmethodID method = (*env)->GetStaticMethodID(env, clazz, "restartProcess", "()V");
    if(method == 0){
        LOGD("find method error");
    }
    // 3.调用方法
    // void (*CallVoidMethod)(JNIEnv*, jobject, jmethodID, ...);
    (*env)->CallStaticVoidMethod(env, clazz, method);
}

//JNIEnv *env, jstring waitFile, jstring waitIndicatorFile
void waitOtherFile(JNIEnv *env, jstring waitFile, jstring waitIndicatorFile)
{
//    int *v3; // r4
//    int v4; // r9
//    int v5; // r8
//    int v6; // r6
//    const char *v7; // r5
//    int v8; // r0
//    int v9; // r1
//    const char *v10; // r2
//    int v11; // r6
//    int v12; // r0
//    int v13; // r5
//    int v14; // r0

//    v3 = env;
//    v4 = waitIndicatorFile;
//    v5 = waitFile;
    int retry_times = 0;
    //v7 = (const char *)(*(int (__fastcall **)(int *, int, _DWORD))(*env + 676))(env, waitIndicatorFile, 0);
    char *wait_indicator_file = (char*)(*env)->GetStringUTFChars(env, waitIndicatorFile, 0);
    while ( open(wait_indicator_file, O_RDONLY) == -1 )
    {
        sleep(1);
        if ( ++retry_times >= 30 )
        {
//            v8 = *env;
//            v9 = waitIndicatorFile;
//            v10 = wait_indicator_file;
//            return (*(int (__fastcall **)(int *, int, const char *))(env + 680))(env, waitIndicatorFile, wait_indicator_file);
            (*env)->ReleaseStringUTFChars(env, waitIndicatorFile, wait_indicator_file);
            return;
        }
    }
    remove(wait_indicator_file);
//    (*(void (__fastcall **)(int *, int, const char *))(*env + 680))(env, waitIndicatorFile, wait_indicator_file);
    (*env)->ReleaseStringUTFChars(env, waitIndicatorFile, wait_indicator_file);
//    v11 = (*(int (__fastcall **)(int *, int, _DWORD))(*env + 676))(env, waitFile, 0);
    char *wait_file = (char*)(*env)->GetStringUTFChars(env, waitFile, 0);
    if ( lock_file(wait_file) )
    {
        /*
        //v12 = (*(int (__fastcall **)(int *, const char *))(*env + 24))(env, "com/vi/daemon/DaemonNative");
        jclass clazz = (*env)->FindClass(env, "com/vi/daemon/DaemonNative");
//        v13 = v12;
        //v14 = (*(int (__fastcall **)(int *, int, const char *, int (*)()))(*env + 452))(env, v12, "restartProcess", sub_B48);
        jmethodID method = (*env)->GetStaticMethodID(env, clazz, "restartProcess", "()V");
        //(*(void (__fastcall **)(int *, int, int))(*env + 564))(env, v12, v14);
        (*env)->CallStaticVoidMethod(env, clazz, method);
         */
        restartProgress(env);
    }
//    v8 = *env;
//    v9 = waitFile;
//    v10 = (const char *)wait_file;
//    return (*(int (__fastcall **)(int *, int, const char *))(env + 680))(env, waitFile, wait_file);
    (*env)->ReleaseStringUTFChars(env, waitFile, wait_file);
}

//JNIEnv *env, jstring lockFile, jstring waitFile, jstring indicatorFile, jstring waitIndicatorFile
void lockAndWait(JNIEnv *env, jstring lockFile, jstring waitFile, jstring indicatorFile, jstring waitIndicatorFile)
{
//    int *v5; // r4
//    int v6; // r9
//    int v7; // r8
//    int v8; // r5
//    int v9; // r6
//    int v10; // r0
//    int v11; // r1
//    int v12; // r6

//    v5 = a1;
//    v6 = indicatorFile;
//    v7 = waitFile;
//    v8 = lockFile;
//    v9 = (*(int (__fastcall **)(int *, int, _DWORD))(*env + 676))(env, lockFile, 0);
    char *_lock_file = (char*)(*env)->GetStringUTFChars(env, lockFile, 0);

    create_file_if_not_exist(_lock_file);
    int lock_stat = lock_file(_lock_file);
//    v11 = *env;
    if ( !lock_stat )
    {
//        return (*(int (__fastcall **)(int *, int, int))(env + 680))(env, lockFile, _lock_file);
        (*env)->ReleaseStringUTFChars(env, lockFile, _lock_file);
        return;
    }


//    v12 = (*(int (__fastcall **)(int *, int, _DWORD))(env + 676))(env, indicatorFile, 0);
    char *indicator_file = (char*)(*env)->GetStringUTFChars(env, indicatorFile, 0);
    create_file_if_not_exist(indicator_file);
//    (*(void (__fastcall **)(int *, int, int))(*env + 680))(env, indicatorFile, indicator_file);
    (*env)->ReleaseStringUTFChars(env, indicatorFile, indicator_file);
    waitOtherFile(env, waitFile, waitIndicatorFile);
}

JNIEXPORT void JNICALL Java_com_fork_kp_DaemonNative_forkChild
        (JNIEnv *env, jclass jobj, jstring processName, jstring lockFile, jstring waitFile, jstring indicatorFile, jstring waitIndicatorFile)
{
    __pid_t pid = fork();
    if ( pid == 0)
    {
        pid = fork();
        if ( pid > 0 )
        {
            exit(0);
        }
        else if (pid == 0)
        {
            setsid();
            set_process_name(env, processName);
            lockAndWait(env, lockFile, waitFile, indicatorFile, waitIndicatorFile);
        }
    }
    else if (pid > 0)
    {
        waitpid(pid, NULL, 0);
    }
}

JNIEXPORT jint JNICALL Java_com_vi_daemon_DaemonNative_lockFile(JNIEnv *env, jclass jobj, jstring fileName)
{
    if ( !fileName )
        return -1;
    char* file_name = (char*)(*env)->GetStringUTFChars(env, fileName, 0);
    int ret = lock_file(file_name);
    (*env)->ReleaseStringUTFChars(env, fileName, file_name);
    return ret;
}
