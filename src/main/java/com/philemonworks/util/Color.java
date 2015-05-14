/*
    Copyright 2005 Ernest Micklei @ PhilemonWorks.com

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
package com.philemonworks.util;
import java.io.Serializable;

/**
 * Use this class when you want to represent Color objects  in a headless Java (server) environment without AWT support.
 * 
 * @author E.M.Micklei
 */
public class Color implements Serializable {
	private static final long serialVersionUID = 3544948853238413621L;
	
	public int redValue = 0;
	public int greenValue = 0;
	public int blueValue = 0;

	/**
	 * The color white.  In the default sRGB space.
	 */
	public final static Color white = new Color(255, 255, 255);

	/**
	 * The color white.  In the default sRGB space.
	 */
	public final static Color WHITE = white;

	/**
	 * The color light gray.  In the default sRGB space.
	 */
	public final static Color lightGray = new Color(192, 192, 192);

	/**
	 * The color light gray.  In the default sRGB space.
	 */
	public final static Color LIGHT_GRAY = lightGray;

	/**
	 * The color gray.  In the default sRGB space.
	 */
	public final static Color gray = new Color(128, 128, 128);

	/**
	 * The color gray.  In the default sRGB space.
	 */
	public final static Color GRAY = gray;

	/**
	 * The color dark gray.  In the default sRGB space.
	 */
	public final static Color darkGray = new Color(64, 64, 64);

	/**
	 * The color dark gray.  In the default sRGB space.
	 */
	public final static Color DARK_GRAY = darkGray;

	/**
	 * The color black.  In the default sRGB space.
	 */
	public final static Color black = new Color(0, 0, 0);

	/**
	 * The color black.  In the default sRGB space.
	 */
	public final static Color BLACK = black;

	/**
	 * The color red.  In the default sRGB space.
	 */
	public final static Color red = new Color(255, 0, 0);

	/**
	 * The color red.  In the default sRGB space.
	 */
	public final static Color RED = red;

	/**
	 * The color pink.  In the default sRGB space.
	 */
	public final static Color pink = new Color(255, 175, 175);

	/**
	 * The color pink.  In the default sRGB space.
	 */
	public final static Color PINK = pink;

	/**
	 * The color orange.  In the default sRGB space.
	 */
	public final static Color orange = new Color(255, 200, 0);

	/**
	 * The color orange.  In the default sRGB space.
	 */
	public final static Color ORANGE = orange;

	/**
	 * The color yellow.  In the default sRGB space.
	 */
	public final static Color yellow = new Color(255, 255, 0);

	/**
	 * The color yellow.  In the default sRGB space.
	 */
	public final static Color YELLOW = yellow;

	/**
	 * The color green.  In the default sRGB space.
	 */
	public final static Color green = new Color(0, 255, 0);

	/**
	 * The color green.  In the default sRGB space.
	 */
	public final static Color GREEN = green;

	/**
	 * The color magenta.  In the default sRGB space.
	 */
	public final static Color magenta = new Color(255, 0, 255);

	/**
	 * The color magenta.  In the default sRGB space.
	 */
	public final static Color MAGENTA = magenta;

	/**
	 * The color cyan.  In the default sRGB space.
	 */
	public final static Color cyan = new Color(0, 255, 255);

	/**
	 * The color cyan.  In the default sRGB space.
	 */
	public final static Color CYAN = cyan;

	/**
	 * The color blue.  In the default sRGB space.
	 */
	public final static Color blue = new Color(0, 0, 255);

	/**
	 * The color blue.  In the default sRGB space.
	 */
	public final static Color BLUE = blue;

	public Color(int redInt, int greenInt, int blueInt) {
		super();
		redValue = redInt & 0xFF;
		greenValue = greenInt & 0xFF;
		blueValue = blueInt & 0xFF;
	}
	public static Color decode(String hexString) {
		Integer intval = Integer.decode(hexString);
		int i = intval.intValue();
		return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
	}
	public int getRed() {
		return redValue;
	}
	public int getGreen() {
		return greenValue;
	}
	public int getBlue() {
		return blueValue;
	}
	/**
	 * Returns a string representation of this <code>Color</code>. This
	 * method is intended to be used only for debugging purposes.  The 
	 * content and format of the returned string might vary between 
	 * implementations. The returned string might be empty but cannot 
	 * be <code>null</code>.
	 * 
	 * @return  a string representation of this <code>Color</code>.
	 */
	public String toString() {
		return getClass().getName() + "[r=" + getRed() + ",g=" + getGreen() + ",b=" + getBlue() + "]";
	}
	public boolean equals(Object obj){
	    if (!(obj instanceof Color)) return false;
	    Color otherColor = (Color)obj;
	    return redValue == otherColor.getRed() && greenValue == otherColor.getGreen() && blueValue == otherColor.getBlue();
	}
	public int hashCode(){
	    return redValue + greenValue + blueValue;
	}
	/**
	 * HTML encode the Color. Example white = #FFFFFF.
	 * 
	 * @return String 
	 */
	public String toHTML() {
		return "#" + this.tohex(this.getRed())
				+ this.tohex(this.getGreen()) + this.tohex(this.getBlue());
	}
	/**
	 * Convert a number to a 2-character hex number.
	 * 
	 * @param num
	 * @return String (hex value)
	 */
	protected String tohex(int num) {
		String hex = Integer.toHexString(num);
		if (hex.length() == 1)
			hex = "0" + hex;
		return hex;
	}
}

