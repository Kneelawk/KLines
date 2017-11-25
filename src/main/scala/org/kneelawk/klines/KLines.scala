package org.kneelawk.klines

import org.lwjgl.Version
import org.lwjgl.glfw.Callbacks._
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWKeyCallbackI
import org.lwjgl.glfw.GLFWVidMode
import org.lwjgl.opengl.GL

object KLines {
  var window: Long = 0

  def main(args: Array[String]) {
    println("Hello LWJGL " + Version.getVersion + "!")

    try {
      init()
      loop()

      glfwFreeCallbacks(window)
      glfwDestroyWindow(window)
    } finally {
      glfwTerminate()
    }
  }

  def init() {
    GLFWErrorCallback.createPrint(Console.err)

    if (!glfwInit())
      throw new IllegalStateException("Failed to initialize GLFW")

    glfwDefaultWindowHints()
    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

    val WIDTH = 300
    val HEIGHT = 300

    window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
    if (window == NULL)
      throw new IllegalStateException("Failed to create GLFW window")

    glfwSetKeyCallback(window, new GLFWKeyCallbackI {
      def invoke(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
          glfwSetWindowShouldClose(window, true)
      }
    })

    val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

    glfwSetWindowPos(window, (vidMode.width() - WIDTH) / 2, (vidMode.height() - HEIGHT) / 2)

    glfwMakeContextCurrent(window)
    glfwSwapInterval(1)

    glfwShowWindow(window)
  }

  def loop() {
    GL.createCapabilities()

    glClearColor(0.2f, 0.2f, 0.2f, 1.0f)

    while (!glfwWindowShouldClose(window)) {
      glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT)

      glfwSwapBuffers(window)

      glfwPollEvents()
    }
  }
}