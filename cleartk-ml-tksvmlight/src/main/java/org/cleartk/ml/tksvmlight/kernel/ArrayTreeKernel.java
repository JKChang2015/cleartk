/*
 * Copyright (c) 2007-2013, Regents of the University of Colorado 
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
package org.cleartk.ml.tksvmlight.kernel;


import java.util.Iterator;
import java.util.Map;

import org.cleartk.ml.tksvmlight.TreeFeature;
import org.cleartk.ml.tksvmlight.TreeFeatureVector;

/**
 * 
 * 
 * <br>
 * Copyright (c) 2015, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * A kernel for combining other types of kernels into an ensemble.
 * 
 * @author Tim Miller
 */

public class ArrayTreeKernel implements TreeKernel {

  /**
   * 
   */
  private static final long serialVersionUID = -4166036694733905043L;
  Map<String,ComposableTreeKernel> kernels = null;
  
  public ArrayTreeKernel(Map<String,ComposableTreeKernel> kernels){
    this.kernels = kernels;
  }
  
  @Override
  public double evaluate(TreeFeatureVector fv1, TreeFeatureVector fv2) {
    double score = 0.0;
    Iterator<TreeFeature> trees1 = fv1.getTrees().values().iterator();
    Iterator<TreeFeature> trees2 = fv2.getTrees().values().iterator();
    
    while(trees1.hasNext() && trees2.hasNext()){
      TreeFeature tf1 = trees1.next();
      TreeFeature tf2 = trees2.next();
      assert tf1.getName().equals(tf2.getName());
      score += this.kernels.get(tf1.getName()).evaluate(tf1, tf2);
    }
    
    return score;
  }

  public ComposableTreeKernel getKernel(String key){
    return kernels.get(key);
  }
}
