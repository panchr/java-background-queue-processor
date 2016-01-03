# Rushy Panchal
# Makefile for java BackgroundQueueProcessor

# The source directory of code
SRC_DIR=src

# Output directory of compiled code
CMP_DIR=build

# Internal parameters
COMPILE_FLAGS=-sourcepath $(SRC_DIR) -d $(CMP_DIR) -s $(CMP_DIR) \
	-h $(CMP_DIR)
COMPILE_CMD=javac $(COMPILE_FLAGS) $(SRC_DIR)/*.java

compile:
	$(COMPILE_CMD)

test:
	$(COMPILE_CMD)
	java -esa -cp $(CMP_DIR) TestBackgroundQueueProcessor
