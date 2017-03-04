package io.gloriousfuture.genspec.openapi.v2

/**
  * Describes a single operation parameter (aka "Parameter Object").
  *
  * @see https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#parameterObject
  *
  * A unique parameter is defined by a combination of a name and location.
  *
  * There are five possible parameter types:
  *
  * Path - Used together with Path Templating, where the parameter value is actually part of the operation's URL.
  *        This does not include the host or base path of the API.
  *        For example, in /items/{itemId}, the path parameter is itemId.
  *
  * Query - Parameters that are appended to the URL. For example, in /items?id=###, the query parameter is id.
  *
  * Header - Custom headers that are expected as part of the request.
  *
  * Body - The payload that's appended to the HTTP request. Since there can only be one payload, there can only be
  *        one body parameter. The name of the body parameter has no effect on the parameter itself and is used for
  *        documentation purposes only. Since Form parameters are also in the payload, body and form parameters
  *        cannot exist together for the same operation.
  *
  * Form - Used to describe the payload of an HTTP request when either application/x-www-form-urlencoded,
  *        multipart/form-data or both are used as the content type of the request
  *        (in Swagger's definition, the consumes property of an operation).
  *        This is the only parameter type that can be used to send files, thus supporting the file type.
  *        Since form parameters are sent in the payload, they cannot be declared together with a body parameter
  *        for the same operation. Form parameters have a different format based on the content-type used
  *        (for further details, consult http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4):
  *
  *        - application/x-www-form-urlencoded - Similar to the format of Query parameters but as a payload.
  *          For example, `foo=1&bar=swagger` - both foo and bar are form parameters.
  *          This is normally used for simple parameters that are being transferred.
  *
  *        - multipart/form-data - each parameter takes a section in the payload with an internal header.
  *          For example, for the header `Content-Disposition: form-data; name="submit-name"`
  *          the name of the parameter is submit-name. This type of form parameters is more commonly used for file transfers.
  */
sealed trait ApiParam {

  /**
    * The name of the parameter. Parameter names are case sensitive.
    *
    * If in is "path", the name field MUST correspond to the associated path segment from the path field
    * in the Paths Object. See Path Templating for further information. For all other cases, the name
    * corresponds to the parameter name used based on the in property.
    */
  def name: String

  /**
    * The location of the parameter. Possible values are enumerated by subclasses of [[ApiParam#InLoc]].
    */
  def in: String

  /**
    * A brief description of the parameter. This could contain examples of use.
    *
    * @note GFM (github flavored markdown) syntax can be used for rich text representation.
    */
  def description: Option[String]

  /**
    * Determines whether this parameter is mandatory. If the parameter is [[ApiParam#InPath]],
    * this property is required and its value MUST be true. Otherwise, the property MAY be included
    * and its default value is false.
    */
  def required: Boolean
}

object ApiParam {

  final val IN_QUERY = "query"
  final val IN_HEADER = "header"
  final val IN_PATH = "path"
  final val IN_FORM_DATA = "formData"
  final val IN_BODY = "body"

  case class InQuery(name: String, description: Option[String] = None, required: Boolean = false) extends ApiParam {
    override def in: String = IN_QUERY
  }
  case class InHeader(name: String, description: Option[String] = None, required: Boolean = false) extends ApiParam {
    override def in: String = IN_HEADER
  }
  case class InPath(name: String, description: Option[String] = None) extends ApiParam {
    override def in: String = IN_PATH
    // Always true for path parameters
    override def required: Boolean = true
  }
  case class InFormData(name: String, description: Option[String] = None, required: Boolean = false) extends ApiParam {
    override def in: String = IN_FORM_DATA
  }
  case class InBody(name: String, schema: JsonRefOr[ModelPrimitive], description: Option[String] = None, required: Boolean = false) extends ApiParam {
    override def in: String = IN_BODY
  }
}
