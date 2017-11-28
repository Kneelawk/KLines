package org.kneelawk.klines

trait ModelRenderer[Model] {
  def initialized: Boolean
  
  def render(model: Model)
}