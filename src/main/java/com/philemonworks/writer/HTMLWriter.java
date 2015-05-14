/*
    Copyright 2004 Ernest Micklei @ PhilemonWorks.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
*/
package com.philemonworks.writer;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import com.philemonworks.writer.Table.Cell;

/**
 * HTMLWriter is a helper class for writing HTML 4.0 compliant documents.
 * Note that every method returns the receiver (a HTMLWriter) itself.
 * This allows for cascading style programming.
 * <p>
 * Methods exists for most common HTML tags. If one is missing, the
 * more generic tag-method defined in XMLWriter can be used.
 * <p>
 * Use the method newMap(...,...) to create a map of attribute values for a tag.
 * Alternatively, the combination opentag({tag}), attribute(..,..) and closetag() 
 * method can be used to write attributes for a tag.
 *  
 * @author E.M.Micklei
 */
public class HTMLWriter extends XMLWriter {
    public HTMLWriter(PrintStream out) {
        super(out);
    }  
	/**
	 * @param out OutputStream
	 */
	public HTMLWriter(OutputStream out) {
		super(out);
	}	
    /**
     * Writes &lt;button name="{name}" value="{value}" type="{type}" onclick="{onclick}" style="{style}"&gt;{label}&lt;/button&gt;
     * @param name is the name of the button
     * @param value is the value 
     * @param type is either submit or ...
     * @param label is the display label for the button
     * @param onclick is the action (javascript) to be executed when the button is clicked.
     * @param style is the style of the button 
     * @return HTMLWriter
     */
    public HTMLWriter button(String name, String value, String type, String label, String onclick, String style) {
        Map map = new HashMap();
        map.put("name", name);
        map.put("value", value);
        map.put("type", type);
        map.put("onclick", onclick);
        map.put("style", style);
		return this.button(map,label);
   }
	/**
	 * Writes a button using a map of attributes and a label
	 * @param map
	 * @param label
	 * @return HTMLWriter
	 */
	public HTMLWriter button(Map map, String label) {
        this.tag("button", map, false);
        this.print(label);
        return (HTMLWriter) this.end();
    }	

    /**
     * Writes a strict xhtml doctype
     * @return HTMLWriter
     */
    public HTMLWriter doctype() {
        return (HTMLWriter) this.raw("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \n\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", true);
    }

    /**
     * Writes &lt;img src="{src}" alt="{alt}" /&gt; Do not close with end();
     * @param src
     * @param alt
     * @return HTMLWriter
     */
    public HTMLWriter img(String src, String alt) {
        return (HTMLWriter) this.tag("img", newMap("src", src, "alt", alt), true);
    }

    /**
     * Writes &lt;html&gt; Must close with end(); 
     * @return HTMLWriter
     */
    public HTMLWriter html() {
        return (HTMLWriter) this.tag("html");
    }

    /**
     * Writes &lt;hr/&gt; 
     * @return HTMLWriter
     */
    public HTMLWriter hr() {
        return (HTMLWriter) this.emptytag("hr");
    }	
	
    /**
     * Write a base element (should be inside head).
     * Used for making relative links.
     * @param href : String = absolute URL
     * @return HTMLWriter
     */
    public HTMLWriter base(String href){
        return (HTMLWriter)this.tag("base",this.newMap("href",href),true);
    }
    
    /**
     * Writes &lt;body&gt; Must close with end(); 
     * @return HTMLWriter
     */
    public HTMLWriter body() {
        return (HTMLWriter) this.tag("body");
    }

    /**
     * Writes &lt;body {attributes} &gt; Must close with end(); 
     * @param attributes is a Map of attribute-value pairs.
     * @return HTMLWriter
     */
    public HTMLWriter body(Map attributes) {
        return (HTMLWriter) this.tag("body", attributes , false);
    }

    /**
     * Writes &lt;head&gt; Must close with end(); 
     * @return HTMLWriter
     */
    public HTMLWriter head() {
        return (HTMLWriter) this.tag("head");
    }

