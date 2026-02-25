.PHONY: test assemble-debug

test:
	./gradlew :app:testDebugUnitTest

assemble-debug:
	./gradlew :app:assembleDebug
