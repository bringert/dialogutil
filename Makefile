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

# path to a OAA installation
OAA_HOME = ../oaa2.3.0

# the host where the OAA facilitator is running
FAC_HOST = $(HOSTNAME)

# path to Java Speech API classes
JSAPI_CP=../freetts-1.2beta2/lib/jsapi.jar

# path to Java Speech Engine classes
JSAPI_ENGINE_CP=../freetts-1.2beta2/lib/freetts.jar

###################################################
###################################################

JAVACFLAGS=-Xlint:deprecation -Xlint:unchecked 

OAA_JARS = $(OAA_HOME)/lib/jar
OAA_CP = $(OAA_JARS)/oaa2.jar:$(OAA_JARS)/antlr-oaa.jar:$(OAA_JARS)/concurrent-1.3.1.jar:$(OAA_JARS)/log4j-1.2.7.jar

CLASSPATH=.:build:$(OAA_CP):$(JSAPI_CP):$(JSAPI_ENGINE_CP)

SRC=src/se/chalmers/cs/gf/dialogutil/*.java \
    src/se/chalmers/cs/gf/dialogutil/sr/*.java \
    src/se/chalmers/cs/gf/dialogutil/tts/*.java

.PHONY: clean distclean classes javadoc

default all: classes

classes:
	mkdir -p build
	$(JAVAC) $(JAVACFLAGS) -cp $(CLASSPATH) -d build $(SRC)

recognize:
	$(JAVA) -cp $(CLASSPATH) se.chalmers.cs.gf.dialogutil.sr.Recognizer -oaa_connect "tcp('$(FAC_HOST)',3378)"

javadoc:
	mkdir -p doc
	$(JAVADOC) -d doc $(SRC)

clean:
	-rm -rf build

distclean: clean
