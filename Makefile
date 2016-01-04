# Rushy Panchal
# Makefile for java BackgroundQueueProcessor

# The source directory of code
SRC_DIR=src

# Output directory of compiled code
CMP_DIR=build

# Documentation directory
DOCS_DIR=docs
JAVA_API="http://docs.oracle.com/javase/8/docs/api/"
DOCS_BRANCH=gh-pages

# Internal parameters
COMPILE_FLAGS:=-sourcepath $(SRC_DIR) -d $(CMP_DIR) -s $(CMP_DIR) \
	-h $(CMP_DIR)
COMPILE_CMD:=javac $(COMPILE_FLAGS) $(SRC_DIR)/*.java
CURRENT_GIT_BRANCH:=$(shell echo `git symbolic-ref --short HEAD`)

compile:
	$(COMPILE_CMD)

test:
	$(COMPILE_CMD)
	java -esa -cp $(CMP_DIR) TestBackgroundQueueProcessor

javadoc:
	javadoc -d ${DOCS_DIR} -linkoffline $(JAVA_API) $(JAVA_API)  $(SRC_DIR)/*.java
	git checkout $(DOCS_BRANCH)
	git merge $(CURRENT_GIT_BRANCH)
	git add docs
	git commit -m "Automatically update Javadoc documentation"
	git checkout $(CURRENT_GIT_BRANCH)
