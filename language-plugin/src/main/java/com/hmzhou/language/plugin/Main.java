package com.hmzhou.language.plugin;

import com.hmzhou.language.common.HJJSONMap;
import com.hmzhou.language.common.HJLanguage;
import com.hmzhou.language.common.HJPOI;
import com.hmzhou.language.common.HJXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {

//    public static void main(String[] args) throws Exception {
//        execute("./language-common/assists/language-map.json");
//    }

    public static void execute(String filePath) throws Exception {
        final HJJSONMap hjjsonMap = new HJJSONMap(filePath);
        HJPOI hjpoi = new HJPOI(hjjsonMap.getExcelPath());
        List<String> excelHead = hjpoi.readRow(0, 0, 1, hjpoi.getMaxColumnCount(0, 0));

        List<String> codes = hjpoi.readColumn(0, 0, 1, hjpoi.getMaxRowCount(0) + 1);

        for (int i = excelHead.size() - 1; i >= 0; i--) {
            if (excelHead.get(i) == null) {
                excelHead.remove(i);
            } else {
                break;
            }
        }

        List<HJLanguage> tasks = new ArrayList<>();

        for (Map.Entry<String, String> entry : hjjsonMap.getLanguageMap().entrySet()) {

            int i = 0;
            Iterator<String> iterator = excelHead.iterator();
            while (iterator.hasNext()) {
                i++;
                String languageCode = iterator.next();
                if (entry.getKey().contains(languageCode)) {
                    HJLanguage hjLanguage = new HJLanguage(entry.getValue(), codes, hjpoi.readColumn(0, i + 1, 1, codes.size() + 1));
                    tasks.add(hjLanguage);
                    iterator.remove();
                    break;
                }
            }
        }

        hjpoi.close();

//        ExecutorService executors = Executors.newFixedThreadPool(tasks.size() > 5 ? tasks.size() / 3 : 1);

        for (final HJLanguage hjLanguage : tasks) {
//            executors.execute(new Runnable() {
//                @Override
//                public void run() {
            try {
                HJXML.writeXML(hjjsonMap.getResPath(), hjLanguage);
            } catch (IOException e) {
                // do nothing
            }
//                }
//            });
        }
    }
}
