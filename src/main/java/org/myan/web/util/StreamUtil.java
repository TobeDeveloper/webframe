package org.myan.web.util;

import javax.servlet.ServletInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by myan on 2017/8/11.
 * Intellij IDEA
 */
public final class StreamUtil {

    public static String getString(ServletInputStream input) {
        StringBuilder sb = new StringBuilder();

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(input))
        ) {
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        int len;
        byte[] buffer = new byte[4 * 1024];
        while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }
    }
}
