#!/bin/zsh

as -o ../out/main.o ../out/main.asm &&
ld -macosx_version_min 12.0.0 -o \
            ../out/main \
            ../out/main.o \
            -lSystem \
            -syslibroot \
            `xcrun -sdk macosx --show-sdk-path` \
            -arch \
            arm64 &&
../out/main