    /**
     * Writer meta tags to prevent the Browser from caching a page.
     * @return HTMLWriter
     */
    public HTMLWriter noCacheMetaTags(){
        this.raw("<meta http-equiv=\"Pragma\" content=\"no-cache\"/>",true);
        this.raw("<meta http-equiv=\"Expires\" content=\"-1\"/>",true);
        return this;
    }
    
    
    /**
     * Writes &lt;title&gt;{title}&lt;/title&gt;
     * @param title
     * @return HTMLWriter
     */
    public HTMLWriter title(String title) {
        return (HTMLWriter) this.tagged("title", title, true);
    }

    /**
     * Writes &lt;b&gt;{content}&lt;/b&gt;
     * @param content
     * @return HTMLWriter
     */
    public HTMLWriter bold(String content) {
        return (HTMLWriter) this.tagged("b", content, true);
    }
    
    /**
     * Writes &lt;table&gt; Must close with end() or end("table"); 
     * @return HTMLWriter
     */
    public HTMLWriter table() {
        return (HTMLWriter) this.tag("table");
    }	
    /**
     * Writes &lt;table key="value" ... &gt; Must close with end() or end("table");
     * @param attributesOrNull : Map containing key value pairs.
     * @return HTMLWriter
     */
    public HTMLWriter table(Map attributesOrNull) {
        return (HTMLWriter) this.tag("table", attributesOrNull, false);
    }
    /**
     * Writes a complete prepared table structure.
     * @param aTable to write
     * @return HTMLWriter
     */
    public HTMLWriter table(Table aTable){
		this.tag("table", aTable.getAttributesMap(), false);
		for (int r = 1; r <= aTable.getMaxRows(); r++) {
			Map row = aTable.getRowAt(r);
			Map map = aTable.getRowAttributesAt(r);
			this.tag("tr", map, false);
			boolean useHeader = r == 1 && aTable.firstRowIsHeader;
			if (row != null) {
				for (int c = 1; c <= aTable.getMaxColumns(); c++) {
					Cell entry = (Cell)row.get(new Integer(c));
					if (entry == null) {
						this.tableEmptyCell();
					} else if (entry == Table.SPANNEDCELL) {
						;// skip cell
					} else if (entry.contents == null) {
						this.tableEmptyCell(); // empty cell
					} else if (useHeader) this.th((Cell)entry); else this.td((Cell) entry);
				}
			} else {
			    tableEmptyCell();
			}
			this.end(); // row
		}
		return (HTMLWriter)this.end(); // table
    }	
	/**
	 * Invoked by the Table write method.
	 */
	protected void tableEmptyCell() {
		this.indent().raw("<td class=\"empty\">&nbsp;</td>",true);
	}
    /**
     * Writes &lt;tr&gt; Must close with end(); 
     * @return HTMLWriter
     */
    public HTMLWriter tr() {
        return (HTMLWriter) this.tag("tr");
    }

    /**
     * Writes &lt;tr key="value" ... &gt; Must close with end(); 
     * @param attributesOrNull : Map containing key value pairs.
     * @return HTMLWriter
     */
    public HTMLWriter tr(Map attributesOrNull) {
        return (HTMLWriter) this.tag("tr", attributesOrNull, false);
    }
    
    /**
     * Writes &lt;div key="value" ... &gt; Must close with end(); 
     * @param attributesOrNull : Map containing key value pairs.
     * @return HTMLWriter
     */
    public HTMLWriter div(Map attributesOrNull) {
        return (HTMLWriter) this.tag("div", attributesOrNull, false);
    }    
    
    /**
     * Writes &lt;div key="value" ... &gt; Must close with end(); 
     * @param attributesOrNull : Map containing key value pairs.
     * @return HTMLWriter
     */
    public HTMLWriter div(String key,String value) {
        return (HTMLWriter) this.tag("div", key, value);
    }    
    
