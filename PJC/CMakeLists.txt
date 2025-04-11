cmake_minimum_required(VERSION 3.30)
project(MonkeyTyper)

set(CMAKE_CXX_STANDARD 20)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

set(BUILD_SHARED_LIBS OFF)

include(FetchContent)

FetchContent_Declare(
        SFML
        GIT_REPOSITORY https://github.com/SFML/SFML
        GIT_TAG 2.6.1
)
FetchContent_MakeAvailable(SFML)

FetchContent_Declare(
        fmt
        GIT_REPOSITORY https://github.com/fmtlib/fmt
        GIT_TAG 11.1.1
)
FetchContent_MakeAvailable(fmt)

add_executable(MonkeyTyper main.cpp
        Scene.h
        MainMenuScene.cpp
        GameplayScene.cpp
        GameplayScene.h
        MainMenuScene.h
        LeaderBoardScene.cpp
        LeaderBoardScene.h
)

target_link_libraries(MonkeyTyper sfml-graphics sfml-audio fmt)