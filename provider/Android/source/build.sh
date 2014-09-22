rm -rf ./build

ant -f antfile.xml

cp -rfv build/strap-tizen-SDK-full.jar ../strap-tizen-SDK-full.jar

cp -rfv build/strap-tizen-SDK-light.jar ../strap-tizen-SDK-light.jar

