
cmake_minimum_required(VERSION 3.10.2)
project("native-lib")
set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/opencv-sdk/sdk/native/jni")
find_package(OpenCV REQUIRED)
add_library(native-lib SHARED src/main/cpp/native-lib.cpp)
include_directories(${OpenCV_INCLUDE_DIRS})
target_link_libraries(native-lib ${OpenCV_LIBS})
