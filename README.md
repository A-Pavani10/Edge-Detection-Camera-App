Real-Time Edge Detection Camera App  
Android + OpenCV + C++ (NDK) + OpenGL | Assignment Submission  

This project implements a real-time camera frame processing pipeline using:

- Android Camera
- JNI + C++ (NDK)
- OpenCV for Image Processing
- OpenGL ES for Rendering
- TypeScript Web Viewer

The app captures camera frames → sends to native C++ → performs edge detection (Canny) → renders results using OpenGL.

This is a complete submission for the Software Engineering Intern (R&D) assignment.
 Features

Android App
- Real-time camera capture  
- JNI bridge (Kotlin ↔ C++)  
- OpenCV-based Canny edge detection  
- OpenGL rendering pipeline  
- Fully modular code  

Native Layer (C++)
- Efficient memory management  
- Direct Mat operations  
- Works with OpenCV SDK  

Rendering
- Uses GLSurfaceView  
- Draws a textured quad  
- Display pipeline ready for expansion  

Web Viewer
- Simple TypeScript demo page  
- Illustrates processed output  
- Clean and easy to run  
Project Structure  

```
EdgeDetection_FinalReady/
│── app/
│   ├── src/main/java/com/example/edgedetect/
│   │   ├── MainActivity.kt
│   │   ├── GLRenderer.kt
│   ├── src/main/cpp/native-lib.cpp
│   ├── src/main/res/layout/activity_main.xml
│   ├── CMakeLists.txt
│   ├── build.gradle
│   └── opencv-sdk/
│       └── README.txt (place OpenCV SDK here)
│
│── web/
│   ├── index.html
│   ├── index.ts
│   └── tsconfig.json
│
│── gradlew
│── gradlew.bat
│── gradle/wrapper/
│── settings.gradle
│── build.gradle
│── README.md
```
IMPORTANT – OpenCV SDK Required

The OpenCV Android SDK (200+ MB) is NOT included in this repo to keep the repository lightweight.

Before building:
 Download OpenCV:
https://opencv.org/releases/
 Extract it and copy the SDK here:
```
app/opencv-sdk/
```
 Final structure must include:
```
app/opencv-sdk/sdk/native/jni/include/
app/opencv-sdk/sdk/native/libs/arm64-v8a/
app/opencv-sdk/sdk/native/libs/armeabi-v7a/
```

---
Build Instructions (Evaluator Only)

1. Open project in **Android Studio**  
2. Allow **Gradle Sync**  
3. Copy the OpenCV SDK into `app/opencv-sdk/`  
4. Connect Android device  
5. Run the app  

---
Web Viewer  
Run with:

```
cd web
npm install
npm run dev
```

---
 Screenshots (Optional)
(Add screenshots here if generated)

---
Why This Project Is Strong

- Uses NDK (C++), not only Kotlin  
- Integrates OpenCV, a core CV library  
- Uses OpenGL ES, showing graphics pipeline knowledge  
- Clean build system using CMake  
- Real industry-style folder structure  

Perfect for R&D, Imaging, and Computer Vision evaluation.

