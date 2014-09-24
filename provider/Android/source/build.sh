rm -rf ./build

ant -f antfile.xml

cp -rfv build/strap-tizen-SDK-bundle.jar ../strap-tizen-SDK-bundle.jar

cp -rfv build/strap-tizen-SDK-core.jar ../strap-tizen-SDK-core.jar

