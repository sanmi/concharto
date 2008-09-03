package com.tech4d.tsm.web.wiki;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.tech4d.tsm.auth.AuthHelper;
import com.tech4d.tsm.web.util.UrlFormat;

public class SubstitutionMacro {
	private static final String USER_TAGS = "~~~~";

	public static String postSignature(HttpServletRequest request, String rendered) {
		return postSignature(UrlFormat.getBasepath(request), rendered);
	}
	
	public static String postSignature(String basePath, String rendered) {
		//implement ~~~ substitution for username
		String username = AuthHelper.getUsername();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a z");
		StringBuffer replaceWith = new StringBuffer()
			.append("[[User:")
			.append(username)
			.append(" | ")
			.append(username)
			.append("]] ")
			.append(sdf.format(new Date()));
		rendered = StringUtils.replace(rendered, USER_TAGS, replaceWith.toString());
		return rendered;
	}
	
	private static String UL_STYLE = "margin-top:.2em;margin-bottom:.2em;padding-top:.2em;padding-bottom:.2em;list-style-position:inside;";
	private static String OL_STYLE = UL_STYLE;
	private static String LI_STYLE = "";
	private static String TD_STYLE = "padding: .2em .5em; margin: 0; border: 1px solid #e0e0e0;";
	private static String TH_STYLE = TD_STYLE+ "background-color: #e0e0e0;";
	private static String HEADER_STYLE = "font-weight: bold;margin: .3em 0 0 0;padding: 0;";
	private static String H1_STYLE = HEADER_STYLE + "font-size: 11pt; 	border-bottom: 1px solid #e0e0e0;";
	private static String H2_STYLE = HEADER_STYLE + "font-size: 10pt; font-style: italic;";
	private static String H3_STYLE = HEADER_STYLE + "font-size: 10pt;";
	private static String H4_STYLE = HEADER_STYLE + "font-size: 10pt; font-weight: normal;";
	private static String H5_STYLE = HEADER_STYLE + "font-size: 10pt; font-weight: normal;";
	private static String TABLE_STYLE = "width: auto;";
	private static String PRE_STYLE = "width: auto;padding: 0 .2em;margin: 0;border: 1px dashed #e0e0e0;background-color: #FAFAFA;";

	/**
	 * Google maplets and google earth rendering doesn't allow CSS stylesheets, so we need to hard code
	 * the style right into the rendered text.  This is a crude way to implement that.  It would have 
	 * been better to have a plug-in for the bliki rendering engine, but I haven't found a way to
	 * do that yet.
	 * @param rendered
	 * @return
	 */
	public static String explicitStyles(String rendered) {
		rendered = addStyle(rendered, "ul", UL_STYLE );
		rendered = addStyle(rendered, "ol", OL_STYLE );
		rendered = addStyle(rendered, "li", LI_STYLE );
		rendered = addStyle(rendered, "td", TD_STYLE );
		rendered = addStyle(rendered, "th", TH_STYLE);
		rendered = addStyle(rendered, "table", TABLE_STYLE);
		rendered = addStyle(rendered, "pre", PRE_STYLE);
		rendered = addStyle(rendered, "h1", H1_STYLE);
		rendered = addStyle(rendered, "h2", H2_STYLE);
		rendered = addStyle(rendered, "h3", H3_STYLE);
		rendered = addStyle(rendered, "h4", H4_STYLE);
		rendered = addStyle(rendered, "h5", H5_STYLE);
		return rendered;
	}

	private static String addStyle(String rendered, String tag, String style) {
		return StringUtils.replace(rendered, "<"+ tag + ">", "<"+ tag +" style='" + style + "'>");
	}

}
