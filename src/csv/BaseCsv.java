package csv;

import java.io.File;

public interface BaseCsv<T> {//svaka Csv klasa implementira ovaj ugovor 
	void save(File file, T data) throws Exception;
	void load(File file, T data) throws Exception;
}
