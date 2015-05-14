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

import java.util.HashMap;
import java.util.Map;

/**
 * Table represents an object for storing two-dimensional structured data. Rows and columns are 1-based so the top-left
 * cell has the coordinates (1,1) Entries of a Table are String of Cell objects
 */
public class Table {
	private Map tableAttributesMap = new HashMap();
	public boolean firstRowIsHeader = true;
	private HashMap rows = new HashMap();
	private HashMap rowAttributesMaps = new HashMap();
	private int maxR = 0;
	private int maxC = 0;
	public static final Object SPANNEDCELL = (new Table()).new Cell();
	/**
	 * Inner class that represents a Cell in a Table. The contents of a Cell is either a Table or a String containing
	 * raw HTML contents.
	 */
	public class Cell {		
		private Map attributesMap = null;
		public Object contents = null;

		/**
		 * Add the colspan attribute. Should be send once if needed.
		 * 
		 * @param span
		 */
		public void colspan(int span) {
			if (span == 1) {
				if (attributesMap != null)
					attributesMap.remove("colspan");
				return;
			}
			this.setAttribute("colspan", String.valueOf(span));
		}
		/**
		 * Add the rowspan attribute. Should be send once if needed.
		 * 
		 * @param span
		 */
		public void rowspan(int span) {
			if (span == 1) {
				if (attributesMap != null)
					attributesMap.remove("rowspan");
				return;
			}			
			this.setAttribute("rowspan", String.valueOf(span));
		}
		/**
		 * Set the contents of the cell
		 * 
		 * @param anObject :
		 *        String || Table
		 */
		public void setContents(Object anObject) {
			contents = anObject;
		}
		/**
		 * Add or overwrite a key-value pair of String.
		 * 
		 * @param key :
		 *        String
		 * @param value :
		 *        String
		 */
		public void setAttribute(String key, String value) {
			this.getAttributesMap().put(key, value);
		}
		/**
		 * Lazy initialize the map of attributes and answer it.
		 * 
		 * @return Map
		 */
		public Map getAttributesMap() {
			if (attributesMap == null)
				attributesMap = new HashMap();
			return attributesMap;
		}
	}

	/**
	 * Return the map of row data
	 * 
	 * @param row :
	 *        one-based row indez
	 * @return Map
	 */
	public Map getRowAt(int row) {
		return (HashMap) rows.get(new Integer(row));
	}
	/**
	 * Return the attributes for a row.
	 * 
	 * @param row
	 * @return Map
	 */
	public Map getRowAttributesAt(int row) {
		return (Map) this.rowAttributesMaps.get(new Integer(row));
	}
	public int getMaxRows() {
		return maxR;
	}
	public int getMaxColumns() {
		return maxC;
	}
	/**
	 * Answer the table entry which is either a String or a Table.Cell
	 * 
	 * @param r
	 * @param c
	 * @return Object the entry
	 */
	public Object get(int r, int c) {
		HashMap row = (HashMap) rows.get(new Integer(r));
		if (row == null) {
			row = new HashMap();
			rows.put(new Integer(r), row);
		}
		Object entry = row.get(new Integer(c));
		maxR = Math.max(maxR, r);
		maxC = Math.max(maxC, c);
		return entry;
	}
	/**
	 * Store an entry (String or Cell or Table) on the location (r,c)
	 * 
	 * @param r =
	 *        row of the table (1-based)
	 * @param c =
	 *        column of the table (1-based)
	 * @param entry :
	 *        Object || Table.Cell || Table || SPANNEDCELL
	 */
	public Cell put(int r, int c, Object entry) {
		Cell newCell;
		if (entry instanceof Cell)
			newCell = (Cell)entry;
		else {
			newCell = new Cell();
			newCell.contents = entry;
		}
		HashMap row = (HashMap) rows.get(new Integer(r));
		if (row == null) {
			row = new HashMap();
			rows.put(new Integer(r), row);
		}
		row.put(new Integer(c), newCell);
		maxR = Math.max(maxR, r);
		maxC = Math.max(maxC, c);
		return newCell;
	}
	/**
	 * Store an entry (String or Table) on the location (r,c) which takes multiple rows and multiple columns. The
	 * topleft location of this area will keep the Cell. The other locations within the area will have the SPANNEDCELL
	 * constant.
	 * 
	 * @param r =
	 *        row of the table (1-based)
	 * @param c =
	 *        column of the table (1-based)
	 * @param rows :
	 *        int = spanned rows
	 * @param columns :
	 *        int = spanned columns
	 * @param entry :
	 *        String || Table
	 * @return topLeft : Table.Cell
	 */
	public Cell put(int r, int c, int rows, int columns, Object entry) {
		Cell topLeft = new Cell();
		topLeft.contents = entry;
		topLeft.colspan(columns);
		topLeft.rowspan(rows);
		// mark other cells in the region bounded by (r,c,rows,columns)
		for (int x = r; x < (r + rows); x++) {
			for (int y = c; y < (c + columns); y++) {
				this.put(x, y, SPANNEDCELL);
			}
		}
		// now replace the marked cell by the new
		this.put(r, c, topLeft);
		return topLeft;
	}
	/**
	 * Store an integer entry on the location (r,c)
	 * 
	 * @param r =
	 *        row of the table (1-based)
	 * @param c =
	 *        column of the table (1-based)
	 * @param entry :
	 *        int
	 * @return Cell
	 */
	public Cell put(int r, int c, int entry) {
		return this.put(r, c, String.valueOf(entry));
	}
	public void setRowAttributesMap(int row, Map attributes) {
		rowAttributesMaps.put(new Integer(row), attributes);
	}
	/**
	 * Add or overwrite a key-value pair of String for the table attributes.
	 * 
	 * @param key :
	 *        String
	 * @param value :
	 *        String
	 */
	public void setAttribute(String key, String value) {
		tableAttributesMap.put(key, value);
	}
	
	/**
	 * Remove the attribute (does not fail if it was not present)
	 * @param key : String
	 */
	public void removeAttribute(String key){
		tableAttributesMap.remove(key);
	}
	
	/**
	 * Answer the Map of table attributes
	 * @return Map
	 */
	public Map getAttributesMap(){
		return tableAttributesMap;
	}
}