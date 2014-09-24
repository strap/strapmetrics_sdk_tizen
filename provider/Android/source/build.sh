rm -rf ./build

ant -f antfile.xml

version=`cat VERSION`

cp -rfv build/strap-tizen-SDK-bundle.jar ../strap-tizen-SDK-bundle-$version.jar

cp -rfv build/strap-tizen-SDK-core.jar ../strap-tizen-SDK-core-$version.jar

