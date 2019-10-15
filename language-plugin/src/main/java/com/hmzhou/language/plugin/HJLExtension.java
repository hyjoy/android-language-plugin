package com.hmzhou.language.plugin;

public class HJLExtension {
    String languagemap;

    HJLExtension languagemap(String path) {
        this.languagemap = path;
        return this;
    }
}
