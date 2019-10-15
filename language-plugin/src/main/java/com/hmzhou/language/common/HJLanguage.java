package com.hmzhou.language.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class HJLanguage {

    private String languageCode;
    private List<String> codes;
    private List<String> languages;

    public HJLanguage(String languageCode, List<String> codes, List<String> languages) {
        this.languageCode = languageCode;
        this.codes = codes;
        this.languages = languages;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public List<String> getCodes() {
        return codes;
    }

    public List<String> getLanguages() {
        return languages;
    }

    /**
     * 转换，主要做容错处理
     *
     * @return :
     */
    public Map<String, String> getCodeValues() {
        Map<String, String> datas = new HashMap<>();
        int min = Math.min(codes.size(), languages.size());
        int count = codes.size() > languages.size() ? min : codes.size();
        for (int i = 0; i < count; i++) {
            datas.put(codes.get(i), languages.get(i));
        }
        for (int i = count + 1; i < codes.size(); i++) {
            datas.put(codes.get(i), null);
        }
        return datas;
    }
}
