# Variables
GRADLEW = ./gradlew

# Default Target
.PHONY: all
all: build

# Compile and package Android App in Debug mode
.PHONY: build
build:
	chmod +x gradlew
	$(GRADLEW) assembleDebug

# Run unit tests
.PHONY: test
test:
	chmod +x gradlew
	$(GRADLEW) test

# Clean build artifacts
.PHONY: clean
clean:
	chmod +x gradlew
	$(GRADLEW) clean
