package com.hmzhou.language.common;

import com.hmzhou.language.common.utils.CloseUtils;
import com.hmzhou.language.common.utils.FileIOUtils;
import com.hmzhou.language.common.utils.FileUtils;
import com.hmzhou.language.common.utils.Prediction;

import org.apache.commons.codec.Charsets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;

public class HJXML {

    private static final String STRING_FILE_NAME = "strings.xml";
    private static final String STRING_FILE_NAME_TMP = "strings.xml.tmp";
    private HJLanguage mHjLanguage;
    private String mPath;

    private HJXML(String path, HJLanguage hjLanguage) {
        Prediction.checkNotNull(path, "请指定对应的res目录");
        File file = new File(path);
        if (!file.exists() || file.isFile() || !file.getName().endsWith("res")) {
            throw new IllegalArgumentException("请指定对应的res目录");
        }
        this.mPath = path;
        this.mHjLanguage = hjLanguage;
    }


    private String buildRow(String code, String value) {
        if (value == null) {
            return "\n";
        }
        return "    <string name=\"" + code + "\">" + value + "</string>\n";
    }

    private void buildXML() throws IOException {

        String filePath = mPath + File.separator + (mHjLanguage.getLanguageCode().length() == 0 ? "values" : "values-" + mHjLanguage.getLanguageCode());
        File xmlFile = new File(filePath, STRING_FILE_NAME);
        FileUtils.createOrExistsFile(xmlFile);
        FileUtils.rename(xmlFile, STRING_FILE_NAME_TMP);

        File readFile = new File(filePath, STRING_FILE_NAME_TMP);
        File writeFile = new File(filePath, STRING_FILE_NAME);
        FileUtils.createOrExistsFile(writeFile);
        BufferedReader reader = null;
        BufferedWriter writer = null;

        String content = FileIOUtils.readFile2String(readFile).trim();
        boolean needHead = false;
        if (!content.startsWith("<resources")) {
            needHead = true;
        }

        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(readFile), Charsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeFile), Charsets.UTF_8));

            if (needHead) {
                writer.write("<resources>\n");
            }
            while ((line = reader.readLine()) != null) {

                if (!line.equals("</resources>")) {
                    writer.write(line + "\n");
                } else {
                    break;
                }
            }

            for (Map.Entry<String, String> entry : mHjLanguage.getCodeValues().entrySet()) {
                String row = buildRow(entry.getKey(), entry.getValue());
                writer.write(row);
            }

            writer.write("</resources>");

            writer.flush();

            FileUtils.delete(readFile);
        } finally {
            CloseUtils.closeIO(writer);
            CloseUtils.closeIO(reader);
        }
    }

    public static void writeXML(String path, HJLanguage hjLanguage) {
        HJXML hjxml = new HJXML(path, hjLanguage);
        try {
            hjxml.buildXML();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
