package io.gloriousfuture.genspec.openapi.v2.circe

import cats.Show
import cats.syntax.show._
import io.circe._
import io.circe.generic.semiauto._
import io.circe.syntax._
import io.gloriousfuture.genspec.openapi.v2._

import scala.reflect.ClassTag
import scala.util.{Failure, Success}

object CirceFormats extends CirceFormats(implicitly)
class CirceFormats(showFailure: Show[DecodingFailure]) extends GenericModelFormatsAdaptor[Decoder, Encoder] {

  override implicit lazy val decodeJsonRef: Decoder[JsonRef] = Decoder.instance { c =>
    val refC = c.downField("$ref")
    refC.as[String].right.flatMap { path =>
      JsonRef.parse(path) match {
        case Success(ref) => Right(ref)
        case Failure(ex) =>
          Left(DecodingFailure.fromThrowable(ex, refC.history))
      }
    }
  }

  override implicit lazy val encodeJsonRef: Encoder[JsonRef] = Encoder.instance { ref =>
    Json.obj("$ref" -> Json.fromString(ref.fullPath))
  }

  override def decodeJsonRefOr[X: Decoder: ClassTag]: Decoder[JsonRefOr[X]] = {
    val jsonRefClassName = classOf[JsonRef].getSimpleName
    val xClassName = implicitly[ClassTag[X]].runtimeClass.getSimpleName
    Decoder.instance { c =>
      c.as[JsonRef]
        .right.map { ref => Left(ref) }
        .left.flatMap { refErr =>
        c.as[X]
          .right.map(x => Right(x))
          .left.map(xErr => {
          val indentedXError = xErr.show.split("\n").mkString("\n  ")
          DecodingFailure(
            s"Could not parse an instance of Either[$jsonRefClassName, $xClassName].\n" +
              s"* as[$jsonRefClassName]: ${refErr.show}\n" +
              s"* as[$xClassName]: $indentedXError",
            c.history
          )
        })
      }
    }
  }

  override def encodeJsonRefOr[X: Encoder]: Encoder[JsonRefOr[X]] = Encoder.instance[JsonRefOr[X]] {
    case Right(x) => x.asJson
    case Left(ref) => ref.asJson
  }

  override implicit lazy val decodeApiPath: Decoder[ApiPath] = Decoder.decodeString.emapTry(ApiPath.parse)
  override implicit lazy val encodeApiPath: Encoder[ApiPath] = Encoder.encodeString.contramap(_.path)

  implicit lazy val keyDecodeApiPath: KeyDecoder[ApiPath] = KeyDecoder.instance(p => Some(ApiPath.parse(p).get))
  implicit lazy val keyEncodeApiPath: KeyEncoder[ApiPath] = KeyEncoder.encodeKeyString.contramap(_.path)

  override implicit lazy val decodeApiPaths: Decoder[Map[ApiPath, JsonRefOr[PathOps]]] = Decoder.decodeMapLike[Map, ApiPath, JsonRefOr[PathOps]]
  override implicit lazy val encodeApiPaths: Encoder[Map[ApiPath, JsonRefOr[PathOps]]] = Encoder.encodeMapLike[Map, ApiPath, JsonRefOr[PathOps]]

  override implicit lazy val decodeContact: Decoder[Contact] = deriveDecoder
  override implicit lazy val encodeContact: Encoder[Contact] = deriveEncoder

  override implicit lazy val decodeDefinitions: Decoder[Map[String, JsonRefOr[ModelDefinition]]] = Decoder.decodeMapLike[Map, String, JsonRefOr[ModelDefinition]]
  override implicit lazy val encodeDefinitions: Encoder[Map[String, JsonRefOr[ModelDefinition]]] = Encoder.encodeMapLike[Map, String, JsonRefOr[ModelDefinition]]

  override implicit lazy val decodeExternalDocs: Decoder[ExternalDocs] = deriveDecoder
  override implicit lazy val encodeExternalDocs: Encoder[ExternalDocs] = deriveEncoder

  override implicit lazy val decodeInfo: Decoder[Info] = deriveDecoder
  override implicit lazy val encodeInfo: Encoder[Info] = deriveEncoder

  override implicit lazy val decodeLicense: Decoder[License] = deriveDecoder
  override implicit lazy val encodeLicense: Encoder[License] = deriveEncoder

  override implicit lazy val decodeModelDefinition: Decoder[ModelDefinition] = deriveDecoder
  override implicit lazy val encodeModelDefinition: Encoder[ModelDefinition] = deriveEncoder

  override implicit lazy val decodeModelPrimitive: Decoder[ModelPrimitive] = deriveDecoder
  override implicit lazy val encodeModelPrimitive: Encoder[ModelPrimitive] = deriveEncoder

  override implicit lazy val decodeRequestParam: Decoder[ApiParam] = {
    val decodedInBody = deriveDecoder[ApiParam.InBody]
    val decodedInFormData = deriveDecoder[ApiParam.InFormData]
    val decodedInHeader = deriveDecoder[ApiParam.InHeader]
    val decodedInPath = deriveDecoder[ApiParam.InPath]
    val decodedInQuery = deriveDecoder[ApiParam.InQuery]
    Decoder.instance { c =>
      val inField = c.downField("in")
      inField.as[String].right.flatMap {
        case ApiParam.IN_BODY => c.as(decodedInBody)
        case ApiParam.IN_FORM_DATA => c.as(decodedInFormData)
        case ApiParam.IN_HEADER => c.as(decodedInHeader)
        case ApiParam.IN_PATH => c.as(decodedInPath)
        case ApiParam.IN_QUERY => c.as(decodedInQuery)
        case unknown => Left(DecodingFailure(s"Unrecognized ApiParam.in field, '$unknown'", inField.history))
      }
    }
  }
  override implicit lazy val encodeRequestParam: Encoder[ApiParam] = deriveEncoder

  override implicit lazy val decodePathOperation: Decoder[PathOperation] = deriveDecoder
  override implicit lazy val encodePathOperation: Encoder[PathOperation] = deriveEncoder

  override implicit lazy val decodePathOps: Decoder[PathOps] = deriveDecoder
  override implicit lazy val encodePathOps: Encoder[PathOps] = deriveEncoder

  override implicit lazy val decodeResponses: Decoder[Responses] = deriveDecoder
  override implicit lazy val encodeResponses: Encoder[Responses] = deriveEncoder

  override implicit lazy val decodeRoot: Decoder[Root] = deriveDecoder
  override implicit lazy val encodeRoot: Encoder[Root] = deriveEncoder

  override implicit lazy val decodeSecurityDefinitions: Decoder[SecurityDefinitions] = deriveDecoder
  override implicit lazy val encodeSecurityDefinitions: Encoder[SecurityDefinitions] = deriveEncoder

  override implicit lazy val decodeSecurityRequirement: Decoder[SecurityRequirement] = deriveDecoder
  override implicit lazy val encodeSecurityRequirement: Encoder[SecurityRequirement] = deriveEncoder

  override implicit lazy val decodeTag: Decoder[Tag] = deriveDecoder
  override implicit lazy val encodeTag: Encoder[Tag] = deriveEncoder

}
