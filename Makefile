all: caf-build macwidgets mdk

.PHONY: caf-build

caf:
	git clone https://github.com/johnmay/caf.git

caf-build: caf
	cd caf && mvn install && cd ..

mac_widgets-0.10.0.zip:
	curl https://storage.googleapis.com/google-code-archive-downloads/v2/code.google.com/macwidgets/mac_widgets-0.10.0.zip > mac_widgets-0.10.0.zip 

mac_widgets-0.10.0: mac_widgets-0.10.0.zip
	unzip mac_widgets-0.10.0.zip

macwidgets: mac_widgets-0.10.0
	mvn install:install-file -DartifactId=macwidgets -DgroupId=com.explodingpixels -Dversion=0.10.0 -DgeneratePom=true -Dpackaging=jar -Dfile=mac_widgets-0.10.0/MacWidgets.jar 

mdk: caf-build macwidgets
	mvn install -DskipTests