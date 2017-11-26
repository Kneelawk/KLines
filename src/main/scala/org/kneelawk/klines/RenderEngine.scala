package org.kneelawk.klines

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
  def setModels()
}