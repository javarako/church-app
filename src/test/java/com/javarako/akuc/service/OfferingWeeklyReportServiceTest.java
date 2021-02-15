package com.javarako.akuc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OfferingWeeklyReportServiceTest {

	public static final String SRC = "C:/tmp/244558.pdf";
	public static final String DEST = "C:/tmp/244558-2.pdf";

	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public OfferingWeeklyReportServiceTest() {

	}

	public static void main(String[] args) {
		OfferingWeeklyReportServiceTest test = new OfferingWeeklyReportServiceTest();
		//test.updatePDF1();
		test.findFiles();
	}

	public void findFiles() {
		ClassLoader cl = this.getClass().getClassLoader();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		try {
		        Resource resources[] = resolver.getResources("WeeklyOfferingReportGEN*.pdf");
		        Arrays.asList(resources).stream()
                .forEach(x -> {
                	try {
						File file = x.getFile();
                		log.info(x.getFilename());
						file.deleteOnExit();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                });
		        
		        Path rootPath = Paths.get("./");
		        List<File> allFiles = Files.walk(rootPath).sorted().map(Path::toFile).filter(f -> 
		        	f.getName().endsWith("pdf")).collect(Collectors.toList());
		        allFiles.forEach(x -> log.info(x.getName()));
		        
	        
		        String pattern = "WeeklyOfferingReportGEN*.pdf";
		        PathMatcher matcher =
		            FileSystems.getDefault().getPathMatcher("glob:" + pattern);
		        
		        List<File> reports = 
		        Files.walk(rootPath)
		        	.filter(f -> matcher.matches(f.getFileName()))
		        	.map(Path::toFile)
		        	.collect(Collectors.toList());

		        long oneDayOld = new Date().getTime() - (24 * 60 * 60 * 1000); 

		        reports.forEach(f -> {  
		        	if (f.lastModified() < oneDayOld)
		        		f.deleteOnExit();
		        	});

	            
		} catch (IOException e) {
		        e.printStackTrace();
		}
	}
	
	public static List<File> search(Path rootPath, String fileExtension, List<String> wildcardKeywords) throws IOException {
        //Get all files under specified path matching with specified file extention list.
        List<File> allFiles = Files.walk(rootPath).sorted().map(Path::toFile).filter(f -> f.getName().endsWith(fileExtension)).collect(Collectors.toList());
        return allFiles.stream().map(f -> {
            try {
                //Read all lines of a file to List.
                List<String> lines = Files.readAllLines(f.toPath());
                //matching flag
                boolean isMatching = false;
                //String line loop.
                for(int i=0; i<lines.size(); i++) {
                    String line = lines.get(i);
                    for(String keyword : wildcardKeywords) {
                        String regex = Arrays.asList(keyword.split(Pattern.quote("*"))).stream().map(s -> s.equals("") ? "" : Pattern.quote(s)).collect(Collectors.joining(".*"))+".*";
                        //if a line be matched with pattern regex, print line number and file absolute path.
                        if(line.matches(regex)) {
                            //Print formatted text on STD IO.
                            System.out.println(String.format("%-10s   %-50s   %s", "LINE: "+(i+1), "MATCHING KEYWORD: "+keyword, "PATH: "+f.getAbsolutePath()));
                            isMatching = true;
                        }   
                    }
                }
                if(isMatching) {
                    return f;
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
            //Filtering null value.                     
        }).filter(f -> f != null).collect(Collectors.toList());
    }
	
	public void updatePDF1() {
		Font blueFont = FontFactory.getFont(FontFactory.HELVETICA, 8, Font.NORMAL, new CMYKColor(255, 0, 0, 0));
		Font redFont = FontFactory.getFont(FontFactory.COURIER, 12, Font.BOLD, new CMYKColor(0, 255, 0, 0));
		Font yellowFont = FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD, new CMYKColor(0, 0, 255, 0));

		try {
			// Create PdfReader instance.
			PdfReader pdfReader = new PdfReader("WeeklyOfferingReport.pdf");

			ClassLoader classLoader = this.getClass().getClassLoader();
			
			String fileName = classLoader.getResource(".").getFile() + "WeeklyOfferingReportGEN" + System.currentTimeMillis() + ".pdf";
			log.info("fileName:"+fileName);
			
			// Create PdfStamper instance.
			PdfStamper pdfStamper = new PdfStamper(pdfReader,
					new FileOutputStream(fileName));

			// Create BaseFont instance.
			BaseFont baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

			// Get the number of pages in pdf.
			int pages = pdfReader.getNumberOfPages();

			// Iterate the pdf through pages.
			for (int i = 1; i <= pages; i++) {
				// Contain the pdf data.
				PdfContentByte pageContentByte = pdfStamper.getOverContent(i);

				Date today = new Date();

				pageContentByte.beginText();
				// Set text font and size.
				pageContentByte.setFontAndSize(baseFont, 12);

				pageContentByte.setTextMatrix(150, 626);

				// Write text
				pageContentByte.showText(DATE_FORMAT.format(today));
				pageContentByte.endText();

				pageContentByte.beginText();
				// Set text font and size.
				pageContentByte.setFontAndSize(baseFont, 12);

				pageContentByte.setTextMatrix(150, 604);

				// Write text
				pageContentByte.showText(DATETIME_FORMAT.format(today));
				pageContentByte.endText();
				
				
				// Write text
				pageContentByte.showText(DATE_FORMAT.format(today));
				pageContentByte.endText();

				pageContentByte.beginText();
				// Set text font and size.
				pageContentByte.setFontAndSize(baseFont, 12);

				pageContentByte.setTextMatrix(150, 580);

				// Write text
				pageContentByte.showText(String.format(" $100 X %3d    : %7.2f", 2, (double)(100 * 2)));
				pageContentByte.endText();

			}

			// Close the pdfStamper.
			pdfStamper.close();

			log.info("PDF modified successfully.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
