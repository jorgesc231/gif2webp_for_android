cmake_minimum_required(VERSION 3.18.1)

FILE(
        GLOB
        UTIL_SRC_LIST
        "example_util.c"
)

FILE(
        GLOB
        GIF_DEC_SRC_LIST
        "gifdec.c"
)

add_library(
        example_util
        STATIC
        ${UTIL_SRC_LIST}
)

add_library(
        gifdec
        STATIC
        ${GIF_DEC_SRC_LIST}
)

target_include_directories(
        example_util
        PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}
)

target_include_directories(
        gifdec
        PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}
)

target_include_directories(
        gifdec
        PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}/../../giflib/
)

target_link_libraries( # Specifies the target library.
        example_util
        gifdec

        # Links the target library to the log library
        # included in the NDK.
        cpufeatures
        log)