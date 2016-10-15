package io.gloriousfuture.genrpc

object RenderScala {

  // TODO: How to handle validation? Use non-empty lists?

  implicit val renderImport: Render[Imports] = Render.instance { x =>
    val groupedImports = x.imports.foldLeft(Map.empty[String, List[String]]) { (imports, path) =>
      val pathParts = path.split('.')
      val packagePath = pathParts.init.mkString(".")
      val packageKey = pathParts.last
      imports.updated(packagePath, packageKey :: imports.getOrElse(packagePath, Nil))
    }
    // TODO: Format imports better?
    val imports = for {
      (packagePath, packageKeys) <- groupedImports
    } yield {
      s"import $packagePath.${packageKeys match {
        case head :: Nil => head
        case _ => packageKeys.mkString("{ ", ", ", " }")
      }}"
    }
    imports.mkString("\n")
  }

  implicit val renderField: Render[Field] = Render.instance { field =>
    s"${field.name}: ${field.typeName}"
  }

  implicit val renderDefinition: Render[Definition] = Render.instance { defined =>
    val trimmed = defined.value.trim
    val bodySansParens = if (trimmed.charAt(0) == '{') trimmed.substring(1, trimmed.length - 1).trim else trimmed
    val indentedBody = Render.expandMany(bodySansParens.lines, "{\n    ", "\n    ", "\n  }")
    s"${Render.toString(defined.field)} = $indentedBody"
  }

  def renderInherits(superclasses: Seq[SealedTrait]): String = {
    Render.expand(superclasses.map(_.name), " extends ", " with ", "")
  }

  def renderConcreteModelBody(model: ConcreteModel): String = {
    val inherited = Model.linearizeAncestors(model).flatMap(_.methods).toSet
    def renderMethod(method: Definition): String = {
      s"  final ${if (inherited(method.field)) "override " else ""}def ${Render.toString(method)}"
    }
    Render.expand(model.methods.map(renderMethod), " {\n\n", "\n\n", "\n\n}")
  }

  implicit val renderCaseClass: Render[CaseClass] = Render.instance { cls =>
    s"case class ${cls.name}(${cls.fields.map(Render[Field].render).mkString(", ")})${
      renderInherits(cls.superclasses)
    }${
      renderConcreteModelBody(cls)
    }"
  }

  implicit val renderCaseObject: Render[CaseObject] = Render.instance { obj =>
    s"case object ${obj.name}${
      renderInherits(obj.superclasses)
    }${
      renderConcreteModelBody(obj)
    }"
  }

  implicit val renderSealedTrait: Render[SealedTrait] = Render.instance { trt =>
    s"sealed trait ${trt.name}${
      renderInherits(trt.superclasses)
    }${
      Render.expand(trt.methods.map(f => s"  def ${Render.toString(f)}"), " {\n\n", "\n\n", "\n}")
    }"
  }

  implicit lazy val renderModel: Render[Model] = Render.instance {
    case trt: SealedTrait => Render.toString(trt)
    case cls: CaseClass   => Render.toString(cls)
    case obj: CaseObject  => Render.toString(obj)
  }

  implicit val renderModelFile: Render[ModelFile] = Render.instance { file =>
    s"package ${file.packagePath}${
      Render.expand(file.imports, "\n\n", "\n", "")
    }${
      Render.expand(file.models, "\n\n", "\n\n", "\n")
    }"
  }

}
