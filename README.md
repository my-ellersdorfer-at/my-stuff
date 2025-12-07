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

# certs
Generate certs manually if needed:
```bash
mkdir -p certs && openssl req -x509 -newkey rsa:2048 -keyout certs/key.pem -out certs/cert.pem -days 365 -nodes -subj "/CN=localhost"
```

# install ng-frontend
```
 ./ng-frontend: ng new my-stuff --directory . --package-manager=pnpm
```