cmake_minimum_required(VERSION 3.18.1)

FILE(
        GLOB
        IMAGEUTIL_SRC_LIST
        "imageio_util.c"
)

add_library(
        imageio_util
        STATIC
        ${IMAGEUTIL_SRC_LIST}
)

target_link_libraries( # Specifies the target library.
        imageio_util

        # Links the target library to the log library
        # included in the NDK.
        cpufeatures
        log)