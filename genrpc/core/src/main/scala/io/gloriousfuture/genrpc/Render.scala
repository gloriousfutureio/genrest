package io.gloriousfuture.genrpc

trait Render[T] {

  def render(value: T): String
}

object Render {

  def apply[T](implicit instance: Render[T]): Render[T] = instance

  def instance[T](f: T => String): Render[T] = new Render[T] {
    override def render(value: T): String = f(value)
  }

  def toString[T](value: T)(implicit render: Render[T]): String = render.render(value)

  def mkString[T: Render](all: TraversableOnce[T], sep: String): String = {
    all.map(Render.toString(_)).mkString(sep)
  }

  def mkString[T: Render](all: TraversableOnce[T], start: String, sep: String, end: String): String = {
    all.map(Render.toString(_)).mkString(start, sep, end)
  }

  def expand[T: Render](all: TraversableOnce[T], start: String, sep: String, end: String): String = {
    if (all.isEmpty) ""
    else mkString(all, start, sep, end)
  }

  def expandMany[T: Render](all: TraversableOnce[T], start: String, sep: String, end: String): String = {
    if (all.isEmpty) ""
    else {
      val stream = all.toStream
      if (stream.tail.isEmpty) Render.toString(stream.head)
      else mkString(stream, start, sep, end)
    }
  }

  implicit val renderString: Render[String] = instance(identity)
}
