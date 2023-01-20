package uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller.advice

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.controller.AssessmentController
import uk.gov.justice.digital.hmpps.hmppsarnassessmentdataapi.eventSourcing.Views

@RestControllerAdvice(assignableTypes = [AssessmentController::class])
class JsonViewAdvice : AbstractMappingJacksonResponseBodyAdvice() {
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }

  override fun beforeBodyWriteInternal(
    bodyContainer: MappingJacksonValue,
    contentType: MediaType,
    returnType: MethodParameter,
    request: ServerHttpRequest,
    response: ServerHttpResponse
  ) {
    val role = request.headers["role"]?.first() ?: "BASIC"

    log.info("Request received, assigning role $role")

    bodyContainer.serializationView = Views.from(role)
  }
}
