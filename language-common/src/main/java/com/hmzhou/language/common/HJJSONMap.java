package com.hmzhou.language.common;

import com.hmzhou.language.common.utils.CloseUtils;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HJJSONMap {

    private JSONObject mJSONObject;

    public HJJSONMap(String filePath) throws Exception {
        mJSONObject = init(filePath);
    }

    private JSONObject init(String filePath) throws Exception {
        String path = filePath;
        if (!path.endsWith(".json")) {
            path += ".json";
        }
        File file = new File(path);

        FileInputStream fileInputStream = null;
        StringBuilder sb = new StringBuilder();
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];

            while (fileInputStream.read(buffer) != -1) {
                sb.append(new String(buffer, StandardCharsets.UTF_8));
            }
        } finally {
            CloseUtils.closeIO(fileInputStream);
        }

        if (sb.length() == 0) {
            throw new IllegalArgumentException("配置文件为空，文件路径： " + filePath);
        }
        return new JSONObject(sb.toString());
    }

    public String getExcelPath() {
        return mJSONObject.getString("filename");
    }

    public Map<String, String> getLanguageMap() {
        JSONObject jsonObject = mJSONObject.getJSONObject("language-map");
        Iterator<String> iterator = jsonObject.keys();
        Map<String, String> map = new HashMap<>();
        while (iterator.hasNext()) {
            String key = iterator.next();
            map.put(key, jsonObject.getString(key));
        }
        return map;
    }

    public String getResPath() {
        return mJSONObject.getString("res-path");
    }
}
