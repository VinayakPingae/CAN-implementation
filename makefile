#
# A makefile for compiling Sample Information Retrieval Posting java classes
#

# define a makefile variable for the java compiler
#

JC = javac

# define a makefile variable for compilation flags
# the -g flag compiles with debugging information
#

JFLAGS = -g


JVM= java
FILE=
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java
CLASSES = \
	BootStrapNodeDriver.java \
	BootStrapNodeImplementation.java \
	BootStrapNodeSupport.java \
	LoggerDebug.java \
	Neighbour.java \
	PacketInformation.java \
	PeerInformation.java \
	PeerNode.java \
	PeerNodeDriver.java \
	PeerNodeImplementation.java \
	PeerNodeStarterThread.java \
	PeerNodeSupport.java \
	Property.java \
	ReadWriteObject.java  
	
    
MAIN = Main

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
	$(JVM) $(MAIN)


# To start over from scratch, type 'make clean'.
# Removes all .class files, so that the next make rebuilds them
#

clean:
	$(RM) *.class
