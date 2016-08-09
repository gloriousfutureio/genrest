package io.swagger.classy

trait ApiRegistry {

  def register(api: Api): Unit
}

class SimpleApiRegistry {

  private var _apis: Map[String, Api] = Map.empty
  def apis: Map[String, Api] = _apis

  def register(api: Api): Unit = {
    _apis += api.name -> api
  }
}

object ApiRegistry {

  implicit object global extends SimpleApiRegistry
}