    /**
     * Writes &lt;td&gt; Must close with end(); 
     * @return HTMLWriter
     */
    public HTMLWriter td() {
        return (HTMLWriter) this.tag("td");
    }
    /**
     * Writes &lt;th&gt; Must close with end(); 
     * @return HTMLWriter
     */
    public HTMLWriter th() {
        return (HTMLWriter) this.tag("th");
    }    
    /**
     * Writes &lt;td key="value" ... &gt; Must close with end(); 
     * @param attributesOrNull : Map containing key value pairs.
     * @return HTMLWriter
     */
    public HTMLWriter td(Map attributesOrNull) {
        return (HTMLWriter) this.tag("td", attributesOrNull, false);
    }
    /**
     * Writes &lt;td&gt;{content}&lt;/td&gt;. No need to close with end();
     * @param content : String contents of cell 
     * @return HTMLWriter
     */
    public HTMLWriter td(String content) {
        return (HTMLWriter) this.tagged("td", content, true);
    }

    public HTMLWriter th(Map attributesOrNull, String content) {
        this.tag("th", attributesOrNull, false);
        this.print(content);
        return (HTMLWriter) this.end();
    }    
    
    public HTMLWriter td(Map attributesOrNull, String content) {
        this.tag("td", attributesOrNull, false);
        this.print(content);
        return (HTMLWriter) this.end();
    }
    /**
     * Writes a Table.Cell
     * @param aCell
     * @return HTMLWriter
     */
    public HTMLWriter td(Table.Cell aCell){
		return this.t("td",aCell);
    }    
    /**
     * Writes a Table.Cell
     * @param aCell
     * @return HTMLWriter
     */
    public HTMLWriter th(Table.Cell aCell){
		return this.t("th",aCell);
    } 	
    /**
     * Writes a Table.Cell
     * @param h_d String either "th" or "td"
     * @param aCell
     * @return HTMLWriter
     */
    private HTMLWriter t(String h_d, Table.Cell aCell){
		this.tag(h_d, aCell.getAttributesMap(),false); 
		if (aCell.contents instanceof Table)
			 this.table((Table) aCell.contents);
		else
			this.raw(String.valueOf(aCell.contents));
		this.end();
		return this;
    }  	
    public HTMLWriter th(String content) {
        return (HTMLWriter) this.tagged("th", content, true);
    }

    public HTMLWriter h1(String content) {
        return this.h(1, content);
    }

    public HTMLWriter h2(String content) {
        return this.h(2, content);
    }

    public HTMLWriter h3(String content) {
        return this.h(3, content);
    }

    public HTMLWriter h(int level, String content) {
        String tag = "h" + level;
        return (HTMLWriter) this.tagged(tag, content, true);
    }

    public HTMLWriter nbsp() {
        this.indent();
        return (HTMLWriter) this.raw("&nbsp;", true);
    }

    public HTMLWriter p(String content) {
        return (HTMLWriter) this.tagged("p", content, true);
    }

    public HTMLWriter p(String styleClass, String content) {
        return (HTMLWriter) this.tagged("p", this.newMap("class", styleClass), content, true);
    }

    public XMLWriter stylesheet(String href) {
        this.opentag("link");
        this.attribute("type", "text/css");
        this.attribute("rel", "stylesheet");
        this.attribute("href", href);
        return this.closeemptytag();
    }

    public HTMLWriter anchor(String name) {
        return (HTMLWriter) this.tag("a", this.newMap("name", name), true);
    }

    public HTMLWriter href(String url, String title) {
        return this.href(url, null, null, title);
    }

    public HTMLWriter href(String url, String styleClassOrNull, String targetOrNull, String title) {
        this.opentag("a");
        this.attribute("href", url);
        this.attribute("target", targetOrNull);
        this.attribute("class", styleClassOrNull);		
        this.closetag();
        this.print(title);
        return (HTMLWriter) this.end(); // a
    }

    public HTMLWriter li() {
        return (HTMLWriter) this.tag("li");
    }

    public HTMLWriter br() {
        return (HTMLWriter) this.emptytag("br");
    }
}