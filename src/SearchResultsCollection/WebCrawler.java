/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SearchResultsCollection;

import CommonTools.CTs;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;



/**
 *
 * @author Administrator
 */
public class WebCrawler {
    
    // 设置缓冲区大小为10K
	private final static int BUFFERSIZE = 10 * 1024;
	// 1秒
	private final static int OneSecond = 1000;
        
        
//    private Page page;
//    WebCrawler(){
//        page = null;
//    
//    }
    public  synchronized static String getPageContent(String url){
        
      		URL WebPageUrl = null;
		HttpURLConnection Connection = null;
		InputStreamReader StreamReader = null;
		BufferedReader reader = null;
//		char[] buffer = new char[BUFFERSIZE]; // 缓冲区
//		int readlength = 0;
		String content = new String(); // 要返回的String
//		StringBuilder strBuilder = new StringBuilder(); // 存储读取网页内容

		String charset = null; // 网页字符集
                String defaultCharset = "gb2312";

		try {
			WebPageUrl = new URL(url);
			Connection = (HttpURLConnection) WebPageUrl.openConnection();
                        
			// 重定向设置
//			HttpURLConnection.setFollowRedirects(true);
			Connection.setInstanceFollowRedirects(true);
                        
                        Connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; CIBA");

			// 设置连接超时时间和读超时时间为10秒和60秒
                        
			Connection.setConnectTimeout(60 * OneSecond);
			Connection.setReadTimeout(60 * OneSecond);

			StreamReader = new InputStreamReader(Connection.getInputStream(),defaultCharset);
			reader = new BufferedReader(StreamReader);

			String line = new String();

			// 从网页的前100行中找出字符集，若未找到，则默认为gb2312
			while (null != (line = reader.readLine())) {
                            content += line + "\r\n";
                            if (charset == null && line.contains("charset=")) {
                                    int begin = line.indexOf("content=");
                                    // 8 是字符串content=的长度
                                    begin += 8;
					// 8 是字符串charset=的长度
                                    int beginIndex = line.indexOf("charset=") + 8;
                                    int endIndex = beginIndex;

                                    if ('"' == line.charAt(begin)) {
                                        endIndex = line.indexOf('"', beginIndex);
//                                                CTs.priorPrint("endIndex = "+endIndex);
                                    }
                                    else if ('\'' == line.charAt(begin)) {
                                        endIndex = line.indexOf('\'', beginIndex);

                                    }
                                    else if(line.substring(beginIndex).contains("\"")){
                                        if(line.substring(beginIndex).indexOf("\"") == 0){
                                            beginIndex++;
                                            endIndex = line.substring(beginIndex).indexOf("\"")+beginIndex;
                                        }else{
                                            endIndex = line.substring(beginIndex).indexOf("\"")+beginIndex;
                                        }

                                            // 未处理
                                    }

                                    charset = line.substring(beginIndex, endIndex).toLowerCase();
                                    if(!charset.equals(defaultCharset)){
                                        break;
                                    }
                                    
                            }
				/*
				 * if( line.contains( "charset=" )) { int begin = line.indexOf(
				 * "charset=" ); int beginIndex = begin + "charset=".length();
				 * int endIndex = 0; if( '"' == line.charAt( beginIndex )) {
				 * beginIndex += 1; endIndex = line.indexOf( '"', beginIndex );
				 * } else { endIndex = line.indexOf( '"', begin ); }
				 * 
				 * 
				 * charset = line.substring( beginIndex, endIndex ); break; }
				 */
			}
		}catch(java.net.SocketTimeoutException e){
                    e.printStackTrace();
                   return ""; 
                }catch (MalformedURLException e1) {
			e1.printStackTrace();
                        return "";
		}catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (null != reader) {
				try {
					reader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
                        if(Connection != null){
                            Connection.disconnect();
                        }
		}

		if (null == charset || "".equals(charset)) {
//			charset = "utf-8";
                    return content;
		}else if(!charset.equals(defaultCharset)){
                    System.err.println("need to crawl the second time!!!!!!!!!!!!");
                    try {
                        WebPageUrl = new URL(url);
                        Connection = (HttpURLConnection) WebPageUrl.openConnection();
                        Connection.setInstanceFollowRedirects(false);
                        Connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; CIBA");
                        Connection.setConnectTimeout(10 * OneSecond);
			Connection.setReadTimeout(60 * OneSecond);
                        StreamReader = new InputStreamReader(Connection.getInputStream(),charset);
			reader = new BufferedReader(StreamReader);
                        String line = new String();
                        content = "";
                        while (null != (line = reader.readLine())) {
                            content += line + "\r\n";
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
			try {
				if (null != reader) {
					reader.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
                    }
                }
            return content;
    } 
        
        
        //*******************************************************NekoHtml Join in ****************************************
        
//        String content = null;
//        try {
//            URL connectUrl = new URL(url);
//            HttpURLConnection urlConnection = (HttpURLConnection) connectUrl.openConnection();
//            urlConnection.setInstanceFollowRedirects(true);
//            urlConnection.setConnectTimeout(10 * OneSecond);
//            urlConnection.setReadTimeout(60 * OneSecond);
//            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
//                String charSet = urlConnection.getContentEncoding();
//                if(charSet == null){
//                    charSet = "gbk";
//                }
//                InputStream stream = urlConnection.getInputStream();
//                StringWriter filteredDescription = new StringWriter();
//                org.cyberneko.html.filters.Writer writer = new org.cyberneko.html.filters.Writer(filteredDescription, null);
//                DOMParser parser = new DOMParser();
//                XMLDocumentFilter[] filters = { /*remover ,*/ writer };
//                parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
//                parser.setProperty("http://cyberneko.org/html/properties/default-encoding", charSet);
//                parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
//                parser.parse(new InputSource(stream));
//                content = filteredDescription.toString();
//            }
//        } catch (SAXException ex) {
//            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        //*******************************************************NekoHtml Join in ****************************************
        
     
    public static String getPageContentByNekoHtml(String url){
        
        String content = "";
        try {
            URL connectUrl = new URL(url);
            HttpURLConnection urlConnection = null;
            
//            HttpURLConnection.setFollowRedirects(true);//by zk
//            urlConnection.setInstanceFollowRedirects(false); //by zk
//            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; CIBA");
//            urlConnection.setConnectTimeout(10 * OneSecond);
//            urlConnection.setReadTimeout(60 * OneSecond);
//          
            int tryCount = 0;
            while(true){
                urlConnection = (HttpURLConnection) connectUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setInstanceFollowRedirects(true); //zk del
                urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; CIBA");
                urlConnection.setConnectTimeout(300 * OneSecond);
                urlConnection.setReadTimeout(300 * OneSecond);
                int responseCode = -1;
                try{
                    responseCode = urlConnection.getResponseCode();
                }catch(IOException ex){
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                if(responseCode != HttpURLConnection.HTTP_OK){
                    tryCount++;
                    if(tryCount > 3){
                        break;
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("trying again " + tryCount);
                }else{
                    break;
                }
            }
            if(tryCount > 3){
                System.out.println("get page content failed!!!");
                return null;
            }
            String charSet = getCharSet(urlConnection);
            System.out.println("charSet = " + charSet);  
//                if(charSet == null){
//                    
//                }
            
//                stream.
//                InputStream stream1 = urlConnection.getInputStream();
//                InputStream stream1 = stream;
//                InputSource is = new InputSource(stream);
                
                
            StringWriter filteredDescription = new StringWriter();
            org.cyberneko.html.filters.Writer writer = new org.cyberneko.html.filters.Writer(filteredDescription, null);
            DOMParser parser = new DOMParser();
            XMLDocumentFilter[] filters = { /*remover ,*/ writer };
            parser.setProperty("http://cyberneko.org/html/properties/filters", filters);
            parser.setProperty("http://cyberneko.org/html/properties/default-encoding", charSet);
            parser.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            urlConnection = (HttpURLConnection) connectUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setInstanceFollowRedirects(true); //zk del
            urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; CIBA");
            urlConnection.setConnectTimeout(300 * OneSecond);
            urlConnection.setReadTimeout(300 * OneSecond);
            InputStream stream = urlConnection.getInputStream();
            try{
                parser.parse(new InputSource(stream));//java.net.SocketException
            }catch(SocketException ex){
                System.err.println("socket exception occured!!");
                stream = urlConnection.getInputStream();
                parser.parse(new InputSource(stream));
            }
//            parser.parse(new InputSource(stream));
            content = filteredDescription.toString();
            stream.close();   
                
                
//                int charSet_Index = content.toLowerCase().indexOf("charset");
//                if(content.toLowerCase().substring(charSet_Index+7, charSet_Index+30).contains("UTF-8".toLowerCase())){
//                    System.out.println("find the charSet is UTF-8, recrawler begining...");
//                    URL connectUrl_second = new URL(url);
//                    HttpURLConnection urlConnection_second = (HttpURLConnection) connectUrl_second.openConnection();
//                    urlConnection_second.setInstanceFollowRedirects(true); //zk del
//        //            HttpURLConnection.setFollowRedirects(true);//by zk
//        //            urlConnection.setInstanceFollowRedirects(false); //by zk
//                    urlConnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648; CIBA");
//                    urlConnection_second.setConnectTimeout(10 * OneSecond);
//                    urlConnection_second.setReadTimeout(60 * OneSecond);
//        //          
//
//                    if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
//                        
//                        InputStream stream_second = urlConnection_second.getInputStream();
//                        StringWriter filteredDescription_second = new StringWriter();
//                        org.cyberneko.html.filters.Writer writer_second = new org.cyberneko.html.filters.Writer(filteredDescription_second, null);
//                        DOMParser parser_second = new DOMParser();
//                        XMLDocumentFilter[] filters_second = { /*remover ,*/ writer_second };
//                        parser_second.setProperty("http://cyberneko.org/html/properties/filters", filters_second);
//                        parser_second.setProperty("http://cyberneko.org/html/properties/default-encoding", "UTF-8");
//                        parser_second.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
//                        parser_second.parse(new InputSource(stream_second));
//        //                parser.parse(is);
//                        content = filteredDescription_second.toString();
//                   }
//                }
            
        } catch (SAXException ex) {
//             AdaptiveWebObjectExtration.crawler_Index = -1;
             Thread.interrupted();   
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
//            AdaptiveWebObjectExtration.crawler_Index = -1;
            Thread.interrupted();   
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(content == null)
            content = "";
        return content;
    }
    
    
    private static String getCharSet(HttpURLConnection urlConnection){
        String strencoding = urlConnection.getContentEncoding();     
        if(strencoding != null){
            System.out.println("find charset by request!!!");
            return strencoding;
        }
        /**   
         * 首先根据header信息，判断页面编码   
         */    
        // map存放的是header信息(url页面的头信息)     
        Map<String, List<String>> map = urlConnection.getHeaderFields();     
        Set<String> keys = map.keySet();     
        Iterator<String> iterator = keys.iterator();     
    
        // 遍历,查找字符编码     
        String key = null;     
        String tmp = null;     
        while (iterator.hasNext()) {     
            key = iterator.next();     
            tmp = map.get(key).toString().toLowerCase();     
            // 获取content-type charset     
            if (key != null && key.equals("Content-Type")) {     
                int m = tmp.indexOf("charset=");     
                if (m != -1) {     
                    strencoding = tmp.substring(m + 8).replace("]", "");
                    System.out.println("find charset by header!!!");
                    return strencoding;     
                }     
            }     
        }     
    
        /**   
         * 通过解析meta得到网页编码   
         */    
        // 获取网页源码(英文字符和数字不会乱码，所以可以得到正确<meta/>区域)     
        StringBuffer sb = new StringBuffer();     
        String line;     
        try {     
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));     
            while ((line = in.readLine()) != null) {     
                sb.append(line);     
            }     
            in.close();     
        } catch (Exception e) { // Report any errors that arise     
            System.err.println(e);     
            System.err.println("Usage:   java   HttpClient   <URL>   [<filename>]");     
        }     
        String htmlcode = sb.toString();     
        // 解析html源码，取出<meta />区域，并取出charset     
        String strbegin = "<meta";     
        String strend = ">";     
        String strtmp;     
        int begin = htmlcode.indexOf(strbegin);     
        int end = -1;     
        int inttmp;     
        while (begin > -1) {     
            end = htmlcode.substring(begin).indexOf(strend);     
            if (begin > -1 && end > -1) {     
                strtmp = htmlcode.substring(begin, begin + end).toLowerCase();     
                inttmp = strtmp.indexOf("charset");     
                if (inttmp > -1) {     
                    strencoding = strtmp.substring(inttmp + 7, end).replace("=", "").replace("/", "").replace("\"", "").replace("\'", "").replace(" ", "");   
                    System.out.println("find charset by meta!!!");
                    return strencoding;     
                }     
            }     
            htmlcode = htmlcode.substring(begin);     
            begin = htmlcode.indexOf(strbegin);     
        }     
    
        if (strencoding == null) {     
            strencoding = "GBK";     
        }      
        return strencoding; 
    }
     
}
