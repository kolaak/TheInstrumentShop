package com.splunk.otel.annotator;



import java.io.File;

import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;


public class DirExplorerInstrumented {
	
    public interface FileHandler {
    	
        void handle(int level, String path, File file);
    }
    
    public interface Filter {
    	
        boolean interested(int level, String path, File file);
    }
    
    
    private FileHandler fileHandler;
    
    private Filter filter;
    
    public DirExplorerInstrumented(Filter filter, FileHandler fileHandler) {
        
    	this.filter = filter;
        this.fileHandler = fileHandler;
        
    }
    
    @WithSpan()
    public void explore(@SpanAttribute("root") File root) {
    	
        explore(0, "", root);
        
    }
    
    @WithSpan()
    private void explore(@SpanAttribute("level") int level, @SpanAttribute("path") String path, @SpanAttribute("file") File file) {
       
    	if (file.isDirectory()) {
        	
            for (File child : file.listFiles()) {
                explore(level + 1, path + "/" + child.getName(), child);
            }
            
        } else {
           
        	if (filter.interested(level, path, file)) {
                fileHandler.handle(level, path, file);
            }
        	
        }
    }
    
    
    
    
}