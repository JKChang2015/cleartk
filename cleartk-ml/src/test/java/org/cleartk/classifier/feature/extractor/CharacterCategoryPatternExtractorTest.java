/* 
 * Copyright (c) 2011, Regents of the University of Colorado 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution. 
 * Neither the name of the University of Colorado at Boulder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 */
package org.cleartk.classifier.feature.extractor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.cleartk.classifier.Feature;
import org.cleartk.classifier.feature.extractor.simple.CharacterCategoryPatternExtractor;
import org.cleartk.classifier.feature.extractor.simple.CharacterCategoryPatternExtractor.PatternType;
import org.cleartk.test.DefaultTestBase;
import org.cleartk.type.test.Token;
import org.junit.Test;
import org.uimafit.util.JCasUtil;

/**
 * <br>
 * Copyright (c) 2011, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Steven Bethard
 */
public class CharacterCategoryPatternExtractorTest extends DefaultTestBase {

  @Test
  public void testOnePerChar() throws Exception {
    CharacterCategoryPatternExtractor extractor = new CharacterCategoryPatternExtractor();
    this.tokenBuilder.buildTokens(this.jCas, "spam 42 XXyy C3p0 A1-b4.");
    Iterator<Token> tokensIter = JCasUtil.select(this.jCas, Token.class).iterator();
    Token spam = tokensIter.next();
    Token fortyTwo = tokensIter.next();
    Token xxyy = tokensIter.next();
    Token c3p0 = tokensIter.next();
    Token a1b4 = tokensIter.next();
    Assert.assertFalse(tokensIter.hasNext());

    this.assertFeature("CharPattern", "LlLlLlLl", extractor, spam);
    this.assertFeature("CharPattern", "NdNd", extractor, fortyTwo);
    this.assertFeature("CharPattern", "LuLuLlLl", extractor, xxyy);
    this.assertFeature("CharPattern", "LuNdLlNd", extractor, c3p0);
    this.assertFeature("CharPattern", "LuNdPdLlNdPo", extractor, a1b4);
  }

  @Test
  public void testRepeatsMerged() throws Exception {
    CharacterCategoryPatternExtractor extractor = new CharacterCategoryPatternExtractor(
        PatternType.REPEATS_MERGED);
    this.tokenBuilder.buildTokens(this.jCas, "spam 42 XXyy C3p0 AC1-b43.!");
    Iterator<Token> tokensIter = JCasUtil.select(this.jCas, Token.class).iterator();
    Token spam = tokensIter.next();
    Token fortyTwo = tokensIter.next();
    Token xxyy = tokensIter.next();
    Token c3p0 = tokensIter.next();
    Token ac1b43 = tokensIter.next();
    Assert.assertFalse(tokensIter.hasNext());

    this.assertFeature("CharPatternRepeatsMerged", "Ll", extractor, spam);
    this.assertFeature("CharPatternRepeatsMerged", "Nd", extractor, fortyTwo);
    this.assertFeature("CharPatternRepeatsMerged", "LuLl", extractor, xxyy);
    this.assertFeature("CharPatternRepeatsMerged", "LuNdLlNd", extractor, c3p0);
    this.assertFeature("CharPatternRepeatsMerged", "LuNdPdLlNdPo", extractor, ac1b43);
  }

  private void assertFeature(
      String name,
      Object value,
      CharacterCategoryPatternExtractor extractor,
      Token token) throws Exception {
    List<Feature> expected = Arrays.asList(new Feature(name, value));
    List<Feature> actual = extractor.extract(this.jCas, token);
    Assert.assertEquals(expected, actual);
  }
}