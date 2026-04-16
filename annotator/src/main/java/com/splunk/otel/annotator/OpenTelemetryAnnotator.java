package com.splunk.otel.annotator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;
import java.util.stream.Stream;

/*
 *  Authored by Derek Desjardins
 *  ddesjardins@splunk.com
 *  December 2021
 */

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;



public class OpenTelemetryAnnotator {
	
	private static final String SHOP_ENV_FILE = "../.env";
	//private static final String FILE_PATH = "../shop/";
	private static final String FILE_PATH2 = 
			"../products/src/main/java/com/shabushabu/javashop/products";
	//private static final String FILE_PATH3 = 
	//		"../instruments/";
	
	  // list all files from this path
    public static ArrayList<Path> listFiles(URI path) throws IOException {
    	
    	
    	ArrayList<Path> result = new ArrayList<Path> () ;
    	try (Stream<Path> paths = Files.walk(Paths.get(path))) {
    	    paths
    	    .filter(Files::isReadable)
    	    .map(Path::toFile)
    	    .filter(file->file.getName().endsWith(".java"))
    	    .forEach( file-> {
    	    	result.add(file.toPath());
    	    	System.out.println(file);
    	    });       
    	}catch (IOException ex){
    	    ex.printStackTrace();
    	}
    	return result;

    }
	
	public static void annotateCodebase(File projectDir) throws Exception {
		ArrayList<Path> files = listFiles(projectDir.toURI());
		
		// Iterating over collection 'c' using iterator
		   for (java.util.Iterator<Path> iter = files.iterator(); iter.hasNext(); ) {
			   annotateFile(iter.next().toAbsolutePath().toString());
		   }
		
	}
	
	public static void annotateFile(String sFileName) throws Exception {
		 	CompilationUnit cu = StaticJavaParser.parse(new FileInputStream(sFileName));
	        
	        LexicalPreservingPrinter.setup(cu);
	        
	        System.out.println(LexicalPreservingPrinter.print(cu));
	       
	        VoidVisitor<Void> otelAnnotationVisitor = new OtelAnnotationVisitor();
	        otelAnnotationVisitor.visit(cu, null);
	        
	        
	        // Add OpenTelemetry @WithSpan and @SpanAttribute Support via package import to file. ( 1.19 )
	        cu.addImport(new ImportDeclaration("io.opentelemetry.instrumentation.annotations.WithSpan",false, false));
	        cu.addImport(new ImportDeclaration("io.opentelemetry.instrumentation.annotations.SpanAttribute",false, false));
	        
	        // Add OpenTelemetry @WithSpan and @SpanAttribute Support via package import to file. 1.10
	        //cu.addImport(new ImportDeclaration("io.opentelemetry.extension.annotations.WithSpan",false, false));
	        //cu.addImport(new ImportDeclaration("io.opentelemetry.extension.annotations.SpanAttribute",false, false));
	        
	        System.out.println("  ");
	        System.out.println("------------------");
	        System.out.println("  ");
	        
	        String sResult = LexicalPreservingPrinter.print(cu);
	        System.out.println(sResult);
	        File file = new File(sFileName);
	        File rename = new File(sFileName + "OLD");
	        
	    
	        boolean flag = file.renameTo(rename);
	  
	    
	        if (!flag) {
	            throw new IOException("Creation of  .OLD file copy failed...");
	        }
	        
	        PrintWriter out = new PrintWriter(sFileName);
	        out.println(sResult);
	        out.close();
	}
	
    public static void main(String[] args) throws Exception {
    	//  File projectDir = new File(FILE_PATH);
    	  //listFiles(projectDir.toURI());
	    // annotateCodebase(projectDir);
	     
	     File projectDir = new File(FILE_PATH2);
	     annotateCodebase(projectDir);
	     
	    // projectDir = new File(FILE_PATH3);
	   //  annotateCodebase(projectDir);
	    
		Properties properties = new Properties();
		 
		try (FileInputStream inputStream = new FileInputStream(SHOP_ENV_FILE)) {
            properties.load(inputStream);
            System.out.println("Properties read: " + properties);
        } catch (IOException e) {
            System.err.println("Failed to read properties file: " + e.getMessage());
            return;
        }
		
		properties.setProperty("Annotated",  "true");
          
		 try {     
             FileOutputStream outputStream = new FileOutputStream(SHOP_ENV_FILE) ;
             properties.store(outputStream, null);
             System.out.println("Properties written: " + properties);
 		} catch (Exception e) {
 			e.printStackTrace();
 		}   
       
    }

    
    private static class OtelAnnotationVisitor extends VoidVisitorAdapter<Void> {

        @Override 
        public void visit(  MethodDeclaration  md, Void arg) {
        	super.visit(md, arg);
        	
        	if ( md.getBody().isPresent() && 
        		!md.getName().asString().startsWith("main") && 
        	    !md.getName().asString().startsWith("set") &&
        	    !md.getName().asString().startsWith("health")) {
        		
        		
        		if (md.getName().asString().startsWith("get") && md.getName().asString().compareToIgnoreCase("getInstruments")!=0) {
        			return;
        		}
        		
        			md.addAnnotation("WithSpan");
        			NodeList<Parameter> params = md.getParameters();
        			params.forEach((param) -> {
        			
        				String paramName = "`" + param.getNameAsString() + "`"; 
        				paramName = paramName.replace('`', '"');
        				param.addSingleMemberAnnotation("SpanAttribute", paramName);
        			});
        	}
        }
    }
}
