package com.oddlabs.matchservlet;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

final strictfp class ServletUtil {
	static void writeByteArray(DataOutput out, byte[] array) throws IOException {
		out.writeInt(array.length);
		out.write(array);
	}

	static OutputStream createOutput(final HttpServletResponse res) {
		final ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
		return new FilterOutputStream(byte_out) {
			public final void close() throws IOException {
				super.close();
				res.setContentType("application/octet-stream");
				res.setContentLength(byte_out.size());
				OutputStream res_out = res.getOutputStream();
				try {
					byte_out.writeTo(res_out);
				} finally {
					res_out.close();
				}
			}
		};
	}
}
