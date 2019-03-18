package org.kneelawk.klines

/*
 * I wonder if you could have all the rendering code be in special
 * implicit ModelRenderers and have each one know how to render a specific kind of model?
 */

class RenderEngine(window: Window, camera: Camera, update: () => Unit = () => {}) {
  GraphicsInterface.setupContext()

  GraphicsInterface.setBackground(0.2f, 0.2f, 0.2f, 1.0f)

  def loop() {
    while (!window.shouldWindowClose()) {
      SystemInterface.pollEvents()

      GraphicsInterface.update()

      update()

      window.refresh()
    }
  }

  def addModel(model: Model): Unit = ???

  def removeModel(model: Model): Unit = ???

  def clearModels(): Unit = ???
}