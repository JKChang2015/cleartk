/** 
 * Copyright (c) 2007-2008, Regents of the University of Colorado 
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
package org.cleartk.feature.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.uima.jcas.JCas;
import org.cleartk.ml.Feature;
import org.cleartk.ml.feature.extractor.NamedFeatureExtractor1;
import org.cleartk.syntax.constituent.type.TreebankNode;
import org.apache.uima.fit.util.JCasUtil;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Philipp Wetzler
 */

public class SubCategorizationExtractor implements NamedFeatureExtractor1<TreebankNode> {
  private String featureName;

  public SubCategorizationExtractor(String name) {
    this.featureName = Feature.createName(name, "SubCategorization");
  }

  public SubCategorizationExtractor() {
    this(null);
  }
  
  @Override
  public String getFeatureName() {
    return this.featureName;
  }

  public List<Feature> extract(JCas jCas, TreebankNode node)
      throws UnsupportedOperationException {

    TreebankNode parent = node.getParent();
    if (parent == null)
      return new ArrayList<Feature>();

    StringBuffer buffer = new StringBuffer();
    buffer.append(parent.getNodeType() + "->");
    boolean first = true;
    for (TreebankNode child : JCasUtil.select(parent.getChildren(), TreebankNode.class)) {
      if (!first)
        buffer.append("-");
      buffer.append(child.getNodeType());
      first = false;
    }

    return Collections.singletonList(new Feature(this.featureName, buffer));
  }
}
