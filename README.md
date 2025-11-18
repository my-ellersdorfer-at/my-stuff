# my-stuff
Example product for the book "The Effective Software Engineer"

# build
```
mvn -B clean verify jacoco:report --file pom.xml
```

# check for dependency updates
```
mvn versions:display-dependency-updates
```

# update dependencies
```
mvn versions:update-properties -DgenerateBackupPoms=false versions:use-releases
```
