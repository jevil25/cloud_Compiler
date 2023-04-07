package compilers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;

import javax.lang.model.element.NestingKind;
import javax.tools.JavaFileObject;

public class InMemoryJavaFileObject implements JavaFileObject {
    private final String name;
    private final String code;

	public InMemoryJavaFileObject(String name, String code) {
        this.name = name;
        this.code = code;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InputStream openInputStream() {
        return new ByteArrayInputStream(code.getBytes());
    }

    @Override
    public OutputStream openOutputStream() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reader openReader(boolean ignoreEncodingErrors) {
        return new InputStreamReader(openInputStream());
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return code;
    }

    @Override
    public Writer openWriter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getLastModified() {
        return 0;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public Kind getKind() {
        return Kind.SOURCE;
    }

    @Override
    public boolean isNameCompatible(String simpleName, Kind kind) {
        return simpleName.equals(getName()) && kind == Kind.SOURCE;
    }

    @Override
    public NestingKind getNestingKind() {
        return null;
    }

    @Override
    public javax.lang.model.element.Modifier getAccessLevel() {
        return null;
    }

	@Override
	public URI toUri() {
		// TODO Auto-generated method stub
		return null;
	}
}