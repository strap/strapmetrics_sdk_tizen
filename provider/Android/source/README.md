##Build Instructions##

You need the following installed in your machine:

1. JDK `1.7+`
2. JavaDoc
3. Ant


To build via `ant` use the provided `antfile.xml`

Run the following commands (In Directory: `{baseDir}/provider/Android/source`):

```bash
> ant -f antfile.xml
```

We have a unified helper script. Its recommended that you run that as it automatically copies required jar files to required places.

```bash
> ./build.sh
```