SHELL := /bin/bash

runAll:
	@echo 'Pulling mongo image'
	@docker pull mongo:latest
	@echo 'Running mongo image'
	@docker run -p 27017:27017 -d mongo:latest
	@echo 'BUILDING APPLICATION'
	@mvn clean package -DskipTests
	@java -jar ./target/*.jar

runApplication:
	@echo 'BUILDING APPLICATION'
	@mvn clean package -DskipTests
	@java -jar ./target/*.jar

runDB:
	@echo 'Pulling mongo image'
	@docker pull mongo:latest
	@echo 'Running mongo image'
	@docker run -p 27017:27017 -d mongo:latest