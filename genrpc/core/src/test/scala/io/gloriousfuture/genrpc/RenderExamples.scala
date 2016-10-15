package io.gloriousfuture.genrpc

object RenderExamples {

  object Shapes {

    final val model: ModelFile = {
      val name = Field("name", "String")
      val area = Field("area", "Double")
      val shape = SealedTrait("Shape", Seq(), Seq(name, area))
      ModelFile(
        "io.gloriousfuture.example",
        Imports.empty,
        Seq(
          SealedTrait("Shape", Seq(), Seq(name, area)),
          CaseClass("Triangle", Seq(shape), Seq(Field("a", "Int"), Field("b", "Int"), Field("c", "Int")), Seq(
            Definition(name, "Triangle"),
            Definition(area, "{\n" +
              "val p: Double = (a + b + c) / 2\n" +
              "math.sqrt(p * (p - a) * (p - b) * (p - c))\n" +
              "}")
          )),
          CaseClass("Rectangle", Seq(shape), Seq(Field("length", "Int"), Field("width", "Int")), Seq(
            Definition(name, "Rectangle"),
            Definition(area, "length * width")
          ))
        )
      )
    }

    final val rendered: String = {
      """package io.gloriousfuture.example
        |
        |sealed trait Shape {
        |
        |  def name: String
        |
        |  def area: Double
        |}
        |
        |case class Triangle(a: Int, b: Int, c: Int) extends Shape {
        |
        |  final override def name: String = Triangle
        |
        |  final override def area: Double = {
        |    val p: Double = (a + b + c) / 2
        |    math.sqrt(p * (p - a) * (p - b) * (p - c))
        |  }
        |
        |}
        |
        |case class Rectangle(length: Int, width: Int) extends Shape {
        |
        |  final override def name: String = Rectangle
        |
        |  final override def area: Double = length * width
        |
        |}
        |""".stripMargin
    }
  }
}
