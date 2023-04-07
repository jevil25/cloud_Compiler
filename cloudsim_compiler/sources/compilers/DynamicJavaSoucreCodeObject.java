package compilers;

import java.net.URI;

import javax.tools.SimpleJavaFileObject;

class DynamicJavaSourceCodeObject extends SimpleJavaFileObject {
    private String code;

    public DynamicJavaSourceCodeObject(String name, String code) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }
}