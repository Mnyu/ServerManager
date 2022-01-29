# Getting Started

### Step 1: Clone the repository and build:

```shell
git clone git@github.com:Mnyu/ServerManager.git
cd servermanager
mvn clean install -Pfe -DskipTests
```

### Step 2: Build docker images:
```shell
bin/docker-build-all.sh
```

### Step 3: Start application:
Run with Docker Compose
```shell
bin/docker-compose-run.sh
```

### Step 4: Open and play with the application:
Visit the url : http://localhost:4200

Sample screenshot:

![Image](https://github.com/Mnyu/ServerManager/blob/main/docs/screenshots/app-screenshot.png)


### Step 5: Start application:
Run with Docker Compose
```shell
bin/docker-compose-run.sh down -v
```