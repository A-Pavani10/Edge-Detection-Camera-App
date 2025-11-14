#include <jni.h>
#include <opencv2/opencv.hpp>

using namespace cv;

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_example_edgedetect_MainActivity_nativeProcessFrame(JNIEnv *env, jobject /* this */, jbyteArray nv21_, jint width, jint height) {
    jsize len = env->GetArrayLength(nv21_);
    jbyte* nv21 = env->GetByteArrayElements(nv21_, NULL);

    // Create Mat from NV21 data: height + height/2 rows, single channel
    Mat yuv(height + height/2, width, CV_8UC1, (unsigned char *)nv21);
    Mat bgr;
    cvtColor(yuv, bgr, COLOR_YUV2BGR_NV21);

    Mat gray;
    cvtColor(bgr, gray, COLOR_BGR2GRAY);

    Mat edges;
    Canny(gray, edges, 50, 150);

    Mat rgba;
    cvtColor(edges, rgba, COLOR_GRAY2RGBA);

    int outSize = rgba.total() * rgba.elemSize();
    jbyteArray out = env->NewByteArray(outSize);
    env->SetByteArrayRegion(out, 0, outSize, (jbyte*)rgba.data);

    env->ReleaseByteArrayElements(nv21_, nv21, 0);
    return out;
}
