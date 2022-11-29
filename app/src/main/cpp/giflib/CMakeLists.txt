cmake_minimum_required(VERSION 3.18.1)

FILE(
        GLOB
        GIF_SRC_LIST
        "*.c"
)

add_library( # Sets the name of the library.
        GIF

        # Sets the library as a shared library.
        STATIC

        # Provides a relative path to your source file(s).
        ${GIF_SRC_LIST})

target_include_directories(
        ${PROJECT_NAME}
        PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}
)

include_directories(BEFORE SYSTEM ${CMAKE_CURRENT_SOURCE_DIR})

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries( # Specifies the target library.
        GIF

        #jnigraphics
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})