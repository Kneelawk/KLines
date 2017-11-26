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

  // TODO Some engines only work with some models
  def setModels[Model: ModelRenderer](model: Model)
}