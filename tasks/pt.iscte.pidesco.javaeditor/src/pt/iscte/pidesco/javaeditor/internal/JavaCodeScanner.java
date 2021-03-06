/*******************************************************************************
 * Copyright (c) 2014 Andre L. Santos.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Andre L Santos - developer
 ******************************************************************************/
package pt.iscte.pidesco.javaeditor.internal;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;


public class JavaCodeScanner extends RuleBasedScanner {
	private final static String[] javaKeyword =
		{ "abstract", "continue", "for", "new", "switch", "assert", "default",
			"goto",	"package", "synchronized", "boolean", "do", "if", "private",
			"this", "break", "double",	"implements", "protected", "throw",
			"byte",	"else",	"import",	"public",	"throws", "case", "enum",
			"instanceof", "return",	"transient", "catch", "extends", "int",	
			"short", "try", "char",	"final", "interface", "static",	"void",
			"class", "finally",	"long",	"strictfp",	"volatile", "const", "float",
			"native", "super",	"while"};
	
	private static IToken comment = Common.createToken(TokenColor.COMMENT, false);
	private static IToken javadoc = Common.createToken(TokenColor.COMMENT, true);
	private static IToken string = Common.createToken(TokenColor.STRING, false);
	private static IToken character = Common.createToken(TokenColor.CHAR, false);
	private static IToken keyword = Common.createToken(TokenColor.KEYWORD, true);
	private static IToken number =  Common.createToken(TokenColor.NUMBER, true);
	private static IToken id =  Common.createToken(TokenColor.ID, true);
	private static IToken trueLit =  Common.createToken(TokenColor.TRUE, true);
	private static IToken falseLit =  Common.createToken(TokenColor.FALSE, true);
	private static IToken nullLit =  Common.createToken(TokenColor.NULL, true);
	private static IToken defaultToken = Common.createToken(TokenColor.BLACK, false);

	public JavaCodeScanner() {
		IRule[] rules = new IRule[] {

				new UniqueWordRule(trueLit, "true"),
				new UniqueWordRule(falseLit, "false"),
				new UniqueWordRule(nullLit, "null"),
				
								new EndOfLineRule("//", comment),
				//				new SingleLineRule("\\\\", "", comment),
				new SingleLineRule("\"", "\"", string, '\\'),
				new SingleLineRule("'", "'", character, '\\'),
//				new UniqueWordRule(keyword, javaKeyword),
				new KeyWordRule(),
				//				new MultiLineRule("/**", "*/", javadoc),
//								new MultiLineRule("/*", "*/", comment),

				new IntRule(number),
				new WhitespaceRule(new IWhitespaceDetector() {
					public boolean isWhitespace(char c) {
						return Character.isWhitespace(c);
					}
				})
		};
		setRules(rules);
	}



	private static class KeyWordRule extends WordRule {
		public KeyWordRule() {
			super(new IWordDetector() {
				@Override
				public boolean isWordStart(char c) {
					return Character.isJavaIdentifierStart(c);
				}

				@Override
				public boolean isWordPart(char c) {
					return Character.isJavaIdentifierPart(c);
				}}, defaultToken);

			for(String word : javaKeyword)
				addWord(word, keyword);
		}
	}

	
	private static class UniqueWordRule extends WordRule {
		public UniqueWordRule(final IToken token, final String ... words) {
			super(new IWordDetector() {
				@Override
				public boolean isWordStart(char c) {
					for(String w : words)
						if(w.charAt(0) == c)
							return true;
					return false;
				}

				@Override
				public boolean isWordPart(char c) {
					return c >= 'a' && c <= 'w';
				}}, defaultToken);

			for(String w : words)
				addWord(w, token);
		}
	}
}
