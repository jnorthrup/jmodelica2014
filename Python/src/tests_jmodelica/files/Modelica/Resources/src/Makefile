PLATFORM=win32

ifeq (win, $(findstring win,$(PLATFORM)))
SUFF=dll
PREF=
SHARED_FLAGS=-shared -Wl,-soname,
else ifeq (darwin, $(findstring darwin,$(PLATFORM)))
SUFF=dylib
PREF=lib
SHARED_FLAGS=-dynamiclib -Wl,-install_name,@rpath/
else
SUFF=so
PREF=lib
SHARED_FLAGS=-shared -Wl,-soname,
endif

CC=gcc

DIR=../Library/$(PLATFORM)
INC=../Include

all : add_shared add_static array_shared array_static

add_shared: $(PREF)addNumbers.$(SUFF) lib_dir
	mv $< $(DIR)

add_static: libaddNumbers.a lib_dir
	mv $< $(DIR)


array_shared: $(PREF)arrayFunctions.$(SUFF) lib_dir
	mv $< $(DIR)

array_static: libarrayFunctions.a lib_dir
	mv $< $(DIR)


lib_dir:
	mkdir -p $(DIR)

%.o: %.c
	$(CC) -c -fPIC -I$(INC) $< -o $@

$(PREF)%.$(SUFF): %.o
	$(CC) $(SHARED_FLAGS)$@ -o $@ $^

lib%.a: %.o
	$(AR) rcs $@ $<

clean:
	rm -rf $(DIR) *.o

.PHONY: lib_dir all clean add_shared add_static array_shared array_static