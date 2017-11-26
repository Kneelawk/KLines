package org.kneelawk.klines

/*
 * I wonder if you could have all the rendering code be in special
 * implicit ModelRenderers and have each one know how to render a specific kind of model?
 */

trait RenderEngine {
  type UpdateCallback = () => Unit

  /**
   * Initialize the engine.
   */
  def init(window: Window, camera: Camera)

  /**
   * Start the engine's render loop.
   */
  def loop()

  /**
   * Set the callback for every render cycle.
   */
  def setUpdateCallback(callback: UpdateCallback)

  def addModel[Model: ModelRenderer](model: Model)
  
  def removeModel[Model: ModelRenderer](model: Model)
  
  def clearModels()
}