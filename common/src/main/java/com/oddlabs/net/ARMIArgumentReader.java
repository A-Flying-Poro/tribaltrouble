package com.oddlabs.net;

import java.io.IOException;

public strictfp interface ARMIArgumentReader {
	Object readArgument(Class type, ByteBufferInputStream in) throws IOException, ClassNotFoundException;
}
