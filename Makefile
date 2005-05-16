###################################################
###################################################
# You may need to change some of these variables

# path to the gf executable
GF    = gf

# path to the Java compiler
JAVAC = javac

# path to the Java VM
JAVA  = java

# path to the Java archive tool
JAR   = jar

# path to the javadoc tool
JAVADOC = javadoc

# the host where the OAA facilitator is running
FAC_HOST = $(HOSTNAME)

# path to the base of a FreeTTS install 
# (only used to define JSAPI_CP and
# JSAPI_ENGINE_CP)
FREETTS=../freetts-1.2

# path to Java Speech API classes
JSAPI_CP=$(FREETTS)/lib/jsapi.jar

# path to Java Speech Engine classes
JSAPI_ENGINE_CP=$(FREETTS)/lib/freetts.jar

###################################################
###################################################

JAVACFLAGS=-Xlint:deprecation -Xlint:unchecked 

OAA_JARS = $(OAA_HOME)/lib/jar
OAA_CP = $(OAA_JARS)/oaa2.jar:$(OAA_JARS)/antlr-oaa.jar:$(OAA_JARS)/concurrent-1.3.1.jar:$(OAA_JARS)/log4j-1.2.7.jar

CLASSPATH=.:build:$(OAA_CP):$(JSAPI_CP):$(JSAPI_ENGINE_CP)

SRC=src/se/chalmers/cs/gf/dialogutil/*.java \
    src/se/chalmers/cs/gf/dialogutil/gui/*.java \
    src/se/chalmers/cs/gf/dialogutil/sr/*.java \
    src/se/chalmers/cs/gf/dialogutil/tts/*.java

DIST_NAME=dialogutil-`date '+%Y%m%d'`
DIST_FILE=$(DIST_NAME).tar.gz

.PHONY: clean distclean classes javadoc

default all: jar

jar: classes
	cd build; $(JAR) -cf ../dialogutil.jar *

classes:
	mkdir -p build
	$(JAVAC) $(JAVACFLAGS) -cp $(CLASSPATH) -d build $(SRC)

recognize:
	$(JAVA) -cp $(CLASSPATH) se.chalmers.cs.gf.dialogutil.sr.Recognizer -oaa_connect "tcp('$(FAC_HOST)',3378)"

nuance-speak:
	$(JAVA) -cp $(CLASSPATH) se.chalmers.cs.gf.dialogutil.tts.NuanceWrapperSpeechOutput -oaa_connect "tcp('$(FAC_HOST)',3378)"

javadoc:
	mkdir -p doc/javadoc
	$(JAVADOC) -d doc/javadoc $(SRC)

dist:
	darcs dist --dist-name=$(DIST_NAME)

upload: jar dist
	scp dialogutil.jar cs:.public_html/gf/downloads/dialogutil/jar/
	scp $(DIST_FILE) cs:.public_html/gf/downloads/dialogutil/

upload-doc: javadoc
	scp doc/dialogutil.html cs:.public_html/gf/
	scp -pr doc/javadoc/* cs:.public_html/gf/javadoc/dialogutil

clean:
	-rm -rf build

distclean: clean
