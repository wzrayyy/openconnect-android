# OpenConnect for Android

This version is still **UNDER DEVELOPMENT**. Check [the official repository](https://gitlab.com/openconnect/ics-openconnect) for the legacy (but working on latest Android) version.


## Building

```sh
# clone the repo with submodules
git clone --recurse-submodules https://github.com/wzrayyy/openconnect-android
cd openconnect-android
# make external dependencies (curl and openconnect)
make -C external
# build apk
./gradlew assembleDebug
# copy the apk to the current folder
find -name '*.apk' | head -1 | xargs -I{} cp {} OpenConnect.apk
# install
adb install OpenConnect.apk
```
