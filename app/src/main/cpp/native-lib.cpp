
#include <jni.h>
#include <opencv2/opencv.hpp>
extern "C"
JNIEXPORT void JNICALL
Java_com_example_edgedetect_MainActivity_nativeProcessFrame(JNIEnv*, jobject) {
    cv::Mat img(200,200,CV_8UC1, cv::Scalar(0));
    cv::Canny(img,img,50,150);
}
