package org.myan.web.mvc.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by myan on 2017/8/17.
 * Intellij IDEA
 */
public final class FileUploadUtil {

    public static String getRealFileName(String fileName) {
        return FilenameUtils.getName(fileName);
    }

    public static File createFile(String filePath) throws IOException {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists())
            FileUtils.forceMkdir(parentDir);

        return file;
    }
}
