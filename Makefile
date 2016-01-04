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

compile:
	$(COMPILE_CMD)

test:
	$(COMPILE_CMD)
	java -esa -cp $(CMP_DIR) TestBackgroundQueueProcessor

javadoc:
	javadoc -d ${DOCS_DIR} -linkoffline $(JAVA_API) $(JAVA_API)  $(SRC_DIR)/*.java
	git add $(DOCS_DIR)
	git commit -m "Update documentation"
	git subtree push --prefix $(DOCS_DIR) origin $(DOCS_BRANCH)
