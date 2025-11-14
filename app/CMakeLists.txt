cmake_minimum_required(VERSION 3.10.2)
project("native-lib")

# Point to OpenCV SDK inside app/opencv-sdk
set(OpenCV_DIR "${CMAKE_SOURCE_DIR}/opencv-sdk/sdk/native/jni")
find_package(OpenCV REQUIRED)
message(STATUS "OpenCV include: ${OpenCV_INCLUDE_DIRS}")
message(STATUS "OpenCV libs: ${OpenCV_LIBS}")

add_library(native-lib SHARED src/main/cpp/native-lib.cpp)

include_directories(${OpenCV_INCLUDE_DIRS})

find_library(log-lib log)

target_link_libraries(native-lib ${OpenCV_LIBS} ${log-lib})
