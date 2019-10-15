package com.hmzhou.language.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        HJJSONMap hjjsonMap = new HJJSONMap("./language-common/assists/language-map.json");
        HJPOI hjpoi = new HJPOI(hjjsonMap.getExcelPath());
        List<String> excelHead = hjpoi.readRow(0, 0, 1, hjpoi.getMaxColumnCount(0, 0));


        List<String> columnDatas = hjpoi.readColumn(0, 0, 0, 28);
        for (int i = 0; i < columnDatas.size(); i++) {
            System.out.println(i + " => " + columnDatas.get(i));
        }
        System.out.println(hjjsonMap.getExcelPath());

        List<String> codes = hjpoi.readColumn(0, 0, 1, hjpoi.getMaxRowCount(0) + 1);

        for (int i = excelHead.size() - 1; i >= 0; i--) {
            if (excelHead.get(i) == null) {
                excelHead.remove(i);
            } else {
                break;
            }
        }

        for (Map.Entry<String, String> entry : hjjsonMap.getLanguageMap().entrySet()) {
            System.out.println(entry + " => " + entry.getValue());

            int i = 0;
            Iterator<String> iterator = excelHead.iterator();
            while (iterator.hasNext()) {
                i++;
                String languageCode = iterator.next();
                if (entry.getKey().contains(languageCode)) {
                    HJLanguage hjLanguage = new HJLanguage(entry.getValue(), codes, hjpoi.readColumn(0, i + 1, 1, codes.size() + 1));
                    iterator.remove();

                    HJXML.writeXML(hjjsonMap.getResPath(), hjLanguage);
                    break;
                }
            }
        }

        hjpoi.close();
    }
}
