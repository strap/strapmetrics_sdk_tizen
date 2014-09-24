rm -rf ./build

ant -f antfile.xml

version=`cat VERSION`

rm -rfv ../*.jar

cp -rfv build/strap-tizen-SDK-bundle.jar ../strap-tizen-SDK-$version-bundle.jar

cp -rfv build/strap-tizen-SDK-core.jar ../strap-tizen-SDK-$version-core.jar

