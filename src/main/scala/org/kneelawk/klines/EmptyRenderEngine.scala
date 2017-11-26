package org.kneelawk.klines

class EmptyRenderEngine extends RenderEngine {
  private var update: UpdateCallback = () => {}
  private var window: Window = null

  def init(window: Window, camera: Camera) {
    this.window = window

    GraphicsInterface.setupContext()

    GraphicsInterface.setBackground(0.2f, 0.2f, 0.2f, 1.0f)
  }

  def setUpdateCallback(callback: UpdateCallback) {
    update = callback
  }

  def loop() {
    while (!window.shouldWindowClose()) {
      SystemInterface.pollEvents()

      GraphicsInterface.update()

      update()

      window.refresh()
    }
  }

  def addModel[Model: ModelRenderer](model: Model): Unit = ???
  def removeModel[Model: ModelRenderer](model: Model): Unit = ???
  def clearModels(): Unit = ???
}