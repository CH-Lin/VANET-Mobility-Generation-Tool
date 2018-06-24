/*
Copyright (c) 2010 Che-Hung Lin

This file is part of VANET Mobility Generation Tool.

VANET Mobility Generation Tool is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or (at your option)
any later version.

VANET Mobility Generation Tool is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details.

You should have received a copy of the GNU General Public License along with
VANET Mobility Generation Tool. If not, see <https://www.gnu.org/licenses/>.
*/
package vanet.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WebFileDownload {

	public final static int overwrite = 0;
	public final static int rename = 1;
	public final static int skip = 2;

	public void DLFile(String link, String localFile, int action) throws MalformedURLException, IOException {

		switch (action) {
		case overwrite:
			File f = new File(localFile);
			if (f.exists())
				f.delete();
			break;
		case rename:
			File f0 = new File(localFile);
			if (f0.exists())
				for (int i = 1; i < Integer.MAX_VALUE; i++) {
					String p1 = localFile.substring(0, localFile.lastIndexOf('.'));
					String p2 = localFile.substring(localFile.lastIndexOf('.'));
					File f1 = new File(p1 + "(" + i + ")" + p2);
					if (!f1.exists()) {
						localFile = f1.getAbsolutePath();
						break;
					}
				}
			break;
		case skip:
			return;
		}

		URLConnection urlc;

		urlc = new URL(link).openConnection();

		BufferedInputStream bir = new BufferedInputStream(urlc.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(localFile));

		byte[] buf = new byte[1024];
		int read = 0;

		while ((read = bir.read(buf, 0, buf.length)) != -1) {
			bos.write(buf, 0, read);
			bos.flush();
		}

		bir.close();
		bos.close();
	}
}
