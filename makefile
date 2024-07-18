# Variabel
MVN=mvn
JAR_FILE=target/roemah-duren-api-0.0.1-SNAPSHOT.jar
SPRING_PROFILES=prod
DATABASE_USERNAME=root
DATABASE_PASSWORD=password

# Deteksi sistem operasi
UNAME_S := $(shell uname -s)

ifeq ($(UNAME_S),Linux)
	ENV_CMD = DATABASE_USERNAME=$(DATABASE_USERNAME) DATABASE_PASSWORD=$(DATABASE_PASSWORD) DB_HOST=localhost DB_PORT=3306 DB_NAME=roemah_duren_db JWT_SECRET=c2VjcmV0 JWT_EXPIRATION_IN_SECOND=31536000 EMAIL_ADMIN=admin@roemahduren.com PASSWORD_ADMIN=password EMAIL_OWNER=diaz.subagja@roemahduren.com PASSWORD_OWNER=password
	RUN_CMD = DATABASE_USERNAME=$(DATABASE_USERNAME) DATABASE_PASSWORD=$(DATABASE_PASSWORD) DB_HOST=localhost DB_PORT=3306 DB_NAME=roemah_duren_db JWT_SECRET=c2VjcmV0 JWT_EXPIRATION_IN_SECOND=31536000 EMAIL_ADMIN=admin@roemahduren.com PASSWORD_ADMIN=password EMAIL_OWNER=diaz.subagja@roemahduren.com PASSWORD_OWNER=password
else ifeq ($(UNAME_S),Darwin) # macOS
	ENV_CMD = DATABASE_USERNAME=$(DATABASE_USERNAME) DATABASE_PASSWORD=$(DATABASE_PASSWORD) DB_HOST=localhost DB_PORT=3306 DB_NAME=roemah_duren_db JWT_SECRET=c2VjcmV0 JWT_EXPIRATION_IN_SECOND=31536000 EMAIL_ADMIN=admin@roemahduren.com PASSWORD_ADMIN=password EMAIL_OWNER=diaz.subagja@roemahduren.com PASSWORD_OWNER=password
	RUN_CMD = DATABASE_USERNAME=$(DATABASE_USERNAME) DATABASE_PASSWORD=$(DATABASE_PASSWORD) DB_HOST=localhost DB_PORT=3306 DB_NAME=roemah_duren_db JWT_SECRET=c2VjcmV0 JWT_EXPIRATION_IN_SECOND=31536000 EMAIL_ADMIN=admin@roemahduren.com PASSWORD_ADMIN=password EMAIL_OWNER=diaz.subagja@roemahduren.com PASSWORD_OWNER=password
else ifeq ($(OS),Windows_NT)
	ENV_CMD = set DATABASE_USERNAME=$(DATABASE_USERNAME) && set DATABASE_PASSWORD=$(DATABASE_PASSWORD) && set DB_HOST=localhost && set DB_PORT=3306 && set DB_NAME=roemah_duren_db && set JWT_SECRET=c2VjcmV0 && set JWT_EXPIRATION_IN_SECOND=31536000 && set EMAIL_ADMIN=admin@roemahduren.com set && PASSWORD_ADMIN=password && set EMAIL_OWNER=diaz.subagja@roemahduren.com && set PASSWORD_OWNER=password
	RUN_CMD = set DATABASE_USERNAME=$(DATABASE_USERNAME) && set DATABASE_PASSWORD=$(DATABASE_PASSWORD) && set DB_HOST=localhost && set DB_PORT=3306 && set DB_NAME=roemah_duren_db && set JWT_SECRET=c2VjcmV0 && set JWT_EXPIRATION_IN_SECOND=31536000 && set EMAIL_ADMIN=admin@roemahduren.com set && PASSWORD_ADMIN=password && set EMAIL_OWNER=diaz.subagja@roemahduren.com && set PASSWORD_OWNER=password
endif

.PHONY: all
all: deploy

.PHONY: build
build:
	@echo "Building the project with Maven..."
	$(ENV_CMD) $(MVN) clean install -DskipTests

.PHONY: run
run:
	@echo "Running the application..."
	$(RUN_CMD) java -jar $(JAR_FILE) --spring.profiles.active=$(SPRING_PROFILES)

.PHONY: clean
clean:
	@echo "Cleaning the project..."
	$(MVN) clean

.PHONY: deploy
deploy: build
	@echo "Running the application..."
	$(RUN_CMD) java -jar $(JAR_FILE) --spring.profiles.active=$(SPRING_PROFILES)
