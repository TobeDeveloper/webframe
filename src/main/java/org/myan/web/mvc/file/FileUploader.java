package org.myan.web.mvc.file;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.myan.web.ConfigHelper;
import org.myan.web.exceptions.FileWriteException;
import org.myan.web.mvc.FileParam;
import org.myan.web.mvc.FormParam;
import org.myan.web.mvc.Param;
import org.myan.web.util.CollectionUtil;
import org.myan.web.util.StreamUtil;
import org.myan.web.util.StringUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by myan on 2017/8/17.
 * Intellij IDEA
 */
public final class FileUploader {

    private static ServletFileUpload fileUpload;

    public static void init(ServletContext servletContext) {
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        fileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,
                repository));
        int uploadLimit = ConfigHelper.getUploadLimit();
        if (uploadLimit > 0)
            fileUpload.setFileSizeMax(uploadLimit * 1024 * 1024);

    }

    public static boolean isMultipart(HttpServletRequest req) {
        return ServletFileUpload.isMultipartContent(req);
    }

    public static Param createParam(HttpServletRequest req) {
        List<FormParam> formParams = new ArrayList<>();
        List<FileParam> fileParams = new ArrayList<>();

        try {
            Map<String, List<FileItem>> fileItems = fileUpload.parseParameterMap(req);
            if (CollectionUtil.isNotEmpty(fileItems)) {
                for (Map.Entry<String, List<FileItem>> entry : fileItems.entrySet()) {
                    String fieldName = entry.getKey();
                    List<FileItem> fileItemList = entry.getValue();
                    if (CollectionUtil.isNotEmpty(fileItemList)) {
                        for (FileItem fileItem : fileItemList) {
                            if (fileItem.isFormField()) {
                                String fieldValue = fileItem.getString("UTF-8");
                                formParams.add(new FormParam(fieldName, fieldValue));
                            } else {
                                String fileName = FileUploadUtil.getRealFileName(new String(fileItem.getName().getBytes(),
                                        "UTF-8"));
                                if (StringUtil.isNotEmpty(fileName)) {
                                    fileParams.add(new FileParam(fieldName,
                                            fileName,
                                            fileItem.getSize(),
                                            fileItem.getContentType(),
                                            fileItem.getInputStream()));
                                }
                            }
                        }
                    }

                }
            }
        } catch (FileUploadException | IOException e) {
            throw new FileWriteException(e);
        }
        return new Param(formParams, fileParams);
    }

    public static void uploadFile(String path, FileParam param) {
        String filePath = path + param.getFileName();
        try (
                InputStream inputStream = new BufferedInputStream(param.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
        ) {
            if (param != null) {
                FileUploadUtil.createFile(filePath);
                StreamUtil.copyStream(inputStream, outputStream);
            }
        } catch (IOException e) {
            throw new FileWriteException(e);
        }
    }

    public static void uploadFile(String path, List<FileParam> params) {
        if (CollectionUtil.isNotEmpty(params)) {
            for (FileParam param : params) {
                uploadFile(path, param);
            }
        }
    }
}
